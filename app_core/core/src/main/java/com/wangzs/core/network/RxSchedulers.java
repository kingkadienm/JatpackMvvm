package com.wangzs.core.network;


import com.wangzs.core.network.bean.ResultBody;
import com.wangzs.core.network.exception.RxErrorCode;
import com.wangzs.core.network.exception.RxException;
import com.wangzs.core.network.exception.RxTokenException;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2022-03-10
 * @Version:
 */
public class RxSchedulers {

    /**
     * 模拟网络延时操作（测试 Loading ）
     */
    private static final long DELAY_TIME = 0L;

    /**
     * 封装线程相关
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> io() {
        return upstream ->
                upstream.delay(DELAY_TIME, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 封装网络请求数据
     *
     * @return
     */
    public static ObservableTransformer<JSONObject, Object> handleResult() {
        return upstream -> upstream
                .flatMap((Function<JSONObject, ObservableSource<Object>>) jsonObject -> {

                            ResultBody rxResult = ResultBody.convert(jsonObject);

                            if (rxResult.mCode == RxErrorCode.CODE_SUCCESS) {
                                // 网络数据请求成功
                                return Observable.just(rxResult.mData);
                            } else if (rxResult.mCode == RxErrorCode.CODE_RE_LOGIN_1 || rxResult.mCode == RxErrorCode.CODE_RE_LOGIN_2) {
                                return Observable.error(new RxTokenException(rxResult.mCode, rxResult.mMsg));
                            }
                            // 错误的请求，服务器返回错误码和错误信息
                            return Observable.error(new RxException(rxResult.mCode, rxResult.mMsg));
                        }
                );
    }


}
