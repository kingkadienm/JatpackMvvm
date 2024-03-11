package com.wangzs.jatpackmvvm;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDex;

import com.wangzs.base.toolskit.LogUtil;
import com.wangzs.core.CoreApplication;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2020-03-11 15:23
 * @Version:
 */
public class AppApplication extends CoreApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.setLogEnabled(BuildConfig.DEBUG);



    }
}
