package com.example.leo.parkingapp.utils;

import android.util.Log;

/**
 * 项目名：    TestApp
 * 包名： com.example.leo.testapp.utils
 * 文件名：    L
 * 创建者：    leo
 * 创建时间：   2018/2/24 17:47
 * 描述： Log封装类
 */
public class L {

    //开关
    public static final boolean DEBUG = true;
    //TAG
    public static final String TAG = "GoodParking";

    //五个等级 D I W E F
    public static void d(String text){
        if(DEBUG){
            Log.d(TAG, text);
        }
    }
    public static void i(String text){
        if(DEBUG){
            Log.i(TAG, text);
        }
    }
    public static void w(String text){
        if(DEBUG){
            Log.w(TAG, text);
        }
    }
    public static void e(String text){
        if(DEBUG){
            Log.e(TAG, text);
        }
    }
    public static void f(String text){
        if(DEBUG){
            Log.wtf(TAG, text);
        }
    }
}
