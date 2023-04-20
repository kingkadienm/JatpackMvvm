package com.wangzs.core.manager;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


import com.blankj.utilcode.util.LanguageUtils;
import com.blankj.utilcode.util.LogUtils;

import java.util.Locale;
import java.util.Stack;


/**
 * @Description:
 * @Author: wangzs
 * @Date: 2020-03-10
 * @Version:
 */
public final class ActivityLifecycleManager {

    private static ActivityLifecycleManager sInstance;

    private ActivityLifecycleManager() {
        mStoreAll = new Stack<>();
    }

    public static ActivityLifecycleManager getInstance() {
        if (sInstance == null) {
            synchronized (ActivityLifecycleManager.class) {
                if (sInstance == null) {
                    sInstance = new ActivityLifecycleManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * activity 活跃数量
     */
    private int mActiveCount = 0;
    /**
     * 应用是否在后台运行
     */
    private boolean mIsRunInBackground = false;
    /**
     * 使用应用的时间
     */
    private long mUseAppTime = 0L;
    /**
     * 存活的 activity 栈
     */
    private Stack<Activity> mStoreAll;

    public void register(Application application) {

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//                if(activity instanceof DetailActivity) {
//                    if(store.size() >= MAX_ACTIVITY_DETAIL_NUM){
//                        store.peek().finish(); //移除栈底的详情页并finish,保证商品详情页个数最大不超过指定
//                    }
//                    store.add((ActivityDetail) activity);
//                }
                mStoreAll.add(activity);
//                LanguageUtils.getInstance().applyLanguage(activity);
//                LanguageUtils.updateAppContextLanguage();
            }

            @Override
            public void onActivityStarted(Activity activity) {
                mActiveCount++;
                if (mIsRunInBackground) {
                    // 应用从后台回到前台 需要做的操作
                    back2App(activity);
                    mUseAppTime = System.currentTimeMillis();
                } else if (mActiveCount == 1) {
                    startApp(activity);
                    mUseAppTime = System.currentTimeMillis();
                } else {
                    startActivity(activity);
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                mActiveCount--;
                if (mActiveCount == 0) {
                    // 应用进入后台 需要做的操作
                    leaveApp(activity);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                mStoreAll.remove(activity);
            }
        });
    }

    /**
     * 获取栈中存在的 activity 数量
     *
     * @return
     */
    public int getAllActivityCount() {
        return mStoreAll.size();
    }

    /**
     * 获取位于栈顶，最后打开的 activity
     *
     * @return
     */
    public Activity getTopActivity() {
        return mStoreAll.lastElement();
    }

    /**
     * 启动了一个activity
     *
     * @param activity
     */
    private void startActivity(Activity activity) {
        LogUtils.i("start activity:" + activity.getClass().getName());
    }

    /**
     * 应用冷启动
     */
    private void startApp(Activity activity) {
        LogUtils.i("start " + activity.getClass().getName());
    }

    /**
     * 应用热启动
     *
     * @param activity
     */
    private void back2App(Activity activity) {
        mIsRunInBackground = false;
        LogUtils.i("back to app");
    }

    /**
     * 离开应用 压入后台或者退出应用
     *
     * @param activity
     */
    private void leaveApp(Activity activity) {
        mIsRunInBackground = true;
        LogUtils.i("leave app");
    }

    public void finishAllActivity() {
        if (mStoreAll != null) {
            for (Activity activity : mStoreAll) {
                activity.finish();
            }
            mStoreAll.clear();
        }
    }
}
