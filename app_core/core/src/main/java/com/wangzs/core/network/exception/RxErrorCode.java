package com.wangzs.core.network.exception;


/**
 * @Description:
 * @Author: wangzs
 * @Date: 2020-03-10
 * @Version:
 */
public class RxErrorCode {

    /**
     * 请求数据成功
     */
    public static final int CODE_SUCCESS = 0;

    /**
     * 跳转登录
     */
    public static final int CODE_LOGIN = 302;


    /**
     * 客户端缺少参数
     */
    public static final int CODE_400 = 400;


    /**
     * session 正常过期
     */
    public static final int CODE_RE_LOGIN_1 = 401;
    /**
     * 强制退出
     */
    public static final int CODE_RE_LOGIN_2 = 402;
}
