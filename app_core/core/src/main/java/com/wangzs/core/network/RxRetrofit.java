package com.wangzs.core.network;



import com.wangzs.core.network.cert.TrustAllManager;
import com.wangzs.core.network.converter.JsonConverterFactory;
import com.wangzs.core.network.interceptor.RxEncryptInterceptor;
import com.wangzs.core.network.interceptor.RxLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2022-03-10
 * @Version:
 */
public final class RxRetrofit {

    private static final int DEFAULT_TIME_OUT = 5;
    private static final int DEFAULT_READ_TIME_OUT = 10;
    private static final int DEFAULT_WRITE_TIME_OUT = 10;

    private Retrofit mRetrofit;

    private static class SingletonHolder {
        private static final RxRetrofit INSTANCE = new RxRetrofit();
    }

    public static RxRetrofit getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private RxRetrofit() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // time out
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_WRITE_TIME_OUT, TimeUnit.SECONDS);
        // cache
//        builder.addNetworkInterceptor(RxCacheInterceptor.onNetwork());
//        builder.addInterceptor(RxCacheInterceptor.notNetwork());
//        builder.cache(RxCacheInterceptor.create());
        // encrypt
//        builder.addInterceptor(RxParamInterceptor.create());
        builder.addInterceptor(RxEncryptInterceptor.create());
        // log
        builder.addInterceptor(RxLoggingInterceptor.create());
        // 证书
        TrustAllManager trustAllManager = new TrustAllManager();
        builder.sslSocketFactory(TrustAllManager.createTrustAllSSLFactory(trustAllManager),
                trustAllManager);
        builder.hostnameVerifier(TrustAllManager.createTrustAllHostnameVerifier());

        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(JsonConverterFactory.create())
                .baseUrl(RxUrl.getBaseUrl())
                .build();
    }

    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }
}
