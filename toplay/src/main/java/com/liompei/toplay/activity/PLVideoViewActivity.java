package com.liompei.toplay.activity;

import android.os.Bundle;
import android.view.View;

import com.liompei.toplay.R;
import com.liompei.toplay.base.BaseActivity;
import com.liompei.toplay.view.MediaController;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.widget.PLVideoView;

/**
 * Created by Liompei
 * on 2016/8/31.
 */

public class PLVideoViewActivity extends BaseActivity {

    private PLVideoView mPLVideoView;
    private MediaController mMediaController;
    private String mPath;
    private int mLive, mCode;

    private static final String[] DEFAULT_PLAYBACK_DOMAIN_ARRAY = {
            "live.hkstv.hk.lxdns.com"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plvideo_view);
        initView();
        initData();

    }

    @Override
    protected void initView() {
        mPLVideoView = (PLVideoView) findViewById(R.id.plVideoView);

        View loadingView = findViewById(R.id.loading);
        mPLVideoView.setBufferingIndicator(loadingView);

        mPath = getIntent().getStringExtra("path");
        mLive = getIntent().getIntExtra("live", 1);
        mCode = getIntent().getIntExtra("code", 1);


        //选项设置,直播的信息设定
        AVOptions avOptions = new AVOptions();
        // the unit of timeout is ms
        avOptions.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        avOptions.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
        avOptions.setInteger(AVOptions.KEY_LIVE_STREAMING, mLive);  //直播

        if (mLive == 1) {
            avOptions.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
        }
        avOptions.setInteger(AVOptions.KEY_MEDIACODEC, mCode);  //硬解
        // whether start play automatically after prepared, default value is 1
        avOptions.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);
        mPLVideoView.setAVOptions(avOptions);
        //设置路径
        mPLVideoView.setVideoPath("rtmp://live.hkstv.hk.lxdns.com/live/hks");
        //绑定控制器
        mMediaController = new MediaController(this, false, true);
        mPLVideoView.setMediaController(mMediaController);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPLVideoView.start();  //开启
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPLVideoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPLVideoView.stopPlayback();
    }

    @Override
    protected void initData() {

    }
}
