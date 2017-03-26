package com.wangng.zhihureader;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by 小爱 on 2017/3/12.
 */

public class App extends Application {

    private static App mApp;
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        Stetho.initializeWithDefaults(this);
    }

    public static App getApp() {
        return mApp;
    }
}
