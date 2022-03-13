package com.wangzs.core.component;

import com.wangzs.base.base.fragment.BaseFragment;
import com.wangzs.contact.fragment.ContactFragment;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2022-03-12 0:13
 * @Version:
 */
public class ContactViewDelegate implements IViewDelegate {
    @Override
    public BaseFragment newInstance() {
        return ContactFragment.newInstance();
    }
}
