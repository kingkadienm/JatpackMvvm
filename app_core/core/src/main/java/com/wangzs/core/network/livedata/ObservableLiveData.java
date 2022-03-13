package com.wangzs.core.network.livedata;

import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;


import com.wangzs.base.toolskit.LogUtil;
import com.wangzs.core.network.bean.RxResult;
import com.wangzs.core.network.exception.RxExceptionHandle;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * @Description: 拓展LiveData对象
 * @Author: wangzs
 * @Date: 2022-03-10
 * @Version:
 */
public class ObservableLiveData<T> extends MediatorLiveData<RxResult<T>> {
    private WeakReference<Disposable> mDisposableRef;
    private final Observable<T> mObservable;
    private final Object mLock = new Object();
    private AtomicBoolean start = new AtomicBoolean(false);

    ObservableLiveData(@NonNull final Observable<T> observable) {
        mObservable = observable;
    }

    @Override
    protected void onActive() {
        super.onActive();
        if (start.compareAndSet(false, true)) {
            mObservable.subscribe(new Observer<T>() {
                @Override
                public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                    // Don't worry about backpressure. If the stream is too noisy then backpressure can be handled upstream.
                    synchronized (mLock) {
                        mDisposableRef = new WeakReference<>(d);
                    }
                    postValue(RxResult.<T>loading());
                }

                @Override
                public void onNext(@io.reactivex.annotations.NonNull T t) {
                    postValue(RxResult.content(t));
                }

                @Override
                public void onError(@io.reactivex.annotations.NonNull Throwable t) {
                    synchronized (mLock) {
                        mDisposableRef = null;
                    }
                    if (LogUtil.isLogEnabled()) {
                        t.printStackTrace();
                    }
                    // Errors should be handled upstream, so propagate as a crash.
                    postValue(RxResult.error(RxExceptionHandle.handleException(t)));
                }

                @Override
                public void onComplete() {
                    synchronized (mLock) {
                        mDisposableRef = null;
                    }
                }
            });
        }


    }

    @Override
    protected void onInactive() {
        super.onInactive();
        synchronized (mLock) {
            WeakReference<Disposable> subscriptionRef = mDisposableRef;
            if (subscriptionRef != null) {
                Disposable subscription = subscriptionRef.get();
                if (subscription != null) {
                    subscription.dispose();
                }
                mDisposableRef = null;
            }
        }
    }
}