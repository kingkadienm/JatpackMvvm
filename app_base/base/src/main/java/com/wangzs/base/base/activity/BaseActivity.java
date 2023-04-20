package com.wangzs.base.base.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import com.wangzs.base.R;
import com.wangzs.base.weight.NavigationBar;
import com.wangzs.base.weight.dialog.LoadingDialog;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * @Description:
 * @Author: wangzs
 * @Date: 2020-03-10
 * @Version:
 */

public abstract class BaseActivity/*<T extends ViewBinding>*/ extends RxAppCompatActivity {

    public final String TAG = this.getClass().getSimpleName();

    protected static final int CHANGE_LANGUAGE = 101;

    protected FragmentActivity mContext;
    // TODO: 2022/4/29 029 暂时注释 后续修改 因为布局会重叠
//    protected PageLoadView mLoadView;
    protected NavigationBar mTitleView;
//    protected T binding;

    protected int getTitleResId() {
        return 0;
    }

    protected int getLayoutResId() {
        return 0;
    }


    protected View getContentView() {
        return null;
    }

    protected boolean isUserViewBinding() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        if (getLayoutResId() != 0) {
            setContentView(getLayoutResId());
        } else if (getContentView() != null) {
            setContentView(getContentView());
        }
//        if (isUserViewBinding()) {
//            Type type = getClass().getGenericSuperclass();
//            if (type instanceof ParameterizedType) {
//                try {
//                    Class<T> clazz = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
//                    Method method = clazz.getMethod("inflate", LayoutInflater.class);
//                    binding = (T) method.invoke(null, getLayoutInflater());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (binding != null) {
//                    setContentView(binding.getRoot());
//                }
//            }
//        }

        afterSetContentView();
//        if (getLoadingContainer() != null) {
//            mLoadView = new PageLoadView(mContext);
//        }


        initToolbar();
        initView();
        initData();

    }

//    protected void showLoadView() {
//        if (mLoadView != null && getLoadingContainer() != null) {
//            mLoadView.setContainerLayout(getLoadingContainer());
//        }
//    }
//
//    protected void dismissLoadView() {
//        if (mLoadView != null && getLoadingContainer() != null) {
//            mLoadView.dismiss();
//        }
//    }


    protected void initData() {

    }

    protected void afterSetContentView() {
    }

    private LoadingDialog mLoadingDialog;


    protected void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mContext);
        }
        mLoadingDialog.show();
    }

    protected void showLoading(String tips) {
        showLoading(tips, false);
    }

    protected void showLoading(String tips, boolean cancel) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mContext);
        }
        mLoadingDialog.show(tips, cancel);
    }

    protected void dismissLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.release();
            mLoadingDialog = null;
        }
    }

    protected void initToolbar() {
//        BarUtils.setNavBarColor(this, Color.WHITE);
        BarUtils.setNavBarColor(this, Color.TRANSPARENT);
        BarUtils.setNavBarLightMode(this,true);
        mTitleView = new NavigationBar(mContext);
        if (showTitle()) {
            mTitleView.initTitleBar();
            // 关于这里的解释，去查看setContentView的源码解析.txt
            ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
            ViewGroup parentView = (ViewGroup) viewGroup.getChildAt(0);
            if (parentView != null) {
                mTitleView.setTitle(getTitleStr());
                if (!StringUtils.isEmpty(getRight())) {
                    mTitleView.setRightText(getRight());
//                    mTitleView.setRightClickListener(getRightClickListener());
                } else if (getRightTextView() != null) {
                    mTitleView.setRightText(getRightTextView());
                    mTitleView.setRightClickListener(getRightClickListener(getRightTextView()));
                }

                mTitleView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                parentView.addView(mTitleView, 0);
            }
        }
    }


    protected void initView() {

    }

    protected View.OnClickListener getRightClickListener(TextView textView) {
        return null;
    }


    /**
     * 加载布局的父布局
     */
    protected ViewGroup getLoadingContainer() {
        return null;
    }

    /**
     * 是否自动创建标题栏
     */
    protected boolean showTitle() {
        return false;
    }


    /**
     * 标题栏右边的文字
     *
     * @return
     */
    protected String getRight() {
        return null;
    }

    protected TextView getRightTextView() {
        return null;
    }

    /**
     * 标题栏文字
     */
    protected String getTitleStr() {
        return null;
    }

    protected void setTitle(String title) {
        if (mTitleView != null) {
            ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
            ViewGroup parentView = (ViewGroup) viewGroup.getChildAt(0);
            if (parentView != null) {
                mTitleView.setTitle(title);
                mTitleView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                parentView.addView(mTitleView, 0);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isFastDoubleClick()) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //开始点击的系统时间
    private long lastClickTime = System.currentTimeMillis();

    private boolean isFastDoubleClick() {
        //第二次点击的系统时间
        long time = System.currentTimeMillis();
        //记录时间戳
        long timeD = time - lastClickTime;
        if (timeD >= 0 && timeD <= 300) {
            return true;
        } else {
            lastClickTime = time;
            return false;
        }
    }

    @Override
    protected void onDestroy() {
//        if (mLoadView != null) {
//            mLoadView.release();
//            mLoadView = null;
//        }
        ToastUtils.cancel();
        dismissLoading();
//        binding = null;
        super.onDestroy();
    }

}
