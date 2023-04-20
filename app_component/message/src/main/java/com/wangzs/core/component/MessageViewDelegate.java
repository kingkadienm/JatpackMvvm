package com.wangzs.core.component;

import com.wangzs.base.base.fragment.BaseFragment;
import com.wangzs.message.fragment.MessageFragment;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2020-03-12 0:23
 * @Version:
 */
public class MessageViewDelegate implements IViewDelegate {
    @Override
    public BaseFragment newInstance() {
        return MessageFragment.newInstance();
    }
}
