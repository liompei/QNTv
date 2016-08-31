package com.liompei.qntv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.liompei.qntv.activity.SWCameraStreamingActivity;
import com.liompei.qntv.base.BaseActivity;
import com.liompei.zlog.Z;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends BaseActivity {

    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();

    }


    //网络请求获取stream Json
    private String requestStreamJson() {
        try {
            // Replace "Your app server" by your app sever url which can get the StreamJson as the SDK's input.
            HttpURLConnection httpConn = (HttpURLConnection) new URL("Your app server").openConnection();
            httpConn.setConnectTimeout(5000);
            httpConn.setReadTimeout(10000);
            int responseCode = httpConn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            int length = httpConn.getContentLength();
            if (length <= 0) {
                return null;
            }
            InputStream is = httpConn.getInputStream();
            byte[] data = new byte[length];
            int read = is.read(data);
            is.close();
            if (read <= 0) {
                return null;
            }
            return new String(data, 0, read);
        } catch (Exception e) {
            Z.show("Network error!");
        }
        return null;
    }


    @Override
    protected void initView() {
        mButton = (Button) findViewById(R.id.btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Get the stream json from http

                            String streamJson = requestStreamJson();
                            Intent intent = new Intent(MainActivity.this, SWCameraStreamingActivity.class);
                            intent.putExtra("stream_json_str", streamJson);
                            startActivity(intent);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }

    @Override
    protected void initData() {

    }
}
