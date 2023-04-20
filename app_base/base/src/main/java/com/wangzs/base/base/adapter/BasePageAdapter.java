package com.wangzs.base.base.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

/**
 * @Description
 * @Date 2022/4/27 027 18:47
 * @Created by wangzs
 */
public class BasePageAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    public BasePageAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, T t) {

    }
}
