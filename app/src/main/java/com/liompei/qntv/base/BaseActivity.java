package com.liompei.qntv.base;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Liompei on 2016/8/30.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected abstract void initView();

    protected abstract void initData();

}
