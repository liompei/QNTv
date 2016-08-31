package com.liompei.toplay;

import android.app.Application;

import com.liompei.zlog.Z;

/**
 * Created by Liompei on 2016/8/30
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Z.initToast(this,true);
    }
}
