package com.zzx.danmu.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.zzx.danmu.test.R;
import com.zzx.danmu.test.service.NotificationDanmuService;


public class GlobalNotificationDanmuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_notification_danmu__activity);
        Button start = (Button) findViewById(R.id.btn_start);
        start.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent service = new Intent();
                service.setClass(GlobalNotificationDanmuActivity.this, NotificationDanmuService.class);
                startService(service);
            }
        });

        Button stop = (Button) findViewById(R.id.btn_stop);
        stop.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceStop = new Intent();
                serviceStop.setClass(GlobalNotificationDanmuActivity.this, NotificationDanmuService.class);
                stopService(serviceStop);
            }
        });
    }
}
