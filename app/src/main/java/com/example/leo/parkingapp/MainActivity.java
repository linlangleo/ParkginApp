package com.example.leo.parkingapp;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.leo.parkingapp.fragment.MineFragment;
import com.example.leo.parkingapp.fragment.NearbyFragment;
import com.example.leo.parkingapp.utils.NoScrollViewPager;
import com.example.leo.parkingapp.utils.ShareUtils;
import com.example.leo.parkingapp.utils.listener.DictationListener;
import com.example.leo.parkingapp.utils.listener.DictationUtil;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
// Toast.makeText(MainActivity.this, "666", Toast.LENGTH_SHORT).show();

public class MainActivity extends AppCompatActivity implements android.view.View.OnClickListener{

    //TabLayout
    private TabLayout mTabLayout;
    //ViewPager
    private NoScrollViewPager mViewPager;
    //Title
    private List<String> mTitle;
    //Fragment
    private List<Fragment> mFragment;
    //悬浮窗
    private FloatingActionButton fab_setting;

    //科大讯飞
    //合成
    private SpeechSynthesizer mTts ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //去掉阴影
        getSupportActionBar().setElevation(0);
        //初始化
        initData();
        initView();

        //Bugly测试
//        CrashReport.testJavaCrash();

//        ShareUtils.putString(this, "username", "刘宇轩");
        String value = ShareUtils.getString(this, "username", "未获取");
    }

    //初始化数据
    private void initData(){
        mTitle = new ArrayList<>();
//        mTitle.add("首页");
        mTitle.add("附近");
        mTitle.add("我的");

        mFragment = new ArrayList<>();
//        mFragment.add(new FirstPageFragment());
        mFragment.add(new NearbyFragment());
        mFragment.add(new MineFragment());

        /*                                 科大讯飞   语音合成   start*/
        //1.创建SpeechSynthesizer对象，第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(getApplicationContext(), null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》 SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaorong"); //设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "80"); //设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "100"); //设置语音，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置(可自定义保存位置)，保存在“./sdcard/iflytek.pcm”
        //保存在SD卡需要在AndroidManifest.xml添加SD卡权限
        //如果不需要保存合成的音频，注释该行代码
//        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
        /*                                 科大讯飞   语音合成   end*/

    }
    //初始化View
    private void initView(){
        //设置浮动按钮
        fab_setting = (FloatingActionButton) findViewById(R.id.fab_setting);
        fab_setting.setOnClickListener(this);

        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        mViewPager = (NoScrollViewPager) findViewById(R.id.mViewPager);
        mViewPager.setNoScroll(true);

        //预加载
        mViewPager.setOffscreenPageLimit(mFragment.size());
        //mViewPager滑动监听
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Log.i("TAG", "position:"+position);
//                if(position == 0){
//                    fab_setting.setVisibility(View.VISIBLE);
//                }else{
//                    fab_setting.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//
//        });

        //设置适配器
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //选中的item
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            //返回item的个数
            @Override
            public int getCount() {
                return mFragment.size();
            }

            //设置标题
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                return super.instantiateItem(container, position);
            }

        });

        //绑定
        mTabLayout.setupWithViewPager(mViewPager);
    }
    //设置点击事件
    public void onClick(View v){
        switch(v.getId()){
            case R.id.fab_setting:
//                startActivity(new Intent(this, SettingActivity.class));
                //语音对话
                /*                                 科大讯飞   语音听写   start*/
                DictationUtil.showDictationDialog(this, new DictationListener() {
                    @Override
                    public void onDictationListener(String dictationResultStr) {
                        Log.e("Result>>>>>>>>>:", dictationResultStr);
                        if(dictationResultStr != null && !dictationResultStr.toString().equals("")){
                            String url = "http://api.jisuapi.com/iqa/query?appkey=7739854da5d22293&question="+dictationResultStr;
                            //get请求简洁版实现
                            RxVolley.get(url, new HttpCallback() {
                                @Override
                                public void onSuccess(String t) {
                                    try {
                                        JSONObject jo = new JSONObject(t);
                                        jo = new JSONObject(jo.get("result").toString());
                                        String content = jo.get("content").toString();
                                        Log.e(">>>>>>>>>>>>:::::", content);
                                        startSpeak(content);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }
                    }
                });
                /*                                 科大讯飞   语音听写   end*/
                break;
        }
    }

    //调用接口类



    //开始说话
    private void startSpeak(String text){
        //3.开始合成
        mTts.startSpeaking(text, mSynListener);
    }
    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener(){
        //回话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {}
        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始的位置，endPos表示缓冲音频在文本中结束位置，info为附加信息
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {}
        //开始播放
        public void onSpeakBegin() {}
        //暂停播放
        public void onSpeakPaused() {}
        //播放进度回调
        ///percent为缓冲进度0~100，beginPos为播放音频在文本中开始的位置，endPos表示播放音频在文本中结束位置
        public void onSpeakProgress(int percent, int beginPos, int endPos) {}
        //恢复播放回调接口
        public void onSpeakResumed() {}
        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {}
    };

}
