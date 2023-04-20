package com.wangzs.core.network.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2020-03-10
 * @Version:
 */
public class ResultBody {

    public static final String RX_CODE = "error"; // 错误代码 200  404  401  502
    public static final String RX_MSG = "message";
    public static final String RX_RESULT = "result"; // 数据

    public int mCode;

    public String mMsg;

    public Object mData;

    public static ResultBody convert(JSONObject jsonObject) {

        ResultBody rxResult = new ResultBody();
        if (jsonObject != null) {
            rxResult.mCode = jsonObject.optInt(RX_CODE);
            rxResult.mMsg = jsonObject.optString(RX_MSG);
            if (jsonObject.has(RX_RESULT)) {
                rxResult.mData = jsonObject.optString(RX_RESULT);
            }
        }
        return rxResult;
    }

    @Override
    public String toString() {
        return "{code:" + mCode + ",msg:" + mMsg + ",result:" + mData + "}";
    }
}
