package com.example.leo.parkingapp.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.leo.parkingapp.server.MyServer;
import com.example.leo.parkingapp.utils.L;

import java.io.IOException;

/**
 * 项目名：    ParkingApp
 * 包名： com.example.leo.parkingapp.service
 * 文件名：    MyService
 * 创建者：    leo
 * 创建时间：   2018/3/5 19:46
 * 描述： TODO
 */
public class MyService extends Service {

    @Override
    //Service时被调用
    public void onCreate()
    {
        L.i( "Service onCreate--->");
        super.onCreate();
    }

    @Override
    //当调用者使用startService()方法启动Service时，该方法被调用
    public void onStart(Intent intent, int startId)
    {
        L.i("Service onStart--->");
        super.onStart(intent, startId);
    }

    @Override
    //当Service不在使用时调用
    public void onDestroy()
    {
        L.i( "Service onDestroy--->");
        super.onDestroy();
    }

    @Nullable
    @Override
    //当使用startService()方法启动Serice时，方法体内只需写return null
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyServer myServer = new MyServer(8084);
        try {
            Toast.makeText(getApplicationContext(), getLocalIpStr(this)+"！", Toast.LENGTH_SHORT).show();

            myServer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }


    public static String getLocalIpStr(Context context) {
        WifiManager wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return intToIpAddr(wifiInfo.getIpAddress());
    }

    private static String intToIpAddr(int ip) {
        return (ip & 0xff) + "." + ((ip>>8)&0xff) + "." + ((ip>>16)&0xff) + "." + ((ip>>24)&0xff);
    }

}
