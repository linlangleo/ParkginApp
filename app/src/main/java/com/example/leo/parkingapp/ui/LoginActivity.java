package com.example.leo.parkingapp.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leo.parkingapp.MainActivity;
import com.example.leo.parkingapp.R;
import com.example.leo.parkingapp.application.BaseApplication;
import com.example.leo.parkingapp.utils.Base64Util;
import com.example.leo.parkingapp.utils.FileUtil;
import com.example.leo.parkingapp.utils.L;
import com.example.leo.parkingapp.utils.ShareUtils;
import com.example.leo.parkingapp.utils.StaticClass;
import com.example.leo.parkingapp.utils.face.Body;
import com.example.leo.parkingapp.utils.face.FaceMatchHelper;
import com.example.leo.parkingapp.view.CustomDialog;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.IOException;

/**
 * 项目名：    ParkingApp
 * 包名： com.example.leo.parkingapp.ui
 * 文件名：    LoginActivity
 * 创建者：    leo
 * 创建时间：   2018/3/8 0:26
 * 描述： 登陆
 */
public class LoginActivity extends AppCompatActivity implements  android.view.View.OnClickListener{

    private Button btn_registered;//注册按钮
    private Button btn_login;//注册按钮
    //输入框
    private EditText et_name;
    private EditText et_password;
    //记住密码
    private CheckBox keep_password;

    //自定义Dialog
    private CustomDialog customDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

    }

    private void initView(){
        //注册按钮
        btn_registered = (Button) findViewById(R.id.btn_registered);
        btn_registered.setOnClickListener(this);
        //登陆按钮
        btn_login = (Button) findViewById(R.id.btn_login);//注册按钮
        btn_login.setOnClickListener(this);
        //输入框
        et_name = (EditText) findViewById(R.id.et_name);
        et_password = (EditText) findViewById(R.id.et_password);
        //记住密码
        keep_password = (CheckBox) findViewById(R.id.keep_password);

        //自定义Dialog
        customDialog = new CustomDialog(this, 100, 100, R.layout.dialog_loding, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        //屏幕外点击无效
        customDialog.setCancelable(false);

        //设置选中的状态
        boolean isCheck = ShareUtils.getBoolean(this, "keeppassword", false);
        keep_password.setChecked(isCheck);
        if(isCheck){
            //设置密码
            et_name.setText(ShareUtils.getString(this, "name", ""));
            et_password.setText(ShareUtils.getString(this, "password", ""));
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_registered:
//                startActivity(new Intent(this, RegisteredActivity.class));
                toCamera();
                break;
            case R.id.btn_login:
                //1.获取输入框的值
                String name = et_name.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                //2.判断是否为空
                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)){
                    //登陆
                    customDialog.show();
                    //绑定信息，远程调用接口
                    // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
                    new Thread(networkTask).start();
                }else{
                    Toast.makeText(this, "输入框不能为空!！", Toast.LENGTH_SHORT).show();
                }
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
            customDialog.dismiss();
            if(obj != null){
                startActivity(new Intent(LoginActivity.this, MainActivity.class));//跳登陆注册页
                finish();
            }else{
                Toast.makeText(LoginActivity.this, "账号或者密码错误!！", Toast.LENGTH_SHORT).show();
            }
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
            HttpTransportSE ht = new HttpTransportSE("http://192.168.43.224:8089/services/appService?wsdl");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            SoapObject request = new SoapObject("http://webservice.parkmanasys.cn", "appLogin");
            /**
             * 设置参数，参数名不一定需要跟调用的服务器端的参数名相同，只需要对应的顺序相同即可
             * */
            request.addProperty("userName", et_name.getText().toString().trim());
            request.addProperty("password", et_password.getText().toString().trim());
            envelope.bodyOut = request;

            SoapObject result=null;
            try {
                // 调用WebService
                ht.call(null, envelope);
                result = (SoapObject) envelope.bodyIn;
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
                    obj = (SoapObject) envelope.getResponse();
                    ((BaseApplication)getApplicationContext()).setLogUser((SoapObject) envelope.getResponse()) ;// 直接将返回值强制转换为已知对象

                    L.i("返回结果::"+ ((BaseApplication)getApplicationContext()).getLogUser().getProperty("realName"));

                }
            }catch(Exception e){
                e.printStackTrace();
            }

