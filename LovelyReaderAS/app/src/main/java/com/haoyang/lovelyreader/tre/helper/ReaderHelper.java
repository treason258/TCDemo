package com.haoyang.lovelyreader.tre.helper;

import android.app.Activity;
import android.graphics.Typeface;

import com.app.base.common.util.Size;
import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.bean.UserBean;
import com.haoyang.reader.sdk.Book;
import com.haoyang.reader.sdk.LineSpace;
import com.haoyang.reader.sdk.ReaderPageStyle;
import com.haoyang.reader.sdk.ReaderPageStyle.BackgroundMode;
import com.haoyang.reader.sdk.ReaderPageStyle.NightDayMode;
import com.haoyang.reader.sdk.ReaderSDK;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xin on 18/11/7.
 */

public class ReaderHelper {

    // TAG
    protected final static String TAG = "ReaderHelper";

//    /**
//     * 启动阅读器进行阅读
//     */
//    public static void startReader(Activity activity, Book book, UserBean userBean) {
//        try {
//            // parameter
//            SDKParameterInfo parameter = new SDKParameterInfo();
//            parameter.appId = "773278";
//            parameter.appKey = "10f2a8b3759b4304a5414269c5c4bf63";
//            parameter.userId = userBean.getUid(); // 接入时需提供应用下唯一的，否则会出现数据错乱。
//            parameter.userName = userBean.getNickName();
//
//            ReaderSDK readerSDK = ReaderSDK.getInstance();
//            readerSDK.initSDK(activity, parameter);
//            readerSDK.setCurrentPageStyle("wallpapers/background_4.jpg"); // 设置默认的样式，这个方法需要在setPageStyle方法之前执行
//            readerSDK.setPageStyle(getPageStyleList()); // 将样式对象列表传给阅读器，阅读器根据这个来定义背景切换，如果在之前没有执行setCurrentPageStyle这个方法，setPageStyle就会把第一个做为默认样式
//            readerSDK.setAnimationType(AnimationType.SlideAnimation); // 设置默认翻页动画。
//            readerSDK.setAnimationSpeed(10); // 设置翻页动画移动时，每次移动的距离基础数字。
//            readerSDK.setAnimationSpeedFactor(1.2f); // 设置翻页动画速度不断加快的因子。
//            readerSDK.setTypefaceSwitchEnable(true); // 是否开启字体切换功能。
//            readerSDK.setDefaultTypeface(Typeface.createFromAsset(activity.getAssets(), "yahei.ttf")); // 设置当前的默认字体。
//
//            // 设置阅读的各种属性
//            readerSDK.setUpDownSpace(100); // 设置阅读器上下屏幕边与文字之间的间距。
//            readerSDK.setLeftRightSpace(50); // 设置阅读器左右屏幕边与文字之间的间距。
//            readerSDK.setLineSpacing(LineSpace.lineSpaceMiddle); // 设置当前行间距。
//            readerSDK.setStressLineThickness(3); // 设置对文字画线时线条的粗细程度。
//            readerSDK.setStressLineColor(new ColorService(230, 45, 150)); // 设置对文字画线时线条的颜色。
//            readerSDK.setShowCatalog(); // 设置阅读器显示目录。setHideCatalog
//            readerSDK.setShowBattery(true); // 设置阅读器显示电池。
//            readerSDK.setShowPageNumber(true); // 设置阅读器显示页码。
//            readerSDK.setShowTime(true); // 设置阅读器显示当前时间。
//            readerSDK.setShareEnable(false); // 是否需要分享功能。
//            readerSDK.setTypefaceSwitchEnable(false); // 是否需要有切换字体功能。
//
//            // 设置阅读器内部一些功能由开发者来实现的接口，这里需要开发者实现SDKInteractive接口。
////            readerSDK.setSDKInteractive(new SDKInteractiveImpl(activity));
////            readerSDK.setCustomServivce(new CustomServiceImpl());
//
//            String id = book.path;
//            if (book.path != null) {
//                int index = book.path.lastIndexOf(File.separator);
//                if (index != -1) {
//                    id = book.path.substring(index + 1);
//                }
//            }
//            book.id = id;
//
//            readerSDK.startReader(activity, book, ReaderSDK.FilePathType.Local_File);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 图片处理方式，开发者自定义。
//     *
//     * @author tianyu912@yeah.net Mar 10, 2018
//     */
//    private static class CustomServiceImpl extends DefaultCustomServivce {
//        @Override
//        public Size getImageSize(float imageWidth, float imageHeight, float readAdeaWidth, float readAreaHeight) {
//            float height, width;
//            float unit = readAdeaWidth / 4.0f;
//            if (imageWidth <= unit) {
//                width = unit;
//                float rate = width / imageWidth;
//                height = rate * imageHeight;
//            } else {
//                width = readAdeaWidth;
//                float rate = width / imageWidth;
//                height = rate * imageHeight;
//            }
//
//            if (height > readAreaHeight) {
//                float rate = readAreaHeight / height;
//                height = (int) (rate * height);
//                width = width * rate;
//            }
//            return new Size((int) width, (int) height);
//        }
//    }
//
////    /**
////     * SDKInteractiveImpl
////     */
////    private static class SDKInteractiveImpl implements ReaderSDK.SDKInteractive {
////
////        private Context mContext;
////
////        public SDKInteractiveImpl(Context context) {
////            mContext = context;
////        }
////
////        @Override
////        public void share(ShareEntity shareEntity) { // 打开分享界面
////            ToastUtils.show("分享");
////        }
////
////        @Override
////        public void typefaceSwitch() { // 打开切换字体界面
////            Log.d(TAG, "typefaceSwitch() called");
////            ToastUtils.show("切换字体");
////        }
////
////        @Override
////        public void toolsBar(RelativeLayout relativeLayout) {
////            View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_reader_custom, null);
////            relativeLayout.addView(contentView);
////
////            // findViewById
////            View tvComment = contentView.findViewById(R.id.tvComment);
////            View tvBuy = contentView.findViewById(R.id.tvBuy);
////
////            // tvComment
////            tvComment.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    ToastUtils.show("评论");
////                }
////            });
////            // tvBuy
////            tvBuy.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    ToastUtils.show("购买");
////                }
////            });
////        }
////
////        @Override
////        public void feedback(FeedBack feedBack) {
////            LogUtils.d(TAG, "feedback() called with: feedBack = [" + JsonParser.get().toJson(feedBack) + "]");
////            ToastUtils.show("意见反馈");
////        }
////
////        @Override
////        public void decrypt(String sourceFilePath, String targetFilePath) {
////            LogUtils.d(TAG, "decrypt() called with: sourceFilePath = [" + sourceFilePath + "], targetFilePath = [" + targetFilePath + "]");
////            ToastUtils.show("decrypt");
////        }
////    }
//
//    /**
//     * getPageStyleList
//     */
//    private static List<PageStyle> getPageStyleList() {
//        String[] wordColorArray = {
//                "0x372d21",
//                "0xb6bbbe",
//                "0x322b23",
//                "0x444444",
//                "0x444444",
//                "0x444444",
//                "0x444444",
//                "0x342d25",
//                "0x293a27",
//                "0x000200",
//                "0x5f707a",
//                "0x96938e"}; // 阅读器文字颜色，与背景资源是一一对应的。
//
//        String[] wallPaperArray = {
//                "wallpapers/background_2.jpg",
//                "wallpapers/background_1.jpg",
//                "wallpapers/background_3.jpg",
//                "wallpapers/background_4.jpg",
//                "wallpapers/background_5.jpg",
//                "wallpapers/background_6.jpg",
//                "wallpapers/background_7.jpg",
//                "0xf6efe7",
//                "0xceefd0",
//                "0x596476",
//                "0x001d28",
//                "0x39312f"}; // 阅读器背景资源。
//
//        PageStyle.BackgroundFillMode[] wallPaperType = {
//                PageStyle.BackgroundFillMode.fullscreen,
//                PageStyle.BackgroundFillMode.tileMirror,
//                PageStyle.BackgroundFillMode.fullscreen,
//                PageStyle.BackgroundFillMode.fullscreen,
//                PageStyle.BackgroundFillMode.fullscreen,
//                PageStyle.BackgroundFillMode.fullscreen,
//                PageStyle.BackgroundFillMode.fullscreen,
//                PageStyle.BackgroundFillMode.backgroundColor,
//                PageStyle.BackgroundFillMode.backgroundColor,
//                PageStyle.BackgroundFillMode.backgroundColor,
//                PageStyle.BackgroundFillMode.backgroundColor,
//                PageStyle.BackgroundFillMode.backgroundColor}; // 阅读器背景资源类型。
//
//        PageStyle.NightDayMode[] nightDayMode = {
//                PageStyle.NightDayMode.day,
//                PageStyle.NightDayMode.none,
//                PageStyle.NightDayMode.none,
//                PageStyle.NightDayMode.none,
//                PageStyle.NightDayMode.none,
//                PageStyle.NightDayMode.none,
//                PageStyle.NightDayMode.none,
//                PageStyle.NightDayMode.none,
//                PageStyle.NightDayMode.none,
//                PageStyle.NightDayMode.none,
//                PageStyle.NightDayMode.none,
//                PageStyle.NightDayMode.night}; // 定义每种背景及文字是夜间模式还是白天模式。
//
//        List<PageStyle> pageStyleList = new ArrayList<>();
//        for (int index = 0; index < wallPaperArray.length; index++) { // 生成样式对象。
//            PageStyle pageStyle = new PageStyle();
//            pageStyle.styleName = wallPaperArray[index];
//            pageStyle.backgroundValue = wallPaperArray[index];
//            pageStyle.backgroundFillMode = wallPaperType[index];
//            pageStyle.textColor = wordColorArray[index];
//            pageStyle.catalogColor = wordColorArray[index];
//            pageStyle.pageNumberColor = wordColorArray[index];
//            pageStyle.timeColor = wordColorArray[index];
//            pageStyle.nightDayMode = nightDayMode[index];
//            pageStyle.source = PageStyle.Source.DEVELOPER;
//            pageStyleList.add(pageStyle);
//        }
//        return pageStyleList;
//    }

