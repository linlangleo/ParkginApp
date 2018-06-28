package com.example.leo.parkingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.leo.parkingapp.R;
import com.example.leo.parkingapp.fragment.NearbyFragment;

/**
 * 项目名：    ParkingApp
 * 包名： com.example.leo.parkingapp.ui
 * 文件名：    ParkingDetailActivity
 * 创建者：    leo
 * 创建时间：   2018/3/9 3:20
 * 描述： 停车场详情
 */
public class ParkingDetailActivity extends BaseActivity implements  android.view.View.OnClickListener{
    //停车场名
    private TextView parking_detail_name;
    //停车场地址
    private TextView parking_detail_address;
    //距离
    private TextView parking_detail_distance;
    //停车场免费时长
    private TextView parking_detail_freetime;
    //停车场总车位
    private TextView parking_detail_total_count;
    //停车场剩余车位
    private TextView parking_detail_left_count;

    //是否合作
    private TextView tv_if_cooperation;
    //是否共享
    private TextView tv_if_sharing;
    //是否共享的控制显示
    private LinearLayout ll_if_sharing_judge;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_detail);

        initView();
    }

    public void initView(){
        //停车场名
        parking_detail_name = (TextView) findViewById(R.id.parking_detail_name);
        //停车场地址
        parking_detail_address = (TextView) findViewById(R.id.parking_detail_address);
        //距离
        parking_detail_distance = (TextView) findViewById(R.id.parking_detail_distance);
        //停车场免费时长
        parking_detail_freetime = (TextView) findViewById(R.id.parking_detail_freetime);
        //停车场总车位
        parking_detail_total_count = (TextView) findViewById(R.id.parking_detail_total_count);
        //停车场剩余车位
        parking_detail_left_count = (TextView) findViewById(R.id.parking_detail_left_count);

        //绑定详细数据
        Intent intent = getIntent();
        parking_detail_name.setText(intent.getStringExtra("parkingName"));
        parking_detail_address.setText(intent.getStringExtra("parkingAddress"));
        parking_detail_distance.setText(Math.round(DistanceUtil.getDistance(NearbyFragment.latLng, NearbyFragment.destinationLatLng))+"");

        //是否合作
        tv_if_cooperation = (TextView) findViewById(R.id.tv_if_cooperation);
        //是否共享
        tv_if_sharing = (TextView) findViewById(R.id.tv_if_sharing);
        tv_if_sharing.setOnClickListener(this);
        //是否共享的控制显示
        ll_if_sharing_judge = (LinearLayout) findViewById(R.id.ll_if_sharing_judge);
        //判断停车场是否合作
        if(parking_detail_name.getText().toString().trim().equals("富荣大厦写字楼-地下停车场")){
            tv_if_cooperation.setText("已合作，可以进行车位使用");
            tv_if_sharing.setText("已开通共享， 点击进入共享车位查看");
            ll_if_sharing_judge.setVisibility(View.VISIBLE);
            //基础信息，先乱绑定吧
            parking_detail_freetime.setText("无");
            parking_detail_total_count.setText("52");
            parking_detail_left_count.setText("19");
        }else{
            tv_if_cooperation.setText("未合作停车场");
            tv_if_sharing.setText("未开通共享平台");
            ll_if_sharing_judge.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_if_sharing:
                startActivity(new Intent(ParkingDetailActivity.this, SharingPlatformActivity.class));
                finish();
                break;
        }
    }
}
