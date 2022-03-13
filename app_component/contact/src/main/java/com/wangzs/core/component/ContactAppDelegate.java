package com.wangzs.core.component;

import android.content.Context;

import com.wangzs.contact.router.ContactServiceImpl;
import com.wangzs.router.RouterHelper;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2022-03-12 0:07
 * @Version:
 */
public class ContactAppDelegate implements IAppDelegate {
    @Override
    public void onCreate(Context context) {
        RouterHelper.getService().setContactService(new ContactServiceImpl());
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
