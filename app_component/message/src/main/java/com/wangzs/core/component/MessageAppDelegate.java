package com.wangzs.core.component;

import android.content.Context;

import com.wangzs.message.router.MessageServiceImpl;
import com.wangzs.router.RouterHelper;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2022-03-12 0:24
 * @Version:
 */
public class MessageAppDelegate implements IAppDelegate {
    @Override
    public void onCreate(Context context) {
        RouterHelper.getService().setMessageService(new MessageServiceImpl());
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
