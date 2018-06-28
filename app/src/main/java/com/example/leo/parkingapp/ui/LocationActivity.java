package com.example.leo.parkingapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.example.leo.parkingapp.fragment.NearbyFragment;
import com.example.leo.parkingapp.utils.L;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 项目名：    TestApp
 * 包名： com.example.leo.testapp.ui
 * 文件名：    LocationActivity
 * 创建者：    leo
 * 创建时间：   2018/2/26 19:42
 * 描述： 我的位置
 */
public class LocationActivity extends BaseActivity {

    //导航
    //一些配置
    public static List<Activity> activityList = new LinkedList<Activity>();

    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public static final String SHOW_CUSTOM_ITEM = "showCustomItem";
    public static final String RESET_END_NODE = "resetEndNode";
    public static final String VOID_MODE = "voidMode";

    private static final String APP_FOLDER_NAME = "BNSDKDemo";
    private String mSDCardPath = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        activityList.add(this);


        if (initDirs()) {
            initNavi();
        }

        try {
            Thread.sleep(2000);
        }catch (Exception e){
            e.printStackTrace();
        }

        // BNOuterLogUtil.setLogSwitcher(true);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void delayTest() {
        // SDKInitializer.initialize(BNDemoMainActivity.this.getApplication());
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                Looper.prepare();
//                SDKInitializer.initialize(BNDemoMainActivity.this.getApplication());
//            }
//        }).start();
    }

    //导航相关
    //判断
    private boolean initDirs(){
        mSDCardPath = getSdcardDir();
        if(mSDCardPath == null){
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if(!f.exists()){
            try {
                f.mkdir();
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    String authinfo = null;
    //初始化导航引擎
    private void initNavi() {
        BaiduNaviManager.getInstance().init(LocationActivity.this, mSDCardPath, APP_FOLDER_NAME,
                new BaiduNaviManager.NaviInitListener() {
                    @Override
                    public void onAuthResult(int status, String msg) {
                        if (0 == status) {
                            authinfo = "key校验成功!";
                        } else {
                            authinfo = "key校验失败, " + msg;
                        }
                        LocationActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(LocationActivity.this, authinfo, Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    public void initSuccess() {
                        Toast.makeText(LocationActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();

                        routeplanToNavi(BNRoutePlanNode.CoordinateType.BD09LL);
                    }

                    public void initStart() {
                        Toast.makeText(LocationActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
                    }

                    public void initFailed() {
                        Toast.makeText(LocationActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
                    }
                }, null /*mTTSCallback*/);
    }
    private String getSdcardDir(){
        if(Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)){
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }
    //发起算路
    private void routeplanToNavi(BNRoutePlanNode.CoordinateType coType) {
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        switch(coType) {
            case GCJ02: {
                sNode = new BNRoutePlanNode(NearbyFragment.latLng.longitude, NearbyFragment.latLng.latitude,
                        null, null, coType);
                eNode = new BNRoutePlanNode(NearbyFragment.destinationLatLng.longitude, NearbyFragment.destinationLatLng.latitude,
                        null, null, coType);
                break;
            }
            case WGS84: {
                sNode = new BNRoutePlanNode(NearbyFragment.latLng.longitude, NearbyFragment.latLng.latitude,
                        null, null, coType);
                eNode = new BNRoutePlanNode(NearbyFragment.destinationLatLng.longitude, NearbyFragment.destinationLatLng.latitude,
                        null, null, coType);
                break;
            }
            case BD09_MC: {
                sNode = new BNRoutePlanNode(NearbyFragment.latLng.longitude, NearbyFragment.latLng.latitude,
                        null, null, coType);
                eNode = new BNRoutePlanNode(NearbyFragment.destinationLatLng.longitude, NearbyFragment.destinationLatLng.latitude,
                        null, null, coType);
                break;
            }
            case BD09LL: {
                sNode = new BNRoutePlanNode(NearbyFragment.latLng.longitude, NearbyFragment.latLng.latitude,
                        null, null, coType);
                eNode = new BNRoutePlanNode(NearbyFragment.destinationLatLng.longitude, NearbyFragment.destinationLatLng.latitude,
                        null, null, coType);
                break;
            }
            default :
        }
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(LocationActivity.this, list, 1, true, new DemoRoutePlanListener(sNode));
        }
    }
    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;
        public DemoRoutePlanListener(BNRoutePlanNode node){
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            Intent intent = new Intent(LocationActivity.this, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        @Override
        public void onRoutePlanFailed() {
            L.e("失败了啊，导航!!!!!");

        }
    }

}
