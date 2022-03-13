package com.wangzs.core.network.livedata;

import androidx.lifecycle.MediatorLiveData;


import com.wangzs.core.network.bean.RxResult;

import io.reactivex.Observable;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2022-03-10
 * @Version:
 */
public class LiveDataObservableAdapter {

    public static <T> MediatorLiveData<RxResult<T>> fromObservable(final Observable<T> observable) {
        return new ObservableLiveData<>(observable);
    }

}
