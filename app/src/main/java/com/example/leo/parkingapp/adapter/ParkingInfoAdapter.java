package com.example.leo.parkingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leo.parkingapp.R;
import com.example.leo.parkingapp.entity.ParkingInfo;

import java.util.List;

/**
 * 项目名：    ParkingApp
 * 包名： com.example.leo.parkingapp.adapter
 * 文件名：    ParkingInfoAdapter
 * 创建者：    leo
 * 创建时间：   2018/3/3 10:53
 * 描述： 停车场信息
 */
public class ParkingInfoAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<ParkingInfo> mList;
    private ParkingInfo data;

    public ParkingInfoAdapter(Context mContext, List<ParkingInfo> mList){
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
            convertView = inflater.inflate(R.layout.item_parking, null);

            viewHolder.iv_lead_to = (ImageView) convertView.findViewById(R.id.iv_lead_to);
            viewHolder.tv_parkingName = (TextView) convertView.findViewById(R.id.tv_parkingName);
            viewHolder.tv_charge = (TextView) convertView.findViewById(R.id.tv_charge);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        data = mList.get(position);
        viewHolder.tv_parkingName.setText(data.getParkingName());
        viewHolder.tv_charge.setText(data.getCharge()+"");

        return convertView;
    }

    class ViewHolder{
        private ImageView iv_lead_to;
        private TextView tv_parkingName;
        private TextView tv_charge;
    }
}
