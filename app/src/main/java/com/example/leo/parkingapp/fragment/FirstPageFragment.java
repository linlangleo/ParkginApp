package com.example.leo.parkingapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leo.parkingapp.R;

/**
 * 项目名：    ParkingApp
 * 包名： com.example.leo.parkingapp.fragment
 * 文件名：    FirstPageFragment
 * 创建者：    leo
 * 创建时间：   2018/2/28 15:10
 * 描述： 首页
 */
public class FirstPageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_firstpage, null);
        return view;
    }
}
