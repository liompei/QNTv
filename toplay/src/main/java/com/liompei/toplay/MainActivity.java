package com.liompei.toplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;

import com.liompei.toplay.activity.PLVideoTextureViewActivity;
import com.liompei.toplay.activity.PLVideoViewActivity;
import com.liompei.toplay.base.BaseActivity;
import com.liompei.zlog.Z;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    private Spinner mSpinner;  //用于选择渲染使用的activity
    private RadioGroup mGroup1, mGroup2;  //选择播放类型和解码方式
    private TextInputEditText mEditText;  //直播流地址
    private Switch mSwitch;  //是否开启DNS缓存管理服务
    private Button btnStart;  //启动

    private static final String[] DEFAULT_PLAYBACK_DOMAIN_ARRAY = {  //缓存地址
            "live.hkstv.hk.lxdns.com"
    };
    public static final String[] ACTIVITY = {  //填充Spinner
            "PLVideoViewActivity",
            "PLVideoTextureViewActivity",
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    @Override
    protected void initView() {
        mSpinner = (Spinner) findViewById(R.id.mSpinner);
        mGroup1 = (RadioGroup) findViewById(R.id.group1);
        mGroup2 = (RadioGroup) findViewById(R.id.group2);
        mEditText = (TextInputEditText) findViewById(R.id.mdEditText);
        mSwitch = (Switch) findViewById(R.id.mSwitch);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);

        mSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ACTIVITY));

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                String path = mEditText.getText().toString().trim();
                if (path.isEmpty()) {
                    Z.show("请输入正确地址");
                } else {
                    toStartActivity(path);
                }
                break;
        }
    }


    private void toStartActivity(String path) {
        Class mClass = null;

        switch (mSpinner.getSelectedItemPosition()) {
            case 0:
                mClass = PLVideoViewActivity.class;
                break;
            case 1:
                mClass = PLVideoTextureViewActivity.class;
                break;
            default:
                Z.show("不存在此行");
                break;
        }

        Intent intent = new Intent();
        intent.setClass(MainActivity.this, mClass);
        intent.putExtra("path", path);
        //判断是直播还是点播
        if (mGroup1.getCheckedRadioButtonId() == R.id.zhibo) {
            intent.putExtra("live", 1);
        } else {
            intent.putExtra("live", 0);
        }
        //判断是硬解还是软解
        if (mGroup2.getCheckedRadioButtonId() == R.id.yingjie) {
            intent.putExtra("code", 1);
        } else {
            intent.putExtra("code", 0);
        }
        startActivity(intent);

    }


}
