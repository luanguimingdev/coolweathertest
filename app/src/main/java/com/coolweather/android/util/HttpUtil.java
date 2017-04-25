package com.coolweather.android.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by xh on 2017/4/25.
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address ,okhttp3.Callback calback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        /***
         * callback是一个处理回调结果的接口内部包含请求成功的处理方法和处理失败的处理方法
         */
        client.newCall(request).enqueue(calback);

    }
}
