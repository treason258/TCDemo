package com.mjiayou.trecore.helper;

import android.content.Context;
import android.content.Intent;

import com.mjiayou.trecore.ui.demo.TestMainActivity;
import com.mjiayou.trecore.ui.demo.TestSplashActivity;
import com.mjiayou.trecore.ui.demo.TestUserLoginActivity;
import com.mjiayou.trecore.ui.demo.TestUserRegisterActivity;
import com.mjiayou.trecore.test.TestVideoActivity;
import com.mjiayou.trecore.test.TestWeiboActivity;
import com.mjiayou.trecore.util.ClickUtil;

/**
 * Created by treason on 16/5/14.
 */
public class Router {

    protected static final String TAG = "Router";

    protected static Intent mIntent;

    /**
     * openTestWeiboActivity
     */
    public static void openTestWeiboActivity(Context context) {
        if (ClickUtil.isFastClick()) {
            return;
        }

        mIntent = new Intent(context, TestWeiboActivity.class);
        context.startActivity(mIntent);
    }

    /**
     * openTestSplashActivity
     */
    public static void openTestSplashActivity(Context context) {
        if (ClickUtil.isFastClick()) {
            return;
        }

        mIntent = new Intent(context, TestSplashActivity.class);
        context.startActivity(mIntent);
    }

    /**
     * openTestMainActivity
     */
    public static void openTestMainActivity(Context context) {
        if (ClickUtil.isFastClick()) {
            return;
        }

        mIntent = new Intent(context, TestMainActivity.class);
        context.startActivity(mIntent);
    }

    /**
     * openTestUserLoginActivity
     */
    public static void openTestUserLoginActivity(Context context) {
        if (ClickUtil.isFastClick()) {
            return;
        }

        mIntent = new Intent(context, TestUserLoginActivity.class);
        context.startActivity(mIntent);
    }

    /**
     * openTestUserRegisterActivity
     */
    public static void openTestUserRegisterActivity(Context context) {
        if (ClickUtil.isFastClick()) {
            return;
        }

        mIntent = new Intent(context, TestUserRegisterActivity.class);
        context.startActivity(mIntent);
    }

    /**
     * openTestVideoActivity
     */
    public static void openTestVideoActivity(Context context) {
        if (ClickUtil.isFastClick()) {
            return;
        }

        mIntent = new Intent(context, TestVideoActivity.class);
        context.startActivity(mIntent);
    }

    // ******************************** project ********************************
}
