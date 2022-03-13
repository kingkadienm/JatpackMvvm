package com.wangzs.home;

import androidx.fragment.app.FragmentManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.wangzs.base.base.activity.BaseActivity;
import com.wangzs.home.fragment.HomeFragment;
import com.wangzs.router.RouterHelper;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2022-03-12 0:04
 * @Version:
 */
@Route(path=RouterHelper.Home.HOME_ACTIVITY)
public class HomeActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.home_activity_home;
    }

    @Override
    protected void afterSetContentView() {
        ARouter.getInstance().inject(this);
    }

    @Override
    protected void initView() {
        HomeFragment homeFragment = HomeFragment.newInstance();
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction().add(R.id.home_root_view, homeFragment)
                .commit();
    }
}
