package com.example.leo.parkingapp.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * 项目名：    TestApp
 * 包名： com.example.leo.testapp.utils
 * 文件名：    UtilTools
 * 创建者：    leo
 * 创建时间：   2018/2/24 13:44
 * 描述： 工具统一类
 */
public class UtilTools {

    //设置字体
    public static void setFont(Context mContext, TextView textView){
        Typeface fontType = Typeface.createFromAsset(mContext.getAssets(), "fonts/FONT.TTF");
        textView.setTypeface(fontType);
    }
}
