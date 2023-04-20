package com.wangzs.core.network.interceptor;


import com.wangzs.base.toolskit.LogUtil;
import com.wangzs.core.network.RxUrl;
import com.wangzs.core.network.bean.RxCommonParam;

import java.io.IOException;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Description: network interceptor
 * @Author: wangzs
 * @Date: 2020-03-10
 * @Version:
 */
public class RxEncryptInterceptor {

    private static final String TAG = RxEncryptInterceptor.class.getName();

    public static Interceptor create() {
        return chain -> {
            Request request = chain.request();
            request = replaceHost(request);
            LogUtil.d(TAG, "Network Request URL:" + request.url());
            request = addCommonParams(request);
            request = addHeader(request);
            Response response = chain.proceed(request);
            return response;
        };
    }

    /**
     * 替换 url
     *
     * @param request
     * @return
     */
    private static Request replaceHost(Request request) {
        Request.Builder builder = request.newBuilder();
        HttpUrl newBaseUrl = HttpUrl.parse(RxUrl.getBaseUrl());
        HttpUrl oldHttpUrl = request.url();
        //重建新的HttpUrl，修改需要修改的url部分
        HttpUrl newFullUrl = oldHttpUrl
                .newBuilder()
                .scheme(newBaseUrl.scheme())
                .host(newBaseUrl.host())
                .port(newBaseUrl.port())
                .build();
        return builder.url(newFullUrl).build();
    }

    private static Request addCommonParams(Request oldRequest) {
        HttpUrl.Builder builder = oldRequest.url()
                .newBuilder();
        Map<String, Object> params = RxCommonParam.getCommonParams();
        for (String key : params.keySet()) {
            builder.setEncodedQueryParameter(key, String.valueOf(params.get(key)));
        }
        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(builder.build())
                .build();
        return newRequest;
    }

    /**
     * Request数据加密
     *
     * @param request
     * @return
     * @throws IOException
     */
    private static Request addHeader(Request request) {
        Request.Builder requestBuilder = request.newBuilder();
        HttpUrl url = request.url();
        HttpUrl.Builder builder = url.newBuilder();
        requestBuilder.url(builder.build())
                .method(request.method(), request.body())
                .addHeader("Content_Type", "application/json")
                .addHeader("charset", "UTF-8");

//        String cookie = RouterHelper.getService().getAccountService().getAccountCookie();
//        if (!TextUtils.isEmpty(cookie)) {
//            requestBuilder.addHeader("Cookie", cookie); // "JSESSIONID=constant-session-0"
//        }
        requestBuilder.addHeader("appVersion", "1.0.0");
        return requestBuilder.build();
    }
}
