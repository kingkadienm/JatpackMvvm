package com.wangzs.core.component;

import android.content.Context;

import com.wangzs.home.router.HomeServiceImpl;
import com.wangzs.router.RouterHelper;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2020-03-12 0:07
 * @Version:
 */
public class HomeAppDelegate implements IAppDelegate {
    @Override
    public void onCreate(Context context) {
        RouterHelper.getService().setHomeService(new HomeServiceImpl());
    }

    @Override
    public void onTerminate() {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onTrimMemory(int level) {

    }
}
