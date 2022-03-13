package com.wangzs.account.fragment;

import com.wangzs.base.base.fragment.BaseFragment;
import com.wangzs.core.base.IMainFragment;
import com.wangzs.account.R;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2022-03-11 12:25
 * @Version:
 */
public class AccountFragment extends BaseFragment implements IMainFragment {

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.account_fragment_account;
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
        return IMainFragment.FRAGMENT_ACCOUNT_INDEX;
    }

    @Override
    public String getTitleTxt() {
        return "我的";
    }
}