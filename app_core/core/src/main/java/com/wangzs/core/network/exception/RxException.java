package com.wangzs.core.network.exception;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2022-03-10
 * @Version:
 */
public class RxException extends Exception {

    public static final String RX_NETWORK_ERROR_MSG_1 = "网络连接超时，请检查您的网络状态!";
    public static final String RX_NETWORK_ERROR_MSG_2 = "无可用的网络连接，请检查您的网络状态!";
    public static final String RX_NETWORK_ERROR_MSG_3 = "网络连接异常，请检查您的网络状态!";

    public static final String RX_SERVICE_ERROR_MSG = "服务器异常";
    public static final String RX_DATA_ERROR_MSG = "服务器返回数据解析错误";

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public RxException() {
    }

    public RxException(String msg) {
        super(msg);
    }

    public RxException(int code, String msg) {
        super(msg);
        this.code = code;
    }

}
