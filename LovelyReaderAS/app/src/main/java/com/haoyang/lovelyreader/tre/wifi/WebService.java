package com.haoyang.lovelyreader.tre.wifi;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.haoyang.lovelyreader.tre.bean.FileBean;
import com.haoyang.lovelyreader.tre.helper.Configs;
import com.haoyang.lovelyreader.tre.helper.OnBookAddEvent;
import com.java.common.service.file.FileNameService;
import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.http.body.MultipartFormDataBody;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.async.http.body.UrlEncodedFormBody;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.mjiayou.trecorelib.base.TCService;
import com.mjiayou.trecorelib.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

public class WebService extends TCService {

    static final String ACTION_START_WEB_SERVICE = "com.baidusoso.wifitransfer.action.START_WEB_SERVICE";
    static final String ACTION_STOP_WEB_SERVICE = "com.baidusoso.wifitransfer.action.STOP_WEB_SERVICE";

    private static final String TEXT_CONTENT_TYPE = "text/html;charset=utf-8";
    private static final String CSS_CONTENT_TYPE = "text/css;charset=utf-8";
    private static final String BINARY_CONTENT_TYPE = "application/octet-stream";
    private static final String JS_CONTENT_TYPE = "application/javascript";
    private static final String PNG_CONTENT_TYPE = "application/x-png";
    private static final String JPG_CONTENT_TYPE = "application/jpeg";
    private static final String SWF_CONTENT_TYPE = "application/x-shockwave-flash";
    private static final String WOFF_CONTENT_TYPE = "application/x-font-woff";
    private static final String TTF_CONTENT_TYPE = "application/x-font-truetype";
    private static final String SVG_CONTENT_TYPE = "image/svg+xml";
    private static final String EOT_CONTENT_TYPE = "image/vnd.ms-fontobject";
    private static final String MP3_CONTENT_TYPE = "audio/mp3";
    private static final String MP4_CONTENT_TYPE = "video/mpeg4";
    FileUploadHolder fileUploadHolder = new FileUploadHolder();
    private AsyncHttpServer server = new AsyncHttpServer();
    private AsyncServer mAsyncServer = new AsyncServer();

    public static void start(Context context) {
        Intent intent = new Intent(context, WebService.class);
        intent.setAction(ACTION_START_WEB_SERVICE);
        context.startService(intent);
    }

    public static void stop(Context context) {
        Intent intent = new Intent(context, WebService.class);
        intent.setAction(ACTION_STOP_WEB_SERVICE);
        context.startService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_START_WEB_SERVICE.equals(action)) {
                startServer();
            } else if (ACTION_STOP_WEB_SERVICE.equals(action)) {
                stopSelf();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (server != null) {
            server.stop();
        }
        if (mAsyncServer != null) {
            mAsyncServer.stop();
        }
    }

