package com.wangzs.message.fragment;

import android.os.Bundle;

import com.wangzs.base.base.fragment.BaseFragment;
import com.wangzs.core.base.IMainFragment;
import com.wangzs.message.R;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2020-03-12 0:26
 * @Version:
 */
public class MessageFragment extends BaseFragment implements IMainFragment {

    public static MessageFragment newInstance() {

        Bundle args = new Bundle();

        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.message_fragment_message;
    }

    @Override
    public int getTabIconRes() {
        return  R.drawable.common_tab_message_n;

    }

    @Override
    public int getTabIconCheckRes() {
        return  R.drawable.common_tab_message_s;
    }

    @Override
    public int getTabIndex() {
        return IMainFragment.FRAGMENT_MESSAGE_INDEX;
    }

    @Override
    public String getTitleTxt() {
        return "消息";
    }
}
