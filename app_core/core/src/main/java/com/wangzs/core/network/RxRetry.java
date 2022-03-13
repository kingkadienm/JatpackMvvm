package com.wangzs.core.network;



import com.wangzs.base.toolskit.LogUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2022-03-10
 * @Version:
 */
public class RxRetry implements Function<Observable<? extends Throwable>, Observable<?>> {

    private static final String TAG = RxRetry.class.getName();
    /**
     * 重试次数
     */
    private static final int MAX_RETRIES = 2;
    /**
     * 重试间隔时间
     */
    private static final int RETRY_DELAY_MILLIS = 1000;
    /**
     * 当前出错重试次数
     */
    private int retryCount = 0;

    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) {
        return observable
                .flatMap((Function<Throwable, ObservableSource<?>>) throwable -> {

                    throwable.printStackTrace();

                    if (throwable instanceof ConnectException
                            || throwable instanceof SocketTimeoutException) {
                        // 网络错误才会重试
                        if (++retryCount <= MAX_RETRIES) {
                            LogUtil.i(TAG, "Network error, retry count " + retryCount + " retry,"
                                    + "it will try after " + RETRY_DELAY_MILLIS * retryCount);
                            return Observable.timer(RETRY_DELAY_MILLIS * retryCount,
                                    TimeUnit.MILLISECONDS);
                        }
                    }

                    return Observable.error(throwable);
                });
    }
}
