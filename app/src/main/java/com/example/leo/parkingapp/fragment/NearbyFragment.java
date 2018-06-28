package com.example.leo.parkingapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.leo.parkingapp.R;
import com.example.leo.parkingapp.ui.LocationActivity;
import com.example.leo.parkingapp.ui.ParkingDetailActivity;
import com.example.leo.parkingapp.ui.SeekActivity;
import com.example.leo.parkingapp.utils.L;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 项目名：    ParkingApp
 * 包名： com.example.leo.parkingapp.fragment
 * 文件名：    NearbyFragment
 * 创建者：    leo
 * 创建时间：   2018/2/28 15:11
 * 描述： 附近
 */
public class NearbyFragment extends Fragment{
    //页面显示
    private View view;
    private boolean isViewCreate;//view是否创建
    private boolean isViewVisible;//view是否可见

    //百度Map
    private MapView mMapView;
    //定义百度Map
    private BaiduMap mBaiduMap;

    //定位
    public LocationClient mLocationClient = null;
    public MyLocationListener myListener = new MyLocationListener();
    //重定位按钮
    private ImageButton ib_relocation;

    //POI检索实例
    PoiSearch mPoiSearch;
    //建议检索
    SuggestionSearch mSuggestionSearch;

    //当前经纬度
    public static LatLng latLng;
    //目的地经纬度
    public static LatLng destinationLatLng;

    //距离
    private static double distance = 0;
    //停车场名字
    private static String parkingName;
    //停车场地址
    private static String parkingAddress;

    //检索文本框
    private TextView seek;
    //检索按钮
    private ImageView dialog_search_back;
    //语音按钮
    private ImageView dialog_serach_btn_search;

    //底部数据列对象
    private LinearLayout ll_bottom_list;
    //停车场的ListView
    private ListView mListView;

    //导航
    //一些配置
    public static List<Activity> activityList = new LinkedList<Activity>();

    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public static final String SHOW_CUSTOM_ITEM = "showCustomItem";
    public static final String RESET_END_NODE = "resetEndNode";
    public static final String VOID_MODE = "voidMode";

    private static final String APP_FOLDER_NAME = "BNSDKDemo";
    private String mSDCardPath = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            isViewCreate = true;//view已创建
            view = inflater.inflate(R.layout.fragment_nearby, null);

            //进行页面的初始化工作
            initView();

            //开启定位
            mLocationClient.start();
            L.e("开始定位");

        }

        return view;
    }

    //初始化方法
    private void initView() {
        mMapView = (MapView) view.findViewById(R.id.mMapView);
        mBaiduMap = mMapView.getMap();

        ib_relocation = (ImageButton) view.findViewById(R.id.ib_relocation);//重定位按钮

        //创建POI检索实例
        mPoiSearch = PoiSearch.newInstance();
        //建议检索
        mSuggestionSearch = SuggestionSearch.newInstance();

        //检索文本框
        seek = (TextView) view.findViewById(R.id.tv_seek);
        //检索按钮
        dialog_search_back = (ImageView) view.findViewById(R.id.dialog_search_back);
        //语音按钮
        dialog_serach_btn_search = (ImageView) view.findViewById(R.id.dialog_serach_btn_search);

        //底部数据列
        ll_bottom_list = (LinearLayout) view.findViewById(R.id.ll_bottom_list);
//        ib_relocation.setVisibility(View.GONE); //把重定向按钮隐藏

        //声明LocationClient类
        mLocationClient = new LocationClient(getActivity());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);

        initLocation();//初始化地图资源

        //初始化导航引擎
