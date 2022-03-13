package com.wangzs.account;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.wangzs.account.fragment.AccountFragment;
import com.wangzs.base.base.activity.BaseActivity;
import com.wangzs.router.RouterHelper;

import kotlin.jvm.JvmField;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2022-03-11 15:27
 * @Version:
 */
@Route(path = RouterHelper.Account.ACCOUNT_ACTIVITY)
public class AccountActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.account_activity_account;
    }

    @Override
    protected void afterSetContentView() {
        ARouter.getInstance().inject(this);
    }

    @Override
    protected void initView() {
        AccountFragment accountFragment = AccountFragment.newInstance();

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction().add(R.id.account_root_view, accountFragment)
                .commit();
    }
}
