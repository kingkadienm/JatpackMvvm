package com.wangzs.core.network.bean;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2020-03-10
 * @Version:
 */
public class ResultBody<T> {

    public static final String RX_CODE = "error"; // 错误代码 200  404  401  502
    public static final String RX_MSG = "message";
    public static final String RX_RESULT = "result"; // 数据

    @SerializedName("error")
    public int mCode;
    @SerializedName("message")
    public String mMsg;
    @SerializedName("result")
    public T mData;

    public int getmCode() {
        return mCode;
    }

    public void setmCode(int mCode) {
        this.mCode = mCode;
    }

    public String getmMsg() {
        return mMsg;
    }

    public void setmMsg(String mMsg) {
        this.mMsg = mMsg;
    }

    public T getmData() {
        return mData;
    }

    public void setmData(T mData) {
        this.mData = mData;
    }

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
