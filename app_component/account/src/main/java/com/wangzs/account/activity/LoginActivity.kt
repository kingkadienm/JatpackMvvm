package com.wangzs.account.activity

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.wangzs.account.R
import com.wangzs.base.base.activity.BaseActivity
import com.wangzs.router.RouterHelper

@Route(path = RouterHelper.Account.LOGIN_ACTIVITY)
class LoginActivity : BaseActivity() {
    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        super.initView()
    }

    override fun initData() {
        super.initData()
    }

    fun netTest(view: View?) {
//        UpdateUtils(mContext).checkVersion { }
        RouterHelper.getActivity(RouterHelper.App.MAIN_ACTIVITY).navigation()

    }
}