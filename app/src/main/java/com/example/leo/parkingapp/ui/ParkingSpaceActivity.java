package com.example.leo.parkingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leo.parkingapp.R;
import com.example.leo.parkingapp.application.BaseApplication;

/**
 * 项目名：    ParkingApp
 * 包名： com.example.leo.parkingapp.ui
 * 文件名：    ParkingSpaceActivity
 * 创建者：    leo
 * 创建时间：   2018/3/9 5:00
 * 描述： 车位管理
 */
public class ParkingSpaceActivity extends  BaseActivity {
    //已购买的暂无车位
    private TextView tv_purchased_none;
    //已购的车位
    private LinearLayout ll_parkingspace_purchased;

    //已租的暂无车位
    private TextView tv_rented_none;
    //已租的车位
    private LinearLayout ll_parkingspace_rented;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parkingspace);

        initView();
    }

    private void initView(){
        //已购买的暂无车位
        tv_purchased_none = (TextView) findViewById(R.id.tv_purchased_none);
        //已购的车位列表
        ll_parkingspace_purchased = (LinearLayout) findViewById(R.id.ll_parkingspace_purchased);

        //已租的暂无车位
        tv_rented_none = (TextView) findViewById(R.id.tv_rented_none);
        //已租的车位列表
        ll_parkingspace_rented = (LinearLayout) findViewById(R.id.ll_parkingspace_rented);


        if(((BaseApplication)getApplicationContext()).getLogUser().getProperty("id").toString().equals("2")){
            tv_purchased_none.setVisibility(View.GONE);
            tv_rented_none.setVisibility(View.GONE);

            // 接下来向layout中添加item
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View convertView = inflater.inflate(R.layout.item_parkingspace, null);//获得单个item
            ((TextView)convertView.findViewById(R.id.tv_parkingspace_number)).setText("X988");
            ((TextView)convertView.findViewById(R.id.tv_spaceOfParking)).setText("富荣大厦写字楼-地下停车场");
            ll_parkingspace_purchased.addView(convertView);
            View convertView2 = inflater.inflate(R.layout.item_parkingspace, null);//获得单个item
            ((TextView)convertView2.findViewById(R.id.tv_parkingspace_number)).setText("XU77");
            ((TextView)convertView2.findViewById(R.id.tv_spaceOfParking)).setText("富荣大厦写字楼-地下停车场");
            ll_parkingspace_purchased.addView(convertView2);

            View convertView3 = inflater.inflate(R.layout.item_parkingspace, null);//获得单个item
            ((TextView)convertView3.findViewById(R.id.tv_parkingspace_number)).setText("X987             到期时间:2018-4-3");
            ((TextView)convertView3.findViewById(R.id.tv_spaceOfParking)).setText("富荣大厦写字楼-地下停车场");
            ll_parkingspace_rented.addView(convertView3);
        }

    }

}
