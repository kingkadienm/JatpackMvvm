package com.wangzs.base.base.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;




/**
 * @Description:
 * @Author: wangzs
 * @Date: 2022-03-10
 * @Version:
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected int getTitleResId() {
        return com.wangzs.res.R.string.app_name;
    }

    protected int getLayoutResId() {
        return 0;
    }


    protected View getContentView() {
        return null;
    }

    protected boolean enableBack() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutResId() != 0) {
            setContentView(getLayoutResId());
        } else if (getContentView() != null) {
            setContentView(getContentView());
        }
        afterSetContentView();

        //标题栏设置
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getTitleResId());
            if (enableBack()) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        initToolbar();
        initView();

    }

    protected void afterSetContentView() {
    }

    protected void initToolbar() {

    }

    protected void setTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected void initView() {

    }

    /**
     * 把状态栏设成透明
     */
    protected void setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            decorView.setOnApplyWindowInsetsListener((v, insets) -> {
                WindowInsets defaultInsets = v.onApplyWindowInsets(insets);
                return defaultInsets.replaceSystemWindowInsets(
                        defaultInsets.getSystemWindowInsetLeft(),
                        0,
                        defaultInsets.getSystemWindowInsetRight(),
                        defaultInsets.getSystemWindowInsetBottom());
            });
            ViewCompat.requestApplyInsets(decorView);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
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
    private  boolean isFastDoubleClick() {
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
}
