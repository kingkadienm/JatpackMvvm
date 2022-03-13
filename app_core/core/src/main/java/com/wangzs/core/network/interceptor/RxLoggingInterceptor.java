package com.wangzs.core.network.interceptor;


import com.wangzs.base.toolskit.LogUtil;

import org.jetbrains.annotations.NotNull;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2022-03-10
 * @Version:
 */
public class RxLoggingInterceptor {

    /**
     * 输出log
     *
     * @return
     */
    public static HttpLoggingInterceptor create() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NotNull String message) {
                LogUtil.i(message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);
    }
}
