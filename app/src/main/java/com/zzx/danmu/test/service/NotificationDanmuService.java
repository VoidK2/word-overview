package com.zzx.danmu.test.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.zzx.danmu.test.R;
import com.zzx.danmu.test.danmu.DanmuControl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import master.flame.danmaku.controller.IDanmakuView;

import static android.content.ContentValues.TAG;

public class NotificationDanmuService extends Service {

    private IDanmakuView mDanmakuView;
    private DanmuControl danmuControl;

    private WindowManager wm;
    private WindowManager.LayoutParams wmParams;
    private View view;
    private String jsonDataString;
    private List<JSONObject> jsonWord;

    public NotificationDanmuService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("NotificationDanmu", "onCreate");
        jsonWord = new ArrayList<>();
        JsonDataToString();
        //检查是否已经授予权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                //若未授权则请求权限
                createView();
                handler.postDelayed(task, 1000);
            }
        } else {
            createView();
            handler.postDelayed(task, 1000);
        }

    }

    private void createView() {
        view = LayoutInflater.from(this).inflate(R.layout.view_danmaku, null);
        mDanmakuView = view.findViewById(R.id.containerView);
        danmuControl = new DanmuControl(this, mDanmakuView);

        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);


        wmParams = new WindowManager.LayoutParams();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;//2002
        }

        wmParams.flags |= 8;
        /*
        * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
        */
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        wmParams.gravity = Gravity.LEFT | Gravity.TOP; // 调整悬浮窗口至左上角
        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 20;
        wmParams.y = 20;
        // 设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.format = 1;

        wm.addView(view, wmParams);

    }

    private Handler handler = new Handler();
    private Runnable task = new Runnable() {
        public void run() {
            dataRefresh();
            handler.postDelayed(this, 3000);
            wm.updateViewLayout(view, wmParams);
        }
    };

    public void dataRefresh() {
//        String avator = "http://g.hiphotos.baidu.com/image/h%3D200/sign=9b2f9371992397ddc9799f046983b216/dc54564e9258d1094dc90324d958ccbf6c814d7a.jpg";
        String avator = "https://cdn.longdoer.com/2020/11/18/madalorianc903c4603b3941b6.jpg";
        Random r =new Random();
        List<String> l1=getData(r.nextInt(4534));
        String name = l1.get(0);
        String content = l1.get(1);
        danmuControl.addDanmu(avator, name, content);
    }

    public List<String> getData(int con){
        List<String> l1=new ArrayList<>();
        try {
            JSONObject jsontmp=jsonWord.get(con);
            String temp=jsontmp.getString("headWord");
            l1.add(temp);
            JSONArray j2=jsontmp.getJSONObject("content").getJSONObject("word").getJSONObject("content").getJSONArray("trans");
            StringBuilder s2= new StringBuilder();
            for (int i = 0; i < j2.length(); i++) {
                String tmp = j2.get(i).toString();
                JSONObject j5=new JSONObject(tmp);
                String s1=j5.getString("tranCn");
                if(i!=0){s2.append(";");}
                s2.append(s1);

            }
            String s3=s2.toString();
            l1.add(s3);
//            Toast.makeText(NotificationDanmuService.this, temp+":"+s3, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return l1;

    }
    public void JsonDataToString() {
        Log.i(TAG,"读取 json  文件 转化 String  ");
        InputStream inputStream = getResources().openRawResource(R.raw.kaoyan2);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line="";
        try {
            while ((line = reader.readLine()) != null) {
//                JSONObject json1=new JSONObject(line);
                JSONObject jsonObject =   new JSONObject(line);
                Log.i(TAG,jsonObject.toString());
                jsonWord.add(jsonObject);
            }
//            inputStreamReader.close();
//            bufferedReader.close();
//            jsonData = stringBuilder.toString();
//            Log.i("TAG", stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d("NotificationDanmu", "onStart");
//		setForeground(true);
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (danmuControl != null) {
            danmuControl.stop();
        }
        if (mDanmakuView != null) {
            mDanmakuView.release();
            mDanmakuView = null;
        }
        handler.removeCallbacks(task);
        Log.d("NotificationDanmu", "onDestroy");
        if (wm != null) {
            wm.removeView(view);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
