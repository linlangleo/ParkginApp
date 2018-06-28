package com.example.leo.parkingapp.ui;

import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.leo.parkingapp.R;
import com.example.leo.parkingapp.adapter.PoiInfoAdapter;
import com.example.leo.parkingapp.entity.PoiInfoData;
import com.example.leo.parkingapp.utils.Base64Util;
import com.example.leo.parkingapp.utils.FileUtil;
import com.example.leo.parkingapp.utils.L;
import com.example.leo.parkingapp.utils.listener.DictationListener;
import com.example.leo.parkingapp.utils.listener.DictationUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：    ParkingApp
 * 包名： com.example.leo.parkingapp.ui
 * 文件名：    SeekActivity
 * 创建者：    leo
 * 创建时间：   2018/3/1 16:09
 * 描述： TODO
 */
public class SeekActivity extends AppCompatActivity implements android.view.View.OnClickListener {

    //poi的ListView
    private ListView mListView;

    //返回的按钮
    private ImageView dialog_search_back;

    //关键字输入框
    private EditText dialog_search_et;

    //语音按钮
    private ImageView dialog_serach_btn_search;

    //POI检索实例
    PoiSearch mPoiSearch;
    //建议检索
    SuggestionSearch mSuggestionSearch;
    List<PoiInfoData> poiInfoDataList = new ArrayList<>();
    private LatLng latLng;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek);

        initView();
    }

    private void initView(){
        //poi的ListView
        mListView = (ListView) findViewById(R.id.mListView);

        //返回的按钮
        dialog_search_back = (ImageView) findViewById(R.id.dialog_search_back);
        dialog_search_back.setOnClickListener(this);

        //关键字的输入框
        dialog_search_et = (EditText) findViewById(R.id.dialog_search_et);
        dialog_search_et.addTextChangedListener(new mTextChanged());

        //语音按钮
        dialog_serach_btn_search = (ImageView) findViewById(R.id.dialog_serach_btn_search);
        dialog_serach_btn_search.setOnClickListener(this);

        //创建POI检索实例
        mPoiSearch = PoiSearch.newInstance();
        //建议检索
        mSuggestionSearch = SuggestionSearch.newInstance();

    }


    //创建POI检索监听者
    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener(){
        //获得POI的检索结果，一般检索数据都是在这里获取
        public void onGetPoiResult(PoiResult result){
            //获取POI检索结果
//            Toast.makeText(getActivity(), result.getTotalPageNum(), Toast.LENGTH_LONG).show();
            //如果搜索到的结果不为空，并且没有错误
            if (result != null && result.error == PoiResult.ERRORNO.NO_ERROR) {
//                Toast.makeText(getApplicationContext(), "已搜索到需要的信息！", Toast.LENGTH_SHORT).show();
//
//                //将查询的数据绑定到ListView
//                //先绑定到PoiInfoData
//                List<PoiInfoData> poiInfoDataList = new ArrayList<>();
//                for(PoiInfo p : result.getAllPoi()){
//                    PoiInfoData poiInfoData = new PoiInfoData();
//                    poiInfoData.setName(p.name);
//                    poiInfoData.setAddress(p.address);
//                    poiInfoDataList.add(poiInfoData);
//                }
//
//                PoiInfoAdapter adapter = new PoiInfoAdapter(getApplicationContext(),poiInfoDataList );
//                mListView.setAdapter(adapter);

            } else {
                Toast.makeText(getApplicationContext(), "未搜索到需要的信息！", Toast.LENGTH_SHORT).show();
            }
        }
        //获得POI的详细检索结果，如果发起的是详细检索，这个方法会得到回调(需要uid)
        //详细检索一般用于单个地点的搜索，比如搜索一大堆信息后，选择其中一个地点再使用详细检索
        public void onGetPoiDetailResult(PoiDetailResult result){
            //获取Place详情页检索结果
            PoiInfoData data = new PoiInfoData();
            data.setAddress(result.getAddress());
            data.setName(result.getName());
            poiInfoDataList.add(data);
        }
        //获得POI室内检索结果
        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    };
    OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
        public void onGetSuggestionResult(SuggestionResult res) {
//            Toast.makeText(getApplicationContext(), res.getAllSuggestions().isEmpty()+"", Toast.LENGTH_LONG).show();

            if (res == null || res.getAllSuggestions() == null) {
                return;
                //未找到相关结果
            }
            //将查询的数据绑定到LinearLayout
            for(SuggestionResult.SuggestionInfo sugInfo : res.getAllSuggestions()){
                //poi检索
                mPoiSearch.setOnGetPoiSearchResultListener(poiListener);

                mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                        .poiUid(sugInfo.uid));
                mPoiSearch.destroy();
            }
            //将list加入到listview
            PoiInfoAdapter adapter = new PoiInfoAdapter(getApplicationContext(),poiInfoDataList );
            mListView.setAdapter(adapter);
            poiInfoDataList.clear();//清空
            //获取在线建议检索结果
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_search_back:
                finish();
                break;
            case R.id.dialog_serach_btn_search:
                /*                                 科大讯飞   语音听写   start*/
                DictationUtil.showDictationDialog(this, new DictationListener() {
                    @Override
                    public void onDictationListener(String dictationResultStr) {
                        Log.e("Result>>>>>>>>>:", dictationResultStr);
                        if(dictationResultStr != null && !dictationResultStr.toString().equals("") && dictationResultStr.contains("去")){
                            dictationResultStr = dictationResultStr.substring(dictationResultStr.indexOf("去")+1);
                        }
                        //清理
                        dialog_search_et.clearComposingText();
                        dialog_search_et.clearFocus();
                        //设值
                        dialog_search_et.setText(dictationResultStr);
                        dialog_search_et.requestFocus();
                        dialog_search_et.setSelection(dictationResultStr
                                .length());
                        //搜索
                        if(dictationResultStr != null && !dictationResultStr.toString().equals("")){
                            mSuggestionSearch.setOnGetSuggestionResultListener(listener);
                            // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新

                            mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                                    .keyword(dictationResultStr)
                                    .city("佛山")
                                    .location(latLng));
                            mSuggestionSearch.destroy();
                        }
                    }
                });
                /*                                 科大讯飞   语音听写   end*/
                break;
        }
    }

    //输入框的内容改变监听类
    class mTextChanged implements TextWatcher {//添加内容改变的监听

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // 输入的内容变化的监听
//                Toast.makeText(getApplicationContext(), "输入的内容变化的监听", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            // 输入前的监听
//                Toast.makeText(getApplicationContext(), "输入前的监听", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void afterTextChanged(Editable s) {
            // 输入后的监听
            if(s != null && !s.toString().equals("")){
                mSuggestionSearch.setOnGetSuggestionResultListener(listener);
                // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新

                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                        .keyword(s.toString())
                        .city("佛山")
                        .location(latLng));
                mSuggestionSearch.destroy();
            }
        }
    }

}
