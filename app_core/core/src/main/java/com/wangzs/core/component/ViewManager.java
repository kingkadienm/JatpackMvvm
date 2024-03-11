package com.wangzs.core.component;

import android.content.Context;


import androidx.fragment.app.Fragment;

import com.wangzs.base.base.fragment.BaseFragment;
import com.wangzs.base.toolskit.LogUtil;
import com.wangzs.core.base.IMainFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class ViewManager {

    private static Map<Integer, BaseFragment> sFragmentList = new TreeMap<>();

    public static ViewManager getInstance() {
        return ViewManagerHolder.sInstance;
    }

    private static class ViewManagerHolder {
        private static final ViewManager sInstance = new ViewManager();
    }

    private ViewManager() {
    }

    public List<Fragment> getAllFragment(Context context) {

        List<IViewDelegate> viewDelegates = ClassUtils.getObjectsWithInterface(context, IViewDelegate.class, "com.wangzs.core.component");
        for (IViewDelegate viewDelegate : viewDelegates) {
            BaseFragment fragment = viewDelegate.newInstance();

            int tabIndex = 0;
            if (fragment instanceof IMainFragment) {
                tabIndex = ((IMainFragment) fragment).getTabIndex();

                LogUtil.i("load frgment index:" + tabIndex + "," + fragment.getClass().getName());
            }
            sFragmentList.put(tabIndex, fragment);
        }

        if (sFragmentList.size() > 0) {
            List<Fragment> list = new ArrayList<>();
            for (Integer key : sFragmentList.keySet()) {
                list.add(sFragmentList.get(key));
            }
            return list;
        }
        return null;
    }
}
