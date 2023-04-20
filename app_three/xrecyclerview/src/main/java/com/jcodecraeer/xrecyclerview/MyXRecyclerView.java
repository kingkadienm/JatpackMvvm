package com.jcodecraeer.xrecyclerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.DefaultItemAnimator;

/**
 * @Description
 * @Date 2022/4/27 027 14:52
 * @Created by wangzs
 */
public class MyXRecyclerView extends XRecyclerView {
    public MyXRecyclerView(Context context) {
        this(context, null);
    }

    public MyXRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyXRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setHasFixedSize(true);
        setItemAnimator(new DefaultItemAnimator());
        setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
    }
}
