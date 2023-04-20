package com.wangzs.home.fragment;

import android.os.Bundle;

import com.wangzs.base.apk_update.UpdateVersionDialog;
import com.wangzs.base.base.fragment.BaseFragment;
import com.wangzs.base.bean.VersionBean;
import com.wangzs.core.base.IMainFragment;
import com.wangzs.core.checkVersion.UpdateUtils;
import com.wangzs.home.R;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2020-03-12 0:14
 * @Version:
 */
public class HomeFragment extends BaseFragment implements IMainFragment {


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.home_fragment_home;
    }

    @Override
    public int getTabIconRes() {
        return R.drawable.common_tab_me_n;
    }

    @Override
    public int getTabIconCheckRes() {
        return R.drawable.common_tab_me_s;
    }

    @Override
    public int getTabIndex() {
        return IMainFragment.FRAGMENT_HOME_INDEX;
    }

    @Override
    public String getTitleTxt() {
        return "首页";
    }

    @Override
    protected void initView() {
        super.initView();
//        UpdateUtils updateUtils = new UpdateUtils(context);
//        updateUtils.checkVersion(new UpdateUtils.CheckVersionCallback() {
//            @Override
//            public void onNotUpdateVersion() {
//
//            }
//        });
    }
}
