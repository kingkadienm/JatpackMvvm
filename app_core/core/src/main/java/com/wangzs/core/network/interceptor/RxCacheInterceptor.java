package com.wangzs.core.network.interceptor;


import com.wangzs.base.BaseApplication;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2020-03-10
 * @Version:
 */

public class RxCacheInterceptor {

    /**
     * Rx缓存配置
     *
     * @return
     */
    public static Cache create() {
        return new Cache(BaseApplication.getContext().getCacheDir(), 10 * 1024 * 1024); // 缓存大小10M
    }

    /**
     * 有网时的缓存设置
     *
     * @return
     */
    public static Interceptor onNetwork() {
        return chain -> {
            Request request = chain.request();
            Response originalResponse = chain.proceed(request);
            int maxAge = 30; // 在线缓存,单位:秒
            return originalResponse.newBuilder()
                    .removeHeader("Pragma") // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        };
    }

    /**
     * 无网时的缓存设置
     *
     * @return
     */
    public static Interceptor notNetwork() {
        return chain -> {
            Request request = chain.request();
//            if (!NetworkUtil.isConnected(BaseApplication.getContext())) {
//                int maxStale = 12 * 60 * 60; // 缓存时间
//                CacheControl tempCacheControl = new CacheControl.Builder()
//                        .onlyIfCached()
//                        .maxStale(maxStale, TimeUnit.SECONDS)
//                        .build();
//                request = request.newBuilder()
//                        .cacheControl(tempCacheControl)
//                        .build();
//            }
            return chain.proceed(request);
        };
    }
}
