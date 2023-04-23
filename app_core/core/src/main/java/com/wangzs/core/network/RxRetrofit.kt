package com.wangzs.core.network

import com.wangzs.core.network.cert.TrustAllManager
import com.wangzs.core.network.converter.GsonConverterFactory
import com.wangzs.core.network.converter.JsonConverterFactory
import com.wangzs.core.network.interceptor.RxEncryptInterceptor
import com.wangzs.core.network.interceptor.RxLoggingInterceptor
import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
//import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2020-03-10
 * @Version:
 */
object RxRetrofit {

    private const val DEFAULT_TIME_OUT = 5
    private const val DEFAULT_READ_TIME_OUT = 10
    private const val DEFAULT_WRITE_TIME_OUT = 10

    // 证书
    private val trustAllManager = TrustAllManager()
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.SECONDS)
        .readTimeout(DEFAULT_READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_WRITE_TIME_OUT.toLong(), TimeUnit.SECONDS)
//        .addNetworkInterceptor(RxCacheInterceptor.onNetwork())
        .addInterceptor(RxLoggingInterceptor.create())
//        .addInterceptor(
//            RxCacheInterceptor.notNetwork()
//        )
//        .cache(RxCacheInterceptor.create())
//        .addInterceptor(RxParamInterceptor.create())
        .addInterceptor(RxEncryptInterceptor.create())
        .hostnameVerifier(TrustAllManager.createTrustAllHostnameVerifier())
        .sslSocketFactory(
            TrustAllManager.createTrustAllSSLFactory(trustAllManager),
            trustAllManager
        )
        .build()


    private val mRetrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(JsonConverterFactory.create())
        .baseUrl(RxUrl.getBaseUrl())
        .build()


    fun <T> create(service: Class<T>?): T {
        return mRetrofit.create(service)
    }


}