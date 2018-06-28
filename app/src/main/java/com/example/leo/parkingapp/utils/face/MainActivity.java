//package com.example.leo.parkingapp.utils.face;
//
//import android.graphics.Bitmap;
//import android.hardware.Camera;
//import android.os.Bundle;
//import android.os.StrictMode;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//
//import android.media.FaceDetector;
//
//import com.example.leo.parkingapp.R;
//
//public class MainActivity extends AppCompatActivity implements View.OnClickListener{
//
//    Button btnSend;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//        btnSend = (Button) findViewById(R.id.button);
//        btnSend.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        String url = "https://dtplus-cn-shanghai.data.aliyuncs.com/face/verify";
//        String ak_id = "xxxxxxx";                           // 替换成自己的
//        String ak_secret = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxx"; // 替换成自己的
//
//        Body bd = new Body();
//        bd.type = 0;
//        bd.image_url_1 = "http://pic4.nipic.com/20090926/2121777_021618818098_2.jpg";
//        bd.image_url_2 = "http://pic12.nipic.com/20101225/5311590_211505004116_2.jpg";
//
//        String body = bd.ToString();
//        try {
//            FaceMatchHelper.sendPost(url, body, ak_id, ak_secret);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Body bd2 = new Body();
//
//
//
//
//
//    }
//}