    public static void startReader2(Activity activity, Book book, UserBean userBean) {

        final ReaderSDK readerSDK = ReaderSDK.getInstance();

        readerSDK.initSDK(book, userBean.getUid(), activity);

        String[] wordColorArray = {"0x372d21",
                "0xb6bbbe",
                "0x322b23",
                "0x444444",
                "0x444444",
                "0x444444",
                "0x444444",
                "0x342d25",
                "0x293a27",
                "0x000200",
                "0x5f707a",
                "0x96938e"}; // 阅读器文字颜色，与背景资源是一一对应的。
        String[] lineColorArray = {"0x472d21", "0xc6bbbe", "0x422b23",
                "0x544444", "0x544444", "0x544444", "0x544444", "0x544444",
                "0x393a27", "0x100200", "0x6f707a", "0xa6938e"}; // 阅读器文字颜色，与背景资源是一一对应的。

        String[] wallPaperArray = {"wallpapers/background_2.jpg",
                "wallpapers/background_1.jpg", "wallpapers/background_3.jpg",
                "wallpapers/background_4.jpg", "wallpapers/background_5.jpg",
                "wallpapers/background_6.jpg", "wallpapers/background_7.jpg",
                "0xf6efe7", "0xceefd0", "0x596476", "0x001d28", "0x39312f"}; // 阅读器背景资源。

        ReaderPageStyle.BackgroundMode[] wallPaperType = {BackgroundMode.fullscreen,
                BackgroundMode.tile,
                BackgroundMode.fullscreen,
                BackgroundMode.fullscreen,
                BackgroundMode.fullscreen,
                BackgroundMode.fullscreen,
                BackgroundMode.fullscreen,
                BackgroundMode.backgroundColor,
                BackgroundMode.backgroundColor,
                BackgroundMode.backgroundColor,
                BackgroundMode.backgroundColor,
                BackgroundMode.backgroundColor}; // 阅读器背景资源类型。

        ReaderPageStyle.NightDayMode[] nightDayMode = {NightDayMode.day, NightDayMode.none,
                NightDayMode.none, NightDayMode.none, NightDayMode.none,
                NightDayMode.none, NightDayMode.none, NightDayMode.none,
                NightDayMode.none, NightDayMode.none, NightDayMode.none,
                NightDayMode.night}; // 定义每种背景及文字是夜间模式还是白天模式。

        List<ReaderPageStyle> pageStyleList = new ArrayList<ReaderPageStyle>();

        for (int index = 0; index < wallPaperArray.length; index++) { // 生成样式对象。

            String background = wallPaperArray[index];

            ReaderPageStyle pageStyle = new ReaderPageStyle();
            pageStyle.styleName = background;
            pageStyle.backgroundValue = background;
            pageStyle.backgroundMode = wallPaperType[index];
            pageStyle.textColor = wordColorArray[index];
            pageStyle.catalogColor = wordColorArray[index];
            pageStyle.pageNumberColor = wordColorArray[index];
            pageStyle.timeColor = wordColorArray[index];

            pageStyle.stressColor = "0x00ffff";

            pageStyle.stressLineColor = lineColorArray[index];
            pageStyle.stressLineThickness = 3;                // 设置对文字画线时线条的粗细程度。
            pageStyle.hyperlinkTextColor = "0xff0000";        // 设置对文字画线时线条的颜色。

            // 设置界面上的各种标志
            pageStyle.selectTextAboveIconId = R.drawable.select_word_above;
            pageStyle.selectTextBelowIconId = R.drawable.select_word_below;
            pageStyle.bookmarkIconId = R.drawable.page_bookmark;
            pageStyle.noteLabelIconId = R.drawable.note_label;

            pageStyle.nightDayMode = nightDayMode[index];

            pageStyleList.add(pageStyle);
        } // end for index

        // 设置默认的样式，这个方法需要在setPageStyle方法之前执行
        readerSDK.setCurrentPageStyle("wallpapers/background_3.jpg");

        // 将样式对象列表传给阅读器，阅读器根据这个来定义背景切换。
        // 如果在之前没有执行setCurrentPageStyle这个方法，setPageStyle就会把第一个做为默认样式
        readerSDK.setPageStyle(pageStyleList);

        // ---------------------------------------------------------

        // 设置阅读的各种属性.
        readerSDK.setUpDownSpace(100); // 设置阅读器上下屏幕边与文字之间的间距。
        readerSDK.setLeftRightSpace(50); // 设置阅读器左右屏幕边与文字之间的间距。

        readerSDK.setDefaultTextSize(30);
        readerSDK.setDefaultLineSpace(LineSpace.lineSpaceMiddle.getValue()); // 设置当前行间距。

        readerSDK.setShowTime(true);
        readerSDK.setShowPageNumber(true);
        readerSDK.setShowBattery(true);

        readerSDK.setReaderEventClassPath("com.haoyang.reader.ReaderEventDeveloper");

        try {
            readerSDK.startReader(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
