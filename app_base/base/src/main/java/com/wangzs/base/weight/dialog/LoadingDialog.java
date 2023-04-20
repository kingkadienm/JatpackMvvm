package com.wangzs.base.weight.dialog;

import android.content.Context;

import com.blankj.utilcode.util.StringUtils;
import com.wangzs.base.R;


/**
 * 加载中的Dialog
 * Created by wangzs on 2020/11/24.
 */
public class LoadingDialog {

    private Context mContext;
    private AlertDialog mAlertDialog;

    public LoadingDialog(Context context) {
        this.mContext = context;
    }

    public void show() {
        show(null);
    }

    public void show(String tips) {
        show(tips, false);
    }

    public void show(String tips, boolean cannel) {
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(mContext, R.style.dialog_translucent_theme)
                    .setContentView(R.layout.layout_dialog_loading)
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(cannel)
                    .fullWidth()
                    .create();
        }
        if (!StringUtils.isEmpty(tips)) {
            mAlertDialog.setText(R.id.tv_dialog_loading_text, tips);
        } else {
            mAlertDialog.setText(R.id.tv_dialog_loading_text, mContext.getResources().getString(R.string.amicable_view_loading));
        }
        if (!mAlertDialog.isShowing()) {
            mAlertDialog.show();
        }
    }

    public void dismiss() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }

    public boolean isShowing() {
        return mAlertDialog != null && mAlertDialog.isShowing();
    }

    public void release() {
        dismiss();
        mAlertDialog = null;
    }
}