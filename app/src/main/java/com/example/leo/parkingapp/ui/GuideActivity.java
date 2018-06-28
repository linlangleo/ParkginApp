package com.example.leo.parkingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.leo.parkingapp.MainActivity;
import com.example.leo.parkingapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：    TestApp
 * 包名： com.example.leo.testapp.ui
 * 文件名：    GuideActivity
 * 创建者：    leo
 * 创建时间：   2018/2/24 19:52
 * 描述： 引导页
 */
public class GuideActivity extends AppCompatActivity implements android.view.View.OnClickListener{

    private ViewPager mViewPager_guide;
    //容器
    private List<View> mList = new ArrayList<>();
    private View view1, view2, view3;

    //小圆点
    private ImageView point1, point2, point3;

    //跳过按钮
    private ImageView iv_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_activity);

        initView();
    }

    //初始化View
    private void initView(){
        //跳过按钮
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);

        point1 = (ImageView) findViewById(R.id.point1);
        point2 = (ImageView) findViewById(R.id.point2);
        point3 = (ImageView) findViewById(R.id.point3);

        //设置默认图片
        setPointImg(true, false, false);

        mViewPager_guide = (ViewPager) findViewById(R.id.mViewPager_guide);

        view1 = View.inflate(this, R.layout.guide_item1, null);
        view2 = View.inflate(this, R.layout.guide_item2, null);
        view3 = View.inflate(this, R.layout.guide_item3, null);

        //进入按钮
        view3.findViewById(R.id.btn_start).setOnClickListener(this);

        mList.add(view1);
        mList.add(view2);
        mList.add(view3);

        //设置适配器
        mViewPager_guide.setAdapter(new GuideAdapter());

        //监听view的滑动
        mViewPager_guide.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //page切换的回调
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        setPointImg(true, false, false);
                        iv_back.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        setPointImg(false, true, false);
                        iv_back.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        setPointImg(false, false, true);
                        iv_back.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class GuideAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(mList.get(position));
            return mList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(mList.get(position));
//            super.destroyItem(container, position, object);
        }
    }

    private void setPointImg(boolean isCheck1, boolean isCheck2, boolean isCheck3){
        if(isCheck1){
            point1.setBackgroundResource(R.drawable.point_on);
        }else{
            point1.setBackgroundResource(R.drawable.point_off);
        }
        if(isCheck2){
            point2.setBackgroundResource(R.drawable.point_on);
        }else{
            point2.setBackgroundResource(R.drawable.point_off);
        }
        if(isCheck3){
            point3.setBackgroundResource(R.drawable.point_on);
        }else{
            point3.setBackgroundResource(R.drawable.point_off);
        }
    }

    //点击事件
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_start:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.iv_back:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }

}
