package com.wangzs.contact.fragment;

import com.wangzs.base.base.fragment.BaseFragment;
import com.wangzs.contact.R;
import com.wangzs.core.base.IMainFragment;


/**
 * @Description:
 * @Author: wangzs
 * @Date: 2020-03-12 0:14
 * @Version:
 */
public class ContactFragment extends BaseFragment implements IMainFragment {


    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.contact_fragment_contact;
    }

    @Override
    public int getTabIconRes() {
        return  R.drawable.common_tab_discover_n;
    }

    @Override
    public int getTabIconCheckRes() {
        return  R.drawable.common_tab_discover_s;
    }

    @Override
    public int getTabIndex() {
        return IMainFragment.FRAGMENT_CONTACT_INDEX;
    }

    @Override
    public String getTitleTxt() {
        return "舞圈";
    }
}
