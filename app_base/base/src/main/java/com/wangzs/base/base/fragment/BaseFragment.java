package com.wangzs.base.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.wangzs.base.toolskit.LogUtil;


/**
 * @Description:
 * @Author: wangzs
 * @Date: 2020-03-10
 * @Version:
 */
public abstract class BaseFragment extends Fragment {

    protected FragmentActivity context;
    private static final String TAG = "BaseFragment";

    private View mRootView;

    private boolean mIsInitData;

    private boolean mUserVisible = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutResId(), container, false);
            initView();
        }
        String simpleName = getClass().getSimpleName();
        LogUtil.d(TAG, simpleName + " onCreateView");
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLazyLoad()) {
            fetchData();
        }
        String simpleName = getClass().getSimpleName();
        LogUtil.d(TAG, simpleName + " onViewCreated");
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData();
        // 当前Fragment用户可见
        mUserVisible = true;
        String simpleName = getClass().getSimpleName();
        LogUtil.d(TAG, simpleName + " onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        // 当前Fragment用户不可见
        mUserVisible = false;
        String simpleName = getClass().getSimpleName();
        LogUtil.d(TAG, simpleName + " onPause");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        String simpleName = getClass().getSimpleName();
        LogUtil.d(TAG, simpleName + " onHiddenChanged " + hidden);
    }

    private void fetchData() {
        if (mIsInitData)
            return;
        initData();
        mIsInitData = true;
        String simpleName = getClass().getSimpleName();
        LogUtil.d(TAG, simpleName + " fetchData");
    }

    public <T extends View> T findViewById(@IdRes int id) {
        return mRootView.findViewById(id);
    }

    protected abstract int getLayoutResId();

    protected void initView() {
        String simpleName = getClass().getSimpleName();
        LogUtil.d(TAG, simpleName + " initView");
    }

    protected void initData() {
        String simpleName = getClass().getSimpleName();
        LogUtil.d(TAG, simpleName + " initData");
    }

    /**
     * 是否懒加载
     */
    protected boolean isLazyLoad() {
        return false;
    }

    /**
     * 当前Fragment是否可见（用户在手机屏幕看得见？）
     */
    protected boolean isCurFragmentVisible() {
        return mUserVisible;
    }
}
