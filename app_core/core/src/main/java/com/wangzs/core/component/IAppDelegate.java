package com.wangzs.core.component;

import android.content.Context;

import androidx.annotation.Keep;

@Keep
public interface IAppDelegate {

    void onCreate(Context context);

    void onTerminate();

    void onLowMemory();

    void onTrimMemory(int level);
}
