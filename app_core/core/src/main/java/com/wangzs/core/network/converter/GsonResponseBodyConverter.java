package com.wangzs.core.network.converter;


import android.util.Log;

import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.wangzs.base.toolskit.LogUtil;
import com.wangzs.core.network.bean.ResultBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.ParameterizedType;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String string = value.string();
        LogUtil.e("response convert " + string);
          ResultBody baseResponse = gson.fromJson(string, ResultBody.class);
        LogUtil.e("-----"+baseResponse.getmData().getClass());
        try {
            return adapter.fromJson(string);
        } finally {
            value.close();
        }


    }
}
