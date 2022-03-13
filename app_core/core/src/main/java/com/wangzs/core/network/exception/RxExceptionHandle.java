package com.wangzs.core.network.exception;

import java.io.EOFException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2022-03-10
 * @Version:
 */
public class RxExceptionHandle {

    public static RxException handleException(Throwable e) {

        e.printStackTrace();

        if (e instanceof RxException) {
            return (RxException) e;
        } else if (e instanceof RxTokenException) {
            return (RxTokenException) e;
        } else if (e instanceof EOFException) {
            return new RxException(RxException.RX_DATA_ERROR_MSG);
        } else if (e instanceof SocketTimeoutException) {
            return new RxException(RxException.RX_NETWORK_ERROR_MSG_1);
        } else if (e instanceof UnknownHostException) {
            return new RxException(RxException.RX_NETWORK_ERROR_MSG_2);
        } else {
            return new RxException(RxException.RX_NETWORK_ERROR_MSG_3);
        }
    }
}