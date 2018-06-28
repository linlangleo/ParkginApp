package com.example.leo.parkingapp.utils.listener;

import android.content.Context;

import com.example.leo.parkingapp.utils.StaticClass;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

/**
 * 语音听写工具类，用于弹出语音听写Dialog进行听写
 *
 */

public class DictationUtil {
    private static final String DICTATION_APPID = StaticClass.VOICE_KEY;

    private static SpeechRecognizer mIat;
    private static RecognizerDialog iatDialog;
    private static String dictationResultStr;
    private static String finalResult;

    public static void showDictationDialog(final Context context,
                                           final DictationListener listener) {
        // 初始化语音配置
        initConfig(context);

        // 开始听写
        iatDialog.setListener(new RecognizerDialogListener() {

            @Override
            public void onResult(RecognizerResult results, boolean isLast) {
                if (!isLast) {
                    dictationResultStr += results.getResultString() + ",";
                } else {
                    dictationResultStr += results.getResultString() + "]";

                    finalResult = DictationJsonParseUtil
                            .parseJsonData(dictationResultStr);

                    listener.onDictationListener(finalResult);
                }
          }

            @Override
            public void onError(SpeechError error) {
                error.getPlainDescription(true);
            }
        });

        // 开始听写
        iatDialog.show();
    }

    private static void initConfig(Context context) {
        dictationResultStr = "[";
        finalResult = "";

        // 语音配置对象初始化
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "="
                + DICTATION_APPID);

        // 1.创建SpeechRecognizer对象，第2个参数：本地听写时传InitListener
        mIat = SpeechRecognizer.createRecognizer(context, null);
        // 交互动画
        iatDialog = new RecognizerDialog(context, null);

        // 2.设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
        mIat.setParameter(SpeechConstant.DOMAIN, "iat"); // domain:域名
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin"); // mandarin:普通话
    }
}
