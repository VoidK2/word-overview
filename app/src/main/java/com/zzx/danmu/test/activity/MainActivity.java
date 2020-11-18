package com.zzx.danmu.test.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.zzx.danmu.test.R;
import com.zzx.danmu.test.service.NotificationDanmuService;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                //若未授权则请求权限
                getOverlayPermission();//getOverlayPermission
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (Activity.RESULT_OK == resultCode){
                Toast.makeText(this,"悬浮窗授权通过",Toast.LENGTH_LONG).show();
            }
        }
    }

    //请求悬浮窗权限
    @TargetApi(Build.VERSION_CODES.M)
    private void getOverlayPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 1);
    }
//
//    public void videoDanmu(View view) {
//        Intent intent = new Intent(this, ViewDanmuActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putInt("contentType", 0);
//        intent.putExtras(bundle);
//        startActivity(intent);
//    }
//
//
//    public void videoDanmu2(View view) {
//        Intent intent = new Intent(this, ViewDanmuActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putInt("contentType", 1);
//        intent.putExtras(bundle);
//        startActivity(intent);
//    }

    public void notificationDanmu(View view) {
        startActivity(new Intent(this, GlobalNotificationDanmuActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop(NotificationDanmuService.class);
        stop(NotificationDanmuService.class);
    }

    private void stop(Class<?> clz) {
        Intent serviceStop = new Intent();
        serviceStop.setClass(this, clz);
        stopService(serviceStop);
    }

    public void test(View view) {
        Intent intent = new Intent();
        String key="bYz-XxzevU4DMOX3qlmyHT7sAtHWsh5w";
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
        }
    }

}
