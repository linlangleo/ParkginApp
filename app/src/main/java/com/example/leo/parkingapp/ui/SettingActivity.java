package com.example.leo.parkingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.leo.parkingapp.R;


/**
 * 项目名：    TestApp
 * 包名： com.example.leo.testapp.ui
 * 文件名：    SettingActivity
 * 创建者：    leo
 * 创建时间：   2018/2/24 16:38
 * 描述： 设置
 */
public class SettingActivity extends BaseActivity implements android.view.View.OnClickListener{

    //我的位置
    private LinearLayout ll_my_location;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    //初始化View
    private void initView(){

        //初始化我的位置
        ll_my_location = (LinearLayout)findViewById(R.id.ll_my_location);
        ll_my_location.setOnClickListener(this);
    }

    //点击事件
    @Override
    public void onClick(View v){
        switch (v.getId()){

            case R.id.ll_my_location:
//                startActivity(new Intent(this, LocationActivity.class));
                break;
        }

    }
}
