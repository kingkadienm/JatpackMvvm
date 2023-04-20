package com.wangzs.core.network.bean;


import com.wangzs.core.network.exception.RxException;
import com.wangzs.core.network.exception.RxTokenException;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2020-03-10
 * @Version:
 */
public class RxResult<T> {

    public final RxStatus status;
    public final T data;
    public final RxException error;

    public RxResult(RxStatus status, T data, RxException error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> RxResult<T> content(T data) {
        return new RxResult<>(RxStatus.Success, data, null);
    }

    public static <T> RxResult<T> error(T data, RxException error) {
        if (error instanceof RxTokenException) {
            // Token 异常，进入登录页面
//            Toasty.normal(BaseApplication.getContext(), error.getMessage()).show();
//            RouterHelper.getActivity(RouterHelper.ACCOUNT.LOGIN_ACTIVITY).navigation();
        }
        return new RxResult<>(RxStatus.Error, data, error);
    }

    public static <T> RxResult<T> error(RxException error) {
        return error(null, error);
    }

    public static <T> RxResult<T> loading(T data) {
        return new RxResult<>(RxStatus.Loading, data, null);
    }

    public static <T> RxResult<T> loading() {
        return loading(null);
    }
}
