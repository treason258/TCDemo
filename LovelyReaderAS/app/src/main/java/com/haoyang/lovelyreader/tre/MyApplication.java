package com.haoyang.lovelyreader.tre;

import com.haoyang.reader.page.ReaderApplication;
import com.mjiayou.trecorelib.base.TCApp;

/**
 * Created by xin on 18/9/22.
 */
public class MyApplication extends ReaderApplication {

  @Override public void onCreate() {
    super.onCreate();
    TCApp.onCreateManual(this);
  }
}