    private void startServer() {
        LogUtils.d(TAG, "startServer() called");
        server.get("/images/.*", this::sendResources);
        server.get("/scripts/.*", this::sendResources);
        server.get("/css/.*", this::sendResources);
        // index page
        server.get("/", (AsyncHttpServerRequest request, AsyncHttpServerResponse response) -> {
            LogUtils.d(TAG, "startServer() called | index page");
            try {
                response.send(getIndexContent());
            } catch (IOException e) {
                e.printStackTrace();
                response.code(500).end();
            }
        });
        // query upload list
        server.get("/files", (AsyncHttpServerRequest request, AsyncHttpServerResponse response) -> {
            LogUtils.d(TAG, "startServer() called | query upload list");
            JSONArray array = new JSONArray();
            File dir = new File(Configs.DIR_SDCARD_PROJECT_BOOK);
            if (dir.exists() && dir.isDirectory()) {
                String[] fileNames = dir.list();
                if (fileNames != null) {
                    for (String fileName : fileNames) {
                        File file = new File(dir, fileName);
                        if (file.exists() && file.isFile()) {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("name", fileName);
                                long fileLen = file.length();
                                DecimalFormat df = new DecimalFormat("0.00");
                                if (fileLen > 1024 * 1024) {
                                    jsonObject.put("size", df.format(fileLen * 1f / 1024 / 1024) + "MB");
                                } else if (fileLen > 1024) {
                                    jsonObject.put("size", df.format(fileLen * 1f / 1024) + "KB");
                                } else {
                                    jsonObject.put("size", fileLen + "B");
                                }
                                array.put(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            response.send(array.toString());
        });
        // upload
        server.post("/files", (AsyncHttpServerRequest request, AsyncHttpServerResponse response) -> {
                    LogUtils.d(TAG, "startServer() called | upload");
                    final MultipartFormDataBody body = (MultipartFormDataBody) request.getBody();
                    body.setMultipartCallback((Part part) -> {
                        if (part.isFile()) {
                            body.setDataCallback((DataEmitter emitter, ByteBufferList bb) -> {
                                fileUploadHolder.write(bb.getAllByteArray());
                                bb.recycle();
                            });
                        } else {
                            if (body.getDataCallback() == null) {
                                body.setDataCallback((DataEmitter emitter, ByteBufferList bb) -> {
                                    try {
                                        String fileName = URLDecoder.decode(new String(bb.getAllByteArray()), "UTF-8");
                                        fileUploadHolder.setFileName(fileName);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    bb.recycle();
                                });
                            }
                        }
                    });
                    request.setEndCallback((Exception e) -> {
                        fileUploadHolder.reset();
                        response.end();

                        File receivedFile = fileUploadHolder.getReceivedFile();
                        String filePath = receivedFile.getAbsolutePath();
                        FileNameService mFileNameService = new FileNameService();
                        String fileName = mFileNameService.getFileName(filePath);
                        String fileSuffix = mFileNameService.getFileExtendName(filePath);

                        FileBean fileBean = new FileBean();
                        fileBean.setName(fileName);
                        fileBean.setSuffix(fileSuffix);
                        fileBean.setPath(filePath);
                        fileBean.setFolder(false);

                        EventBus.getDefault().post(new OnBookAddEvent(fileBean));
                        LogUtils.d(TAG, "EventBus.getDefault().post(new OnBookAddEvent(fileBean));");
                    });
                }
        );
        // delete
        server.post("/files/.*", (AsyncHttpServerRequest request, AsyncHttpServerResponse response) -> {
            LogUtils.d(TAG, "startServer() called | delete");
            final UrlEncodedFormBody body = (UrlEncodedFormBody) request.getBody();
            if ("delete".equalsIgnoreCase(body.get().getString("_method"))) {
                String path = request.getPath().replace("/files/", "");
                try {
                    path = URLDecoder.decode(path, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                File file = new File(Configs.DIR_SDCARD_PROJECT_BOOK, path);
                if (file.exists() && file.isFile()) {
                    file.delete();
                }
            }
            response.end();
        });
        // download
        server.get("/files/.*", (AsyncHttpServerRequest request, AsyncHttpServerResponse response) -> {
            LogUtils.d(TAG, "startServer() called | download");
            String path = request.getPath().replace("/files/", "");
            try {
                path = URLDecoder.decode(path, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            File file = new File(Configs.DIR_SDCARD_PROJECT_BOOK, path);
            if (file.exists() && file.isFile()) {
                try {
                    response.getHeaders().add("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                response.sendFile(file);
                return;
            }
            response.code(404).send("Not found!");
        });
        // progress
        server.get("/progress/.*", (final AsyncHttpServerRequest request, final AsyncHttpServerResponse response) -> {
                    LogUtils.d(TAG, "startServer() called | progress");
                    JSONObject res = new JSONObject();

                    String path = request.getPath().replace("/progress/", "");

                    if (path.equals(fileUploadHolder.fileName)) {
                        try {
                            res.put("fileName", fileUploadHolder.fileName);
                            res.put("size", fileUploadHolder.totalSize);
                            res.put("progress", fileUploadHolder.fileOutPutStream == null ? 1 : 0.1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    response.send(res);
                }

        );
        server.listen(mAsyncServer, Configs.HTTP_PORT);
    }

    /**
     * sendResources
     */
    private void sendResources(final AsyncHttpServerRequest request, final AsyncHttpServerResponse response) {
        LogUtils.d(TAG, "sendResources() called with: request = [" + request + "], response = [" + response + "]");
        try {
            String fullPath = request.getPath();
            fullPath = fullPath.replace("%20", " ");
            String resourceName = fullPath;
            if (resourceName.startsWith("/")) {
                resourceName = resourceName.substring(1);
            }
            if (resourceName.indexOf("?") > 0) {
                resourceName = resourceName.substring(0, resourceName.indexOf("?"));
            }
            if (!TextUtils.isEmpty(getContentTypeByResourceName(resourceName))) {
                response.setContentType(getContentTypeByResourceName(resourceName));
            }
            BufferedInputStream bInputStream = new BufferedInputStream(getAssets().open("wifi/" + resourceName));
            response.sendStream(bInputStream, bInputStream.available());
        } catch (IOException e) {
            e.printStackTrace();
            response.code(404).end();
            return;
        }
    }

    /**
     * getContentTypeByResourceName
     */
    private String getContentTypeByResourceName(String resourceName) {
        LogUtils.d(TAG, "getContentTypeByResourceName() called with: resourceName = [" + resourceName + "]");
        if (resourceName.endsWith(".css")) {
            return CSS_CONTENT_TYPE;
        } else if (resourceName.endsWith(".js")) {
            return JS_CONTENT_TYPE;
        } else if (resourceName.endsWith(".swf")) {
            return SWF_CONTENT_TYPE;
        } else if (resourceName.endsWith(".png")) {
            return PNG_CONTENT_TYPE;
        } else if (resourceName.endsWith(".jpg") || resourceName.endsWith(".jpeg")) {
            return JPG_CONTENT_TYPE;
        } else if (resourceName.endsWith(".woff")) {
            return WOFF_CONTENT_TYPE;
        } else if (resourceName.endsWith(".ttf")) {
            return TTF_CONTENT_TYPE;
        } else if (resourceName.endsWith(".svg")) {
            return SVG_CONTENT_TYPE;
        } else if (resourceName.endsWith(".eot")) {
            return EOT_CONTENT_TYPE;
        } else if (resourceName.endsWith(".mp3")) {
            return MP3_CONTENT_TYPE;
        } else if (resourceName.endsWith(".mp4")) {
            return MP4_CONTENT_TYPE;
        }
        return "";
    }

    /**
     * getIndexContent
     */
    private String getIndexContent() throws IOException {
        LogUtils.d(TAG, "getIndexContent() called");
        BufferedInputStream bInputStream = null;
        try {
            bInputStream = new BufferedInputStream(getAssets().open("wifi/index.html"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = 0;
            byte[] tmp = new byte[10240];
            while ((len = bInputStream.read(tmp)) > 0) {
                baos.write(tmp, 0, len);
            }
            return new String(baos.toByteArray(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (bInputStream != null) {
                try {
                    bInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * FileUploadHolder
     */
    public class FileUploadHolder {
        private String fileName;
        private File receivedFile;
        private BufferedOutputStream fileOutPutStream;
        private long totalSize;

        public BufferedOutputStream getFileOutPutStream() {
            LogUtils.d(TAG, "FileUploadHolder | getFileOutPutStream() called");
            return fileOutPutStream;
        }

        public void setFileName(String fileName) {
            LogUtils.d(TAG, "FileUploadHolder | setFileName() called with: fileName = [" + fileName + "]");
            this.fileName = fileName;
            totalSize = 0;
            File file = new File(Configs.DIR_SDCARD_PROJECT_BOOK);
            if (!file.exists()) {
                file.mkdirs();
            }
            this.receivedFile = new File(file, this.fileName);
            Timber.d(receivedFile.getAbsolutePath());
            try {
                fileOutPutStream = new BufferedOutputStream(new FileOutputStream(receivedFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void write(byte[] data) {
            LogUtils.d(TAG, "FileUploadHolder | write() called with: data = [" + data + "]");
            if (fileOutPutStream != null) {
                try {
                    fileOutPutStream.write(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            totalSize += data.length;
        }

        public void reset() {
            LogUtils.d(TAG, "FileUploadHolder | reset() called");
            if (fileOutPutStream != null) {
                try {
                    fileOutPutStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            fileOutPutStream = null;
        }

        public File getReceivedFile() {
            return receivedFile;
        }
    }
}
