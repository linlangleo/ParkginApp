package com.example.leo.parkingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.leo.parkingapp.MainActivity;
import com.example.leo.parkingapp.R;
import com.example.leo.parkingapp.utils.ShareUtils;
import com.example.leo.parkingapp.utils.StaticClass;
import com.example.leo.parkingapp.utils.UtilTools;

/**
 * 项目名：    TestApp
 * 包名： com.example.leo.testapp.ui
 * 文件名：    SplashActivity
 * 创建者：    leo
 * 创建时间：   2018/2/24 19:06
 * 描述： 闪屏页
 */
public class SplashActivity extends AppCompatActivity {

    /**
     * 1.延时2000毫秒
     * 2.判断程序是否是第一次运行
     * 3.自定义字体
     * 5.Activity全屏主题
     * @param savedInstanceState
     * @param persistentState
     */

    private TextView tv_splash;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        intiview();
    }

    //延时用Handler
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case StaticClass.HANDLER_SPLASH:
                    //判断程序是否是第一次运行
                    if(isFirst()){
                        startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    }else{
//                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));//跳登陆注册页
                    }

                    finish();
                    break;
            }
        }
    };

    //判断程序是否是第一次运行
    private boolean isFirst(){
        boolean isFirst = ShareUtils.getBoolean(this, StaticClass.SHARE_IS_FIRST, true);

        if(isFirst){
            //
            ShareUtils.putBoolean(this, StaticClass.SHARE_IS_FIRST, false);
            return true;
        }else{
            //不是第一次运行
            return false;
        }
    }

    //禁止返回键
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    //初始化TextView
    private void intiview(){
        //延时2000ms
        handler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH, 2000);

        tv_splash = (TextView) findViewById(R.id.tv_splash);

        //设置字体
        UtilTools.setFont(this, tv_splash);
    }
}
