package com.example.leo.parkingapp.application;

import android.app.Application;
import android.os.StrictMode;

import com.baidu.mapapi.SDKInitializer;
import com.example.leo.parkingapp.utils.StaticClass;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by leo on 2018/2/24.
 * 描述： Application
 */
public class BaseApplication extends Application {

    //创建
    @Override
    public void onCreate(){
        super.onCreate();

        //初始化Bugly
//        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APP_ID, true);

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"="+ StaticClass.VOICE_KEY);
    }

    //设置登陆后的对象
    SoapObject logUser = null;
    public SoapObject getLogUser() {
        return logUser;
    }
    public void setLogUser(SoapObject logUser) {
        this.logUser = logUser;
    }
}
