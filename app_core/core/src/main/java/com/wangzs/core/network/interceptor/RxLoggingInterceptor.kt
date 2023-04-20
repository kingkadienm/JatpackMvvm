package com.wangzs.core.network.interceptor

import com.wangzs.base.toolskit.LogUtil
import okhttp3.logging.HttpLoggingInterceptor

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2020-03-10
 * @Version:
 */
object RxLoggingInterceptor {
    /**
     * 输出log
     *
     * @return
     */
//    fun create(): HttpLoggingInterceptor {
//        return HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
//            override fun log(message: String) {
//                LogUtil.i(message)
//            }
//        }).setLevel(HttpLoggingInterceptor.Level.BODY)
//    }
    fun create(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

}