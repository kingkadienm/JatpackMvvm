package com.wangzs.core.component;

import android.content.Context;

import com.wangzs.account.router.AccountServiceImpl;
import com.wangzs.router.RouterHelper;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2020-03-11 12:08
 * @Version:
 */
public class AccountAppDelegate implements IAppDelegate {

    @Override
    public void onCreate(Context context) {
        RouterHelper.getService().setAccountService(new AccountServiceImpl());
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
