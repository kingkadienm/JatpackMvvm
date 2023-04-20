package com.wangzs.account.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ToastUtils;
import com.wangzs.account.R;
import com.wangzs.base.base.activity.BaseActivity;
import com.wangzs.core.checkVersion.UpdateUtils;
import com.wangzs.router.RouterHelper;

@Route(path = RouterHelper.Account.LOGIN_ACTIVITY)
public class LoginActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    public void netTest(View view) {
//        new UpdateUtils(mContext).checkVersion(new UpdateUtils.CheckVersionCallback() {
//            @Override
//            public void onNotUpdateVersion() {
//
//            }
//        });
        ToastUtils make = ToastUtils.make();
        make.setBgColor(Color.RED).setLeftIcon(com.wangzs.res.R.mipmap.ic_launcher);
        make.show("11111111");
//        ToastUtils.showLong("12");
    }
}