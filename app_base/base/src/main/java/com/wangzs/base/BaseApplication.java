package com.wangzs.base;


import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.wangzs.base.toolskit.LogUtil;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2020-03-10
 * @Version:
 */
public abstract class BaseApplication extends Application {

    private static Context sContext;

    public boolean isDebug = false;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();

        if (AppGlobal.isDebug = isAppDebug()) {
            LogUtil.setLogEnabled(isDebug);
        }

     }

    /**
     * 判断App是否是Debug版本.
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    private boolean isAppDebug() {
        if (TextUtils.isEmpty(this.getPackageName())) return false;
        try {
            PackageManager pm = this.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(this.getPackageName(), 0);
            isDebug = ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            return isDebug;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
