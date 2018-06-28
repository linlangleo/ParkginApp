package com.example.leo.parkingapp.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leo.parkingapp.R;
import com.example.leo.parkingapp.application.BaseApplication;
import com.example.leo.parkingapp.ui.ParkingSpaceActivity;
import com.example.leo.parkingapp.ui.UserInfoActivity;
import com.example.leo.parkingapp.utils.Base64Util;
import com.example.leo.parkingapp.utils.CustomCamera;
import com.example.leo.parkingapp.utils.FileUtil;
import com.example.leo.parkingapp.utils.L;
import com.example.leo.parkingapp.view.CustomDialog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名：    ParkingApp
 * 包名： com.example.leo.parkingapp.fragment
 * 文件名：    MineFragment
 * 创建者：    leo
 * 创建时间：   2018/2/28 15:11
 * 描述： 我的(个人中心)
 */
public class MineFragment extends Fragment implements android.view.View.OnClickListener{

    //编辑按钮
    private TextView edit_user;

    //编辑头像弹出的三个按钮
    private Button btn_camera;
    private Button btn_video;
    private Button btn_picture;
    private Button btn_cancel;

    //圆形头像
    private CircleImageView profile_image;
    //自定义Dialog
    private CustomDialog dialog;

    //车位管理按钮
    private TextView tv_parkingspace_mana;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, null);

        //初始化数据
        findView(view);

        return view;
    }

    //初始化数据
    private void findView(View view){
        //编辑按钮
        edit_user = (TextView) view.findViewById(R.id.edit_user);
        edit_user.setOnClickListener(this);

        //圆形头像
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);

        //初始化dialog
        dialog = new CustomDialog(getActivity(), 0, 0, R.layout.dialog_image_edit, R.style.pop_anim_style, Gravity.BOTTOM, 0);
        //编辑头像弹出的三个按钮
        btn_camera = (Button) dialog.findViewById(R.id.btn_camera);
        btn_video = (Button) dialog.findViewById(R.id.btn_video);
        btn_picture = (Button) dialog.findViewById(R.id.btn_picture);
        btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btn_camera.setOnClickListener(this);
        btn_video.setOnClickListener(this);
        btn_picture.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        //提示框以外点击无效
        dialog.setCancelable(false);

        //车位管理按钮
        tv_parkingspace_mana = (TextView)view.findViewById(R.id.tv_parkingspace_mana);
        tv_parkingspace_mana.setOnClickListener(this);

        //绑定信息，远程调用接口
        // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
//        new Thread(networkTask).start();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.profile_image:
                dialog.show();
                break;
            case R.id.btn_camera:
                toCamera();//跳转到相机
                break;
            case R.id.btn_video:
                toVideo();//跳转到摄像
                break;
            case R.id.btn_picture:
                toPicture();//跳转到相片
                break;
            case R.id.btn_cancel:
                dialog.dismiss();//关闭泡泡弹出
                break;
            case R.id.edit_user:
                startActivity(new Intent(getActivity(), UserInfoActivity.class));//进入详细信息
                break;
            case R.id.tv_parkingspace_mana://进入车位管理
                startActivity(new Intent(getActivity(), ParkingSpaceActivity.class));
                break;
        }
    }

    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
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
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},222);
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

        dialog.dismiss();
    }

    //跳转到相片
    private void toPicture(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMG_CAMER_REQUEST_CODE);
        dialog.dismiss();
    }

    //跳转到摄像
    private void toVideo(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Uri fileUri = null;
        //权限判断
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},222);
                return;
            }else{
                //调用具体方法
//                try {
//                    fileUri = Uri.fromFile(createMediaFile()); // create a file to save the video
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name
//                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high
//                // start the Video Capture Intent
//                startActivityForResult(intent, VIDEO_CAMER_REQUEST_CODE);

                //此处可执行登录处理
                String ipname = "leo".toString().trim();
                Bundle data = new Bundle();
                data.putString("ipname",ipname);

                Intent intent1 = new Intent(getActivity(), CustomCamera.class);
                intent.putExtras(data);

                startActivity(intent1);
            }
        } else {
            //调用具体方法
//            try {
//                fileUri = Uri.fromFile(createMediaFile()); // create a file to save the video
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name
//            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high
//            // start the Video Capture Intent
//            startActivityForResult(intent, VIDEO_CAMER_REQUEST_CODE);

            Intent intent1 = new Intent(getActivity(), CustomCamera.class);
            startActivity(intent1);
        }

        dialog.dismiss();
    }
    //创建保存录制得到的视频文件
    private File createMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES), "CameraDemo");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                L.d("failed to create directory");
                return null;
            }
        }
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "VID_" + timeStamp;
        String suffix = ".mp4";
        File mediaFile = new File(mediaStorageDir + File.separator + imageFileName + suffix);
        Toast.makeText(getActivity(), mediaFile.getPath()+"", Toast.LENGTH_LONG).show();
        return mediaFile;
    }

    //操作图片相关后，获得返回值
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {//requestCode返回码
        if(resultCode != getActivity().RESULT_CANCELED){//RESULT_CANCELED是0
            switch (requestCode){
                //相册的数据
                case IMG_CAMER_REQUEST_CODE:
//                    startPhotoZoom(data.getData());
                    break;
                //相机的数据
                case CAMER_REQUEST_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);

//                    Toast.makeText(getActivity(), tempFile.getPath()+","+tempFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//                    startPhotoZoom(Uri.fromFile(tempFile));
                    // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
                    new Thread(networkTask).start();
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

    //实时视频相关
    //裁剪
    private void startPhotoZoom(Uri uri){
        if(uri == null){
            L.e("uri == null");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置剪裁
        intent.putExtra("crop", "true");
        //裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }
    //设置图片
    private void setImageToView(){

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
            SoapObject request = new SoapObject("http://webservice.parkmanasys.cn", "plateNumberDiscriminate");
            /**
             * 设置参数，参数名不一定需要跟调用的服务器端的参数名相同，只需要对应的顺序相同即可
             * */
            byte[] imgData = null;
            try {
                imgData = FileUtil.readFileByBytes(tempFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String imgBase64 = Base64Util.encode(imgData);
            request.addProperty("arg0", imgBase64);
            envelope.bodyOut = request;

            SoapObject result=null;
            try {
                // 调用WebService
                ht.call(null, envelope);
                result = (SoapObject) envelope.bodyIn;
            } catch (Exception e) {
                e.printStackTrace();
            }

            //给handle使用
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

                    L.i("返回结果::"+ envelope.getResponse().toString());

                }
            }catch(Exception e){
                e.printStackTrace();
            }

            handler.sendMessage(msg);
        }
    };

}
