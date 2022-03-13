package com.wangzs.core;

import com.alibaba.android.arouter.launcher.ARouter;
import com.wangzs.base.BaseApplication;
import com.wangzs.core.component.ClassUtils;
import com.wangzs.core.component.IAppDelegate;
import com.wangzs.core.manager.ActivityLifecycleManager;

import java.util.List;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2022-03-10
 * @Version:
 */

public class CoreApplication extends BaseApplication {

    public static final String COMPONENT_PACKAGE = "com.wangzs.core.component";

    private List<IAppDelegate> mAppDelegates;

    @Override
    public void onCreate() {
        super.onCreate();

        ARouter.openLog();
        ARouter.openDebug();
        ARouter.init(this);
        ActivityLifecycleManager.getInstance().register(this);
        mAppDelegates = ClassUtils.getObjectsWithInterface(this, IAppDelegate.class, COMPONENT_PACKAGE);
        for (IAppDelegate delegate : mAppDelegates) {
            delegate.onCreate(this);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        for (IAppDelegate delegate : mAppDelegates) {
            delegate.onTerminate();
        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        for (IAppDelegate delegate : mAppDelegates) {
            delegate.onLowMemory();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        for (IAppDelegate delegate : mAppDelegates) {
            delegate.onTrimMemory(level);
        }
    }
}
