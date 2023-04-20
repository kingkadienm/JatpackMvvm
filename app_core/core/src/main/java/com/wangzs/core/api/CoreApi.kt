package com.wangzs.core.api

import com.wangzs.core.network.RxRetrofit
import io.reactivex.Observable
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author: wangzs
 * @date: 2020-05-18 13:47
 * @description:
 */
interface CoreApi {


    companion object {
        private val retrofit = RxRetrofit.create(CoreApi::class.java)

        @JvmStatic
        fun getInstance(): CoreApi {
            return retrofit
        }
    }


    @GET("index.php?app=api/package/detectionInfo")
    fun getVersionInfo(
        @Query("versionNumber") versionNumber: String,
        @Query("devices") devices: String,
        @Query("versionCode") versionCode: Int,
        @Query("versionSource") versionSource: Int
    ): Observable<JSONObject>
}