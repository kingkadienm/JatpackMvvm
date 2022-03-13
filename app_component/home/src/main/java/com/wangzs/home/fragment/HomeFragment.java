package com.wangzs.home.fragment;

import android.os.Bundle;

import com.wangzs.base.base.fragment.BaseFragment;
import com.wangzs.core.base.IMainFragment;
import com.wangzs.home.R;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2022-03-12 0:14
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
        return com.wangzs.res.R.drawable.common_tab_index_n;
    }

    @Override
    public int getTabIconCheckRes() {
        return com.wangzs.res.R.drawable.common_tab_index_s;
    }

    @Override
    public int getTabIndex() {
        return IMainFragment.FRAGMENT_HOME_INDEX;
    }

    @Override
    public String getTitleTxt() {
        return "首页";
    }
}
