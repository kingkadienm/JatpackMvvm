package com.wangzs.core.component;

import com.wangzs.account.fragment.AccountFragment;
import com.wangzs.base.base.fragment.BaseFragment;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2022-03-11 12:08
 * @Version:
 */
public class AccountViewDelegate implements IViewDelegate {
    @Override
    public BaseFragment newInstance() {
        return AccountFragment.newInstance();
    }
}
