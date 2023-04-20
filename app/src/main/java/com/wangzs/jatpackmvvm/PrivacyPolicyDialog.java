package com.wangzs.jatpackmvvm;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.wangzs.base.Constants;
import com.wangzs.base.base.activity.WebViewActivity;
import com.wangzs.base.weight.CashLinkClickTextView;
import com.wangzs.base.weight.dialog.AlertDialog;
import com.wangzs.core.manager.ActivityLifecycleManager;


/**
 * 隐私政策说明的Dialog
 */

public class PrivacyPolicyDialog implements View.OnClickListener {
    private static final String TAG = PrivacyPolicyDialog.class.getSimpleName();
    public static final String PrivacyPolicy = "https://mydata.eovobo.com/v1/privacyPolicy.html";
    public static final String userAgreement = "https://mydata.eovobo.com/v1/userAgreement.html";

    private Context mContext;
    private AlertDialog mAlertDialog;
    private PrivacyPolicyDialogCallback mCallback;

    public PrivacyPolicyDialog(Context context) {
        this.mContext = context;
    }

    public void show(boolean show, PrivacyPolicyDialogCallback callback) {
        this.mCallback = callback;
        // 是否显示过
        if (!show && SPUtils.getInstance().getBoolean(Constants.ISAGREE, false)) {
            if (mCallback != null) {
                mCallback.onComplete();
            }
            return;
        }
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(mContext)
                    .setContentView(R.layout.layout_dialog_privacy_policy)
                    .setOnClickListener(R.id.tv_dialog_privacy_policy_exit, this)
                    .setOnClickListener(R.id.tv_dialog_privacy_policy_agree, this)
                    .fullWidth()
                    .setCanceledOnTouchOutside(false)
                    .setCancelable(false)
                    .create();
        }
        CashLinkClickTextView contentTv = mAlertDialog.getViewById(R.id.tv_dialog_privacy_policy_content);
        contentTv.setContentText(mContext.getString(R.string.PrivacyPolicyExplain));
        contentTv.setCallback(new MyLinkClickCallback());
        // 显示
        mAlertDialog.show();
    }

    /**
     * 链接文字点击监听
     */
    private class MyLinkClickCallback implements CashLinkClickTextView.LinkClickCallback {

        @Override
        public void onClickLink(String linkValue) {

            Intent intent = new Intent(mContext, WebViewActivity.class);
            if ("userAgreement".equals(linkValue)) {// 用户协议
                intent.putExtra(WebViewActivity.KEY_TITLEL, mContext.getString(R.string.str_userAgreement));
                intent.putExtra(WebViewActivity.KEY_URL, userAgreement);
                mContext.startActivity(intent);

            } else if ("privacyPolicy".equals(linkValue)) {// 隐私政策
                intent.putExtra(WebViewActivity.KEY_TITLEL, mContext.getString(R.string.str_privacyPolicy));
                intent.putExtra(WebViewActivity.KEY_URL, PrivacyPolicy);
                mContext.startActivity(intent);

            }

        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        mAlertDialog.dismiss();
        if (id == R.id.tv_dialog_privacy_policy_exit) {// 暂不使用
            LogUtils.e(TAG, "========点击暂不使用=========》");
            // 部分机型会有onDestroy回调不及时的情况，华为某机型偶现Toast不能取消的情况，所以在退出应用的时候直接取消Toast
            ToastUtils.cancel();
            // 目前是关闭所有Activity
            ActivityLifecycleManager.getInstance().finishAllActivity();

        } else if (id == R.id.tv_dialog_privacy_policy_agree) {// 同意并继续使用
            // 保存已经展示过的状态
            SPUtils.getInstance().put(Constants.ISAGREE, true);
            // 发通知
            if (mCallback != null) {
                mCallback.onComplete();
            }
        }
    }

    public interface PrivacyPolicyDialogCallback {
        void onComplete();
    }

    public void release() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }
}
