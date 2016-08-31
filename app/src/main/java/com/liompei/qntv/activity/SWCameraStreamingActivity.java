package com.liompei.qntv.activity;

import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.liompei.qntv.R;
import com.liompei.qntv.base.BaseActivity;
import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.CameraStreamingManager;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.MediaStreamingManager;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.StreamingState;
import com.qiniu.pili.droid.streaming.StreamingStateChangedListener;
import com.qiniu.pili.droid.streaming.widget.AspectFrameLayout;

import org.json.JSONObject;

public class SWCameraStreamingActivity extends BaseActivity implements StreamingStateChangedListener {


    private AspectFrameLayout mAspectFrameLayout;
    private GLSurfaceView mGLSurfaceView;

    private String streamJsonFormServer;
    private JSONObject mJSONObject;
    private CameraStreamingManager mCameraStreamingManager;  //老的监听方法
    private StreamingProfile mProfile;
    private MediaStreamingManager mMediaStreamingManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swcamera_streaming);
        initView();
        initData();
    }

    @Override
    protected void initView() {
        mAspectFrameLayout = (AspectFrameLayout) findViewById(R.id.aspectFrameLayout);
        mGLSurfaceView = (GLSurfaceView) findViewById(R.id.glSurfaceView);

        streamJsonFormServer = getIntent().getStringExtra("stream_json_str");

        mAspectFrameLayout.setShowMode(AspectFrameLayout.SHOW_MODE.REAL);

    }

    @Override
    protected void initData() {


        try {

            mJSONObject = new JSONObject(streamJsonFormServer);
            StreamingProfile.Stream stream = new StreamingProfile.Stream(mJSONObject);
            mProfile.setVideoQuality(StreamingProfile.AUDIO_QUALITY_HIGH2)  //视频清晰度
                    .setAudioQuality(StreamingProfile.AUDIO_QUALITY_HIGH2)  //音频清晰度
                    .setEncodingSizeLevel(StreamingProfile.VIDEO_ENCODING_HEIGHT_480)  //分辨率
                    .setEncoderRCMode(StreamingProfile.EncoderRCModes.QUALITY_PRIORITY)  //质量优先
                    .setStream(stream);  //设置数据源

            CameraStreamingSetting setting = new CameraStreamingSetting();
            setting.setCameraId(Camera.CameraInfo.CAMERA_FACING_BACK)  //hardware
                    .setContinuousFocusModeEnabled(true)  //可点击?
                    .setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.MEDIUM)  //水平尺寸
                    .setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9);  //比例

            mMediaStreamingManager = new MediaStreamingManager(this, mAspectFrameLayout, mGLSurfaceView, AVCodecType.HW_AUDIO_CODEC);
            mMediaStreamingManager.prepare(setting, mProfile);
            mMediaStreamingManager.setStreamingStateListener(this);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaStreamingManager.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // You must invoke pause here.
        mMediaStreamingManager.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaStreamingManager.destroy();
    }

    @Override
    public void onStateChanged(StreamingState streamingState, Object o) {
        switch (streamingState) {
            case PREPARING:  //准备中
                break;
            case READY:  //准备
                // start streaming when READY
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (mMediaStreamingManager!=null){
                            mMediaStreamingManager.startStreaming();  //开始播放
                        }
                    }
                }).start();
                break;
            case CONNECTING:  //连接中
                break;
            case STREAMING:  //该AV数据包被发送
                // The av packet had been sent.
                break;
            case SHUTDOWN:  //结束
                // The streaming had been finished.
                break;
            case IOERROR:  //错误
                break;
            case SENDING_BUFFER_EMPTY:  //发送缓冲区空
                break;
            case SENDING_BUFFER_FULL:  //发送缓冲区满
                break;
            case AUDIO_RECORDING_FAIL:  //无法录制音频
                // Failed to record audio.
                break;
            case OPEN_CAMERA_FAIL:  //不能打开camera
                // Failed to open camera.
                break;
            case DISCONNECTED:  //流媒体反了
                // The socket is broken while streaming
                break;
        }

    }
}
