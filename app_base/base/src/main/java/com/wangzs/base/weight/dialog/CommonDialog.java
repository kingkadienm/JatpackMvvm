package com.wangzs.base.weight.dialog;

import android.content.Context;
import android.content.DialogInterface;

/**
 * 通用Dialog，抽取一些通用方法
 * Created by wangzs on 2021/4/9.
 */
public abstract class CommonDialog {
    protected Context mContext;
    protected AlertDialog mAlertDialog;

    public CommonDialog(Context context) {
        this.mContext = context;
        if (mAlertDialog == null) {
            if (fromBottom()) {
                mAlertDialog = new AlertDialog.Builder(mContext)
                        .setContentView(getContentView())
                        .fullWidth()
                        .fromBottom(true)
                        .create();
            } else {
                mAlertDialog = new AlertDialog.Builder(mContext)
                        .setContentView(getContentView())
                        .fullWidth()
                        .create();
            }
        }
    }

    protected boolean fromBottom() {
        return false;
    }

    public void show() {
        if (mAlertDialog != null) {
            mAlertDialog.show();
        }
    }

    public void dismiss() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
    }

    protected boolean isShowing() {
        if (mAlertDialog != null) {
            return mAlertDialog.isShowing();
        }
        return false;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        if (mAlertDialog != null) {
            mAlertDialog.setOnDismissListener(listener);
        }
    }

    public void release() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }

    protected abstract int getContentView();
}