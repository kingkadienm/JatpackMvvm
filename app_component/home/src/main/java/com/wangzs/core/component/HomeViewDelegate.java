package com.wangzs.core.component;

import com.wangzs.base.base.fragment.BaseFragment;
import com.wangzs.home.fragment.HomeFragment;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2022-03-12 0:13
 * @Version:
 */
public class HomeViewDelegate implements IViewDelegate {
    @Override
    public BaseFragment newInstance() {
        return HomeFragment.newInstance();
    }
}
