package com.example.leo.parkingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leo.parkingapp.R;
import com.example.leo.parkingapp.entity.PoiInfoData;

import java.util.List;


/**
 * 项目名：    ParkingApp
 * 包名： com.example.leo.parkingapp.adapter
 * 文件名：    PoiInfoAdapter
 * 创建者：    leo
 * 创建时间：   2018/3/1 19:27
 * 描述： 目的地poi的adapter
 */

public class PoiInfoAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<PoiInfoData> mList;
    private PoiInfoData data;

    public PoiInfoAdapter(Context mContext, List<PoiInfoData> mList){
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_poi, null);

            viewHolder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        data = mList.get(position);
        viewHolder.tv_name.setText(data.getName());
        viewHolder.tv_address.setText(data.getAddress());

        return convertView;
    }

    class ViewHolder{
        private ImageView iv_img;
        private TextView tv_name;
        private TextView tv_address;
    }
}
