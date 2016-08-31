package com.liompei.qntv;

import android.app.Application;

import com.qiniu.pili.droid.streaming.StreamingEnv;

/**
 * Created by Liompei on 2016/8/30.
 */
public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        StreamingEnv.init(getApplicationContext());  //七牛直播
    }
}