//        if(initDirs()){
//            initNavi();
//        }
    }

    //初始化地图资源
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();

        //地址
        option.setIsNeedAddress(true);

        //经纬度
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(0);
        option.setOpenGps(true);
        option.setLocationNotify(false);//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setEnableSimulateGps(false);

        //是否需要周边POI信息，默认为不需要，即参数为false
        option.setIsNeedLocationPoiList(true);

        mLocationClient.setLocOption(option);
    }

    //创建POI检索监听者
    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener(){
        //获得POI的检索结果，一般检索数据都是在这里获取
        public void onGetPoiResult(PoiResult result){
            //获取POI检索结果
//            Toast.makeText(getActivity(), result.getTotalPageNum(), Toast.LENGTH_LONG).show();
            //如果搜索到的结果不为空，并且没有错误
            if (result != null && result.error == PoiResult.ERRORNO.NO_ERROR) {
                //将查询的数据绑定到LinearLayout
//                for(PoiInfo pInfo : result.getAllPoi()){
//                    add(ll_bottom_list, inflater, pInfo);
//                }

            } else {
                Toast.makeText(getContext(), "未搜索到需要的信息！", Toast.LENGTH_SHORT).show();
            }
        }
        //获得POI的详细检索结果，如果发起的是详细检索，这个方法会得到回调(需要uid)
        //详细检索一般用于单个地点的搜索，比如搜索一大堆信息后，选择其中一个地点再使用详细检索
        public void onGetPoiDetailResult(PoiDetailResult result){
            //获取Place详情页检索结果
            ll_bottom_list = (LinearLayout)view.findViewById(R.id.ll_bottom_list);//获取布局
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            add(ll_bottom_list, inflater, result);
        }
        //获得POI室内检索结果
        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    };
    OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
        public void onGetSuggestionResult(SuggestionResult res) {

            if (res == null || res.getAllSuggestions() == null) {
                //未找到相关结果
                return;
            }

            //获取在线建议检索结果
            //将查询的数据绑定到LinearLayout
            //先绑定到PoiInfoData
            for(SuggestionResult.SuggestionInfo sugInfo : res.getAllSuggestions()){
                //poi详细检索
                mPoiSearch.setOnGetPoiSearchResultListener(poiListener);

                mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                        .poiUid(sugInfo.uid));
                mPoiSearch.destroy();
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    //Fragment的事件
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //重定位点击事件
        ib_relocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.destroyDrawingCache();
                mBaiduMap.clear();
                mLocationClient = new LocationClient(getActivity());

                //开启定位
                mLocationClient.start();
            }
        });
        //搜索跳转
        seek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SeekActivity.class);
                startActivity(intent);
            }
        });
        dialog_search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SeekActivity.class);
                startActivity(intent);
            }
        });
        dialog_serach_btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SeekActivity.class);
                startActivity(intent);
            }
        });

    }

    //向底部数据列添加item
    private void add(LinearLayout ll_bottom_list, LayoutInflater inflater, Object object){
        if(((PoiDetailResult)object).getLocation() == null){//把建议模糊词去掉显示
            return;
        }

        // 接下来向layout中添加item
        View convertView = inflater.inflate(R.layout.item_parking, null);//获得单个item
        //添加
        parkingName = ((PoiDetailResult)object).getName();
        ((TextView)convertView.findViewById(R.id.tv_parkingName)).setText(parkingName);
        ((TextView)convertView.findViewById(R.id.tv_parkingName)).setTag(((PoiDetailResult)object));
        parkingAddress = ((PoiDetailResult)object).getAddress();
        ((TextView)convertView.findViewById(R.id.tv_charge)).setText(parkingAddress);
        //测距离
        distance = DistanceUtil.getDistance(latLng, ((PoiDetailResult)object).getLocation());
        //添加距离
        ((TextView)convertView.findViewById(R.id.tv_distance)).setText("距离"+Math.round(distance)+"米");
        ((ImageView)convertView.findViewById(R.id.iv_lead_to)).setTag(((PoiDetailResult)object).getLocation());

        //给tv_parkingName(停车场名称),添加点击跳转到停车场详细的事件
        ((TextView)convertView.findViewById(R.id.tv_parkingName)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ParkingDetailActivity.class);
                intent.putExtra("parkingName", ((PoiDetailResult)view.getTag()).getName());
                intent.putExtra("parkingAddress", ((PoiDetailResult)view.getTag()).getAddress());
                destinationLatLng = (LatLng) ((PoiDetailResult)view.getTag()).getLocation();
                startActivity(intent);
            }
        });
        //给iv_lead_to(导航图标),添加导航事件
        ((ImageView)convertView.findViewById(R.id.iv_lead_to)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getTag() != null){
                    destinationLatLng = (LatLng) view.getTag();

                    Intent intent = new Intent(getActivity(), LocationActivity.class);
                    startActivity(intent);
                }
            }
        });

        ll_bottom_list.addView(convertView);//添加到传进来的LinearLayout布局
    }


    //定位的回调
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            //信息地址
            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息

            //经纬度
            final double latitude = location.getLatitude();    //获取纬度信息
            final double longitude = location.getLongitude();    //获取经度信息
            final float radius = location.getRadius();    //获取定位精度，默认值为0.0f
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            String coorType = location.getCoorType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            int errorCode = location.getLocType();

            //获取周边POI信息
            //POI信息包括POI ID、名称等，具体信息请参照类参考中POI类的相关说明
            List<Poi> poiList = location.getPoiList();

            //定位的结果
            latLng = new LatLng(latitude, longitude);
            L.e("结束定位");

            //1.移动到我的位置
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(18);//设置缩放，确保屏幕内有我
            mBaiduMap.setMapStatus(mapStatusUpdate);

            //2.开始移动
            MapStatusUpdate mapLatlng = MapStatusUpdateFactory.
                    newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            mBaiduMap.setMapStatus(mapLatlng);

            //3.绘制图层
            //定义Maker坐标点
            LatLng point = new LatLng(location.getLatitude(), location.getLongitude());

            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.ic_location);
            BitmapDescriptor bitmap2 = BitmapDescriptorFactory
                    .fromResource(R.drawable.black);
            ArrayList<BitmapDescriptor> icons = new ArrayList<BitmapDescriptor>();
            icons.add(bitmap);
            icons.add(bitmap2);

            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions().position(point).icon(bitmap).
                    animateType(MarkerOptions.MarkerAnimateType.jump).
                    title("当前位置").
                    draggable(true);  //设置手势拖拽;

            //在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);

            //poi检索
//            mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
//
//            mPoiSearch.searchNearby((new PoiNearbySearchOption())
//                    .keyword("")
//                    .sortType(PoiSortType.distance_from_near_to_far)
//                    .location(latLng)
//                    .radius(50000)
//                    .pageNum(10));
//            mPoiSearch.destroy();

            mSuggestionSearch.setOnGetSuggestionResultListener(listener);
            // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新

            mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                    .keyword("停车场")
                    .city("深圳")
                    .location(latLng));
            mSuggestionSearch.destroy();

            ll_bottom_list.removeAllViews();//重定向完毕后，清空所有的附近停车场，再在底部数据列(建议检索完毕的回调)里面添加。
        }
    }

}
