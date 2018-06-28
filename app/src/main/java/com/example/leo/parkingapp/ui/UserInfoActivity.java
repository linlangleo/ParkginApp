package com.example.leo.parkingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leo.parkingapp.MainActivity;
import com.example.leo.parkingapp.R;
import com.example.leo.parkingapp.application.BaseApplication;
import com.example.leo.parkingapp.utils.L;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 项目名：    ParkingApp
 * 包名： com.example.leo.parkingapp.ui
 * 文件名：    UserInfoActivity
 * 创建者：    leo
 * 创建时间：   2018/3/8 16:59
 * 描述： 用户详细信息
 */
public class UserInfoActivity extends BaseActivity implements android.view.View.OnClickListener{
    //昵称
    private EditText et_nickname;
    //性别
    private EditText et_sex;
    //生日
    private EditText et_borndate;
    //手机
    private TextView tv_phone;
    //注册时间
    private TextView tv_registration_time;

    //保存修改按钮
    private TextView update_ok;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        initView();
    }

    private void initView(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //昵称
        et_nickname = (EditText)findViewById(R.id.et_nickname);
        et_nickname.setText(((BaseApplication)getApplicationContext()).getLogUser().getProperty("nickName").toString());
        //性别
        et_sex = (EditText)findViewById(R.id.et_sex);
        et_sex.setText(((BaseApplication)getApplicationContext()).getLogUser().getProperty("sex").toString());
        //生日
        et_borndate = (EditText)findViewById(R.id.et_borndate);
        try{//转换时间
            Date date = dateFormat.parse(((BaseApplication)getApplicationContext()).getLogUser().getProperty("borndate").toString());
            et_borndate.setText(dateFormat.format(date));
        }catch (Exception e){
            e.printStackTrace();
        }
        //手机
        tv_phone = (TextView)findViewById(R.id.tv_phone);
        tv_phone.setText(((BaseApplication)getApplicationContext()).getLogUser().getProperty("phone").toString());
        //注册时间
        tv_registration_time = (TextView)findViewById(R.id.tv_registration_time);
        try{//转换时间
            Date date = dateFormat.parse(((BaseApplication)getApplicationContext()).getLogUser().getProperty("registrationTime").toString());
            tv_registration_time.setText(dateFormat.format(date));
        }catch (Exception e){
            e.printStackTrace();
        }

        //保存修改按钮
        update_ok = (TextView) findViewById(R.id.update_ok);
        update_ok.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.update_ok:
                //绑定信息，远程调用接口
                // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
                new Thread(networkTask).start();
                break;
        }
    }


    //返回结果对象
    SoapObject obj = null;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            Toast.makeText(MainActivity.this, "2", Toast.LENGTH_SHORT).show();
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            L.i( "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
            Toast.makeText(UserInfoActivity.this, "修改成功!！", Toast.LENGTH_SHORT).show();
            finish();
            new Thread(networkTaskForGetNewUser).start();

        }
    };
    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            HttpTransportSE ht = new HttpTransportSE("http://4083f72.nat123.cc/services/appService?wsdl");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            SoapObject request = new SoapObject("http://webservice.parkmanasys.cn", "updateUserInfo");
            /**
             * 设置参数，参数名不一定需要跟调用的服务器端的参数名相同，只需要对应的顺序相同即可
             * */
            request.addProperty("id", ((BaseApplication)getApplicationContext()).getLogUser().getProperty("id"));
            request.addProperty("nickName", et_nickname.getText().toString().trim());
            request.addProperty("sex", et_sex.getText().toString().trim());
            request.addProperty("bornDate", et_borndate.getText().toString().trim());
            envelope.bodyOut = request;

            try {
                // 调用WebService
                ht.call(null, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //给handler使用
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", "我是请求结果");
            msg.setData(data);

            try {
                if(envelope.bodyIn instanceof SoapFault){
                    String str = ((SoapFault) envelope.bodyIn).faultstring;
                    L.i("空节点返回的东西:"+ str);
                }else {
                    // SoapObject sb = (SoapObject)envelope.bodyIn;//服务器返回的对象存在envelope的bodyIn中
//                    obj = (SoapObject) envelope.getResponse();
                    obj = (SoapObject)envelope.bodyIn;//服务器返回的对象存在envelope的bodyIn中
//                    ((BaseApplication)getApplicationContext()).setLogUser((SoapObject) envelope.getResponse()) ;// 直接将返回值强制转换为已知对象

                    L.i("返回结果::"+ obj);
                }
            }catch(Exception e){
                e.printStackTrace();
            }

//            L.i( "名字-->" + result.getProperty(0).toString());
            handler.sendMessage(msg);
        }
    };
    Runnable networkTaskForGetNewUser = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            HttpTransportSE ht = new HttpTransportSE("http://4083f72.nat123.cc/services/appService?wsdl");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            SoapObject request = new SoapObject("http://webservice.parkmanasys.cn", "getUserInfoById");
            /**
             * 设置参数，参数名不一定需要跟调用的服务器端的参数名相同，只需要对应的顺序相同即可
             * */
            request.addProperty("id", ((BaseApplication)getApplicationContext()).getLogUser().getProperty("id"));
            envelope.bodyOut = request;

            try {
                // 调用WebService
                ht.call(null, envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //给handler使用
            Message msg = new Message();

            try {
                if(envelope.bodyIn instanceof SoapFault){
                    String str = ((SoapFault) envelope.bodyIn).faultstring;
                    L.i("空节点返回的东西:"+ str);
                }else {
                    // SoapObject sb = (SoapObject)envelope.bodyIn;//服务器返回的对象存在envelope的bodyIn中
                    obj = (SoapObject) envelope.getResponse();
                    ((BaseApplication)getApplicationContext()).setLogUser((SoapObject) envelope.getResponse()) ;// 直接将返回值强制转换为已知对象

                    L.i("返回结果::"+ obj);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    };

}