//            L.i( "名字-->" + result.getProperty(0).toString());
            handler.sendMessage(msg);
        }
    };


    //假设我现在输入用户名和密码，但是不点击登陆，而是直接退出了(记住密码按钮生效)
    @Override
    protected void onDestroy() {
        super.onDestroy();

        //保存状态
        ShareUtils.putBoolean(this, "keeppassword", keep_password.isChecked());

        //是否记住密码
        if(keep_password.isChecked()){
            //记住用户名和密码
            ShareUtils.putString(this, "name", et_name.getText().toString().trim());
            ShareUtils.putString(this, "password", et_password.getText().toString().trim());
        }else{
            //清除用户名和密码
            ShareUtils.deleShare(this, "name");
            ShareUtils.deleShare(this, "password");
        }
    }

    public static final String PHOTO_IMAGE_FILE_NAME = "faceImg.jpg";
    public static final int CAMER_REQUEST_CODE = 100;
    public static final int IMG_CAMER_REQUEST_CODE = 101;
    public static final int RESULT_REQUEST_CODE = 102;
    public static final int VIDEO_CAMER_REQUEST_CODE = 103;
    //相机数据
    private File tempFile = null;
    //跳转到相机
    private void toCamera(){
        //权限判断
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.CAMERA},222);
                return;
            }else{
                //调用具体方法
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //判断内存卡是否可用，可用的话就进行存储
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME)));
                startActivityForResult(intent, CAMER_REQUEST_CODE);
            }
        } else {
            //调用具体方法
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //判断内存卡是否可用，可用的话就进行存储
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME)));
            startActivityForResult(intent, CAMER_REQUEST_CODE);
        }
    }
    //操作图片相关后，获得返回值
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {//requestCode返回码
        if(resultCode != LoginActivity.RESULT_CANCELED){//RESULT_CANCELED是0
            switch (requestCode){
                //相册的数据
                case IMG_CAMER_REQUEST_CODE:
//                    startPhotoZoom(data.getData());
                    break;
                //相机的数据
                case CAMER_REQUEST_CODE:
                    customDialog.show();
                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);

//                    startPhotoZoom(Uri.fromFile(tempFile));
                    // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
                    new Thread(faceNetworkTask ).start();

                    break;
                //摄像的数据
                case VIDEO_CAMER_REQUEST_CODE:
                    break;
                //裁剪的数据
                case RESULT_REQUEST_CODE:
                    break;
            }

        }
    }
    Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            Toast.makeText(MainActivity.this, "2", Toast.LENGTH_SHORT).show();
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("result");
//            Toast.makeText(getApplicationContext(), val, Toast.LENGTH_SHORT).show();
            String confidence = null;
            try {
                JSONObject jo = new JSONObject(val);
                confidence = jo.getString("confidence");
                L.i( "置信度为-->" + confidence);
                if(Double.valueOf(confidence) >= 70){
                    //识别成功
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));//跳登陆注册页
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "人脸不匹配!！", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            customDialog.dismiss();
            L.i( "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
        }
    };
    /**
     * 网络操作相关的子线程
     */
    Runnable faceNetworkTask = new Runnable() {

        @Override
        public void run() {
            // TODO
            //压缩图片
            Log.e("path:", tempFile.getPath());
            HttpTransportSE ht = new HttpTransportSE("http://192.168.43.224:8089/services/appService?wsdl");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            SoapObject request = new SoapObject("http://webservice.parkmanasys.cn", "compressPicture");
            /**
             * 设置参数，参数名不一定需要跟调用的服务器端的参数名相同，只需要对应的顺序相同即可
             * */
            byte[] imgData = null;
            try {
                imgData = FileUtil.readFileByBytes(tempFile.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String imgBase64 = Base64Util.encode(imgData);
            request.addProperty("arg0", imgBase64);
            envelope.bodyOut = request;

            SoapObject resultO=null;
            String picBase64 = null;
            try {
                // 调用WebService
                ht.call(null, envelope);
                resultO = (SoapObject) envelope.bodyIn;
                    L.i("空节点返回的东西:"+ resultO.getProperty(0));
                picBase64 = resultO.getProperty(0).toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 在这里进行 http request.网络请求相关操作
            String url = "https://dtplus-cn-shanghai.data.aliyuncs.com/face/verify";
            String ak_id = "LTAInEfAaHbnEU6x";                           // 替换成自己的
            String ak_secret = "TTPQQKj6Dqz5Hs52NuiVivWidPOo4c"; // 替换成自己的

            Body bd = new Body();
//            bd.type = 0;
//            bd.image_url_1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530016616636&di=30a6203c3abbed2040617dc64d346dc1&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F267f9e2f07082838fa79c330b299a9014c08f19a.jpg";
//            bd.image_url_2 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530016616636&di=30a6203c3abbed2040617dc64d346dc1&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F267f9e2f07082838fa79c330b299a9014c08f19a.jpg";
            bd.type = 1;
            bd.content_1 = StaticClass.LEO_FACE;
            bd.content_2 = picBase64;

            String body = bd.ToString();
            String result = null;
            try {
                result = FaceMatchHelper.sendPost(url, body, ak_id, ak_secret);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //给handle使用
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("result", result);
            msg.setData(data);

            handler2.sendMessage(msg);
        }
    };

}
