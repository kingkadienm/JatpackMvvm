package com.wangzs.base.toolskit;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * 键盘工具类
 */
public class KeyboardListener implements ViewTreeObserver.OnGlobalLayoutListener {

    private View mContentView;

    // 状态栏的高度
    private int statusBarHeight;
    // 软键盘的高度
    private int keyboardHeight;
    // 底部虚拟按键的高度
    private int softButtonsBarHeight;
    // 软键盘的显示状态
    private boolean isShowKeyboard;
    /**
     * 键透事件回调
     */
    private KeyBoardListener mKeyBoardListener;

    /**
     * @param keyBoardListener
     */
    public void setKeyBoardListener(KeyBoardListener keyBoardListener) {
        this.mKeyBoardListener = keyBoardListener;
    }

    public interface KeyBoardListener {
        /**
         * 打开键盘
         *
         * @param keyboardHeight
         */
        void onShowKeyboard(int keyboardHeight);

        /**
         * 收起键盘
         */
        void onHideKeyboard();
    }

    /**
     * @param contextObj
     */
    public KeyboardListener(Activity contextObj) {
        mContentView = contextObj.findViewById(android.R.id.content);
        mContentView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        statusBarHeight = KeyboardHelper.getStatusBarHeight(contextObj);
        softButtonsBarHeight = KeyboardHelper.getSoftButtonsBarHeight(contextObj);
    }

    @Override
    public void onGlobalLayout() {
        // 应用可以显示的区域。此处包括应用占用的区域，
        // 以及ActionBar和状态栏，可能会包含设备底部的虚拟按键。
        Rect r = new Rect();
        mContentView.getWindowVisibleDisplayFrame(r);

        // 屏幕高度
        int screenHeight = mContentView.getRootView().getHeight();

        int heightDiff = screenHeight - (r.bottom - r.top);

        // 在不显示软键盘时，heightDiff等于 状态栏 + 虚拟按键 的高度
        // 在显示软键盘时，heightDiff会变大，等于 软键盘 + 状态栏 + 虚拟按键 的高度。
        // 所以heightDiff大于 状态栏 + 虚拟按键 高度时表示软键盘出现了，
        // 这时可算出软键盘的高度，即heightDiff减去 状态栏 + 虚拟按键 的高度
        if (keyboardHeight == 0 && heightDiff > statusBarHeight + softButtonsBarHeight) {
            keyboardHeight = heightDiff - statusBarHeight - softButtonsBarHeight;
        }

        if (isShowKeyboard) {
            // 如果软键盘是弹出的状态，并且heightDiff小于等于 状态栏 + 虚拟按键 高度，
            // 说明这时软键盘已经收起
            if (heightDiff <= statusBarHeight + softButtonsBarHeight) {
                isShowKeyboard = false;
                if (mKeyBoardListener != null) {
                    mKeyBoardListener.onHideKeyboard();
                }
            }
        } else {
            // 如果软键盘是收起的状态，并且heightDiff大于 状态栏 + 虚拟按键 高度，
            // 说明这时软键盘已经弹出
            if (heightDiff > statusBarHeight + softButtonsBarHeight) {
                isShowKeyboard = true;
                if (mKeyBoardListener != null) {
                    mKeyBoardListener.onShowKeyboard(keyboardHeight);
                }
            }
        }
    }

    /**
     * 释放监听
     */
    public void removeListener() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            mContentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        } else {
            mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }
}
