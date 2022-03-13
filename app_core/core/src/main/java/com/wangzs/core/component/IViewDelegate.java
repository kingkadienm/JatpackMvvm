package com.wangzs.core.component;

import androidx.annotation.Keep;

import com.wangzs.base.base.fragment.BaseFragment;


@Keep
public interface IViewDelegate {

    BaseFragment newInstance();
}
