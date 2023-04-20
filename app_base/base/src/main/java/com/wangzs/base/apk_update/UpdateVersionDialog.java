package com.wangzs.base.apk_update;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.wangzs.base.Constants;
import com.wangzs.base.R;
import com.wangzs.base.bean.VersionBean;
import com.wangzs.base.service.ApkDownloadService;
import com.wangzs.base.toolskit.StorageUtils;
import com.wangzs.base.weight.dialog.AlertDialog;

import java.io.File;

/**
 * 版本更新的弹窗
 * Created by wangzs on 2020/10/19.
 */
public class UpdateVersionDialog implements View.OnClickListener {
    private static final String TAG = UpdateVersionDialog.class.getSimpleName();
    private Context mContext;
    private AlertDialog mAlertDialog;
    private TextView mVersionNameTv;
    private LinearLayout mActionBtnLayout;
    private TextView mRightActionBtnTv;
    private TextView mLeftActionBtnTv;
    private TextView mStatusBtnTv;
    private LinearLayout mProgressLayout;
    private TextView mProgressTv;
    private ProgressBar mProgressView;

    private ApkDownloadService.DownloadBinder downloadBinder;
    private VersionBean mShowData;
    private UpdateVersionDialogCallback mCallback;
    // 是否正在下载
    private boolean mIsDownloadApk = false;
    // 是否下载成功
    private boolean mIsDownloadSuccess = false;

    public UpdateVersionDialog(Context context) {
        this.mContext = context;
    }

    public void show(VersionBean bean, UpdateVersionDialogCallback callback) {
        this.mShowData = bean;
        this.mCallback = callback;
        if (mShowData == null) {
            mShowData = new VersionBean();
        }
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(mContext)
                    .setContentView(R.layout.layout_dialog_version_update)
                    .fullWidth()
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .setOnClickListener(R.id.tv_dialog_update_action_btn_left, this)// 暂不更新
                    .setOnClickListener(R.id.tv_dialog_update_action_btn_right, this)// 更新、立即安装
                    .setOnClickListener(R.id.tv_dialog_update_action_btn_status, this)// 状态按钮点击
                    .setOnDismissListener(dialog -> {
                        if (!bean.isForceUpdate()) {
                            if (mIsDownloadApk) {
                                ToastUtils.showShort("已为您转入后台下载应用，稍后进行更新");
                            }
                            if (mCallback != null) {
                                mCallback.onNotUpdateVersion();
                            }
                        }
                    })
                    .create();
            mVersionNameTv = mAlertDialog.getViewById(R.id.tv_dialog_update_version_name);
            mActionBtnLayout = mAlertDialog.getViewById(R.id.layout_dialog_update_action_btn);
            mLeftActionBtnTv = mAlertDialog.getViewById(R.id.tv_dialog_update_action_btn_left);
            mRightActionBtnTv = mAlertDialog.getViewById(R.id.tv_dialog_update_action_btn_right);
            mStatusBtnTv = mAlertDialog.getViewById(R.id.tv_dialog_update_action_btn_status);
            mProgressLayout = mAlertDialog.getViewById(R.id.layout_dialog_update_download_progress);
            mProgressTv = mAlertDialog.getViewById(R.id.tv_dialog_update_download_progress);
            mProgressView = mAlertDialog.getViewById(R.id.pb_dialog_update_download_progress);
        }
        TextView notUpdateTv = mAlertDialog.getViewById(R.id.tv_dialog_update_action_btn_left);
        TextView versionInfoTv = mAlertDialog.getViewById(R.id.tv_dialog_update_version_info);
        mVersionNameTv.setText("版本" + bean.getVersionName());
        mAlertDialog.setCancelable(!bean.isForceUpdate());
        if (bean.isForceUpdate()) {// 强制更新
            notUpdateTv.setVisibility(View.GONE);
        } else {// 非强制更新
            notUpdateTv.setVisibility(View.VISIBLE);
        }
        // 新版本信息
        String updateInfo = bean.getUpdateInfo();
        if (!StringUtils.isEmpty(updateInfo)) {
            updateInfo = updateInfo.replace("\\n", "\n");
        }
        versionInfoTv.setText(updateInfo);
        mAlertDialog.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_dialog_update_action_btn_left) {// 点击暂不更新
            mAlertDialog.dismiss();
        } else if (id == R.id.tv_dialog_update_action_btn_right) {// 点击更新、安装
            if (mIsDownloadSuccess) {// 安装
                ApkInstallUtils.getInstance().installAPKFile(mContext, Constants.FILE_PROVIDER);
            } else {// 点击下载更新
                showDownloadingView();
                downLoadApkFile();
            }
        } else if (id == R.id.tv_dialog_update_action_btn_status) {// 点击重试
            showDownloadingView();
            // 重试
            downloadBinder.retryDownload();
        }
        // TODO 下载中退出的逻辑，勿删
//        else if (id == R.id.tv_dialog_update_download_exit) {// 下载的退出
//            if (mShowData.isForceUpdate()) {
//                mAlertDialog.dismiss();
//                // 强制更新的情况下关闭所有页面
//                ActivityUtils.getInstance().finishAllActivity();
//            } else {
//                // 否则取消Dialog
//                mAlertDialog.dismiss();
//            }
//        }
        // TODO 下载中退出的逻辑，勿删
    }

    private void downLoadApkFile() {
        if (mShowData == null) {
            ToastUtils.showShort("未获取到下载信息，下载失败");
            mAlertDialog.dismiss();
            return;
        }
        mIsDownloadApk = true;
        mIsDownloadSuccess = false;
        StorageUtils.getUpdatePath(mContext, path -> {
            Intent intent = new Intent(mContext, ApkDownloadService.class);
            intent.putExtra(ApkDownloadService.KEY_APK_URL, mShowData.getUpgradeUrl());
            intent.putExtra(ApkDownloadService.KEY_FILE_NAME, getApkFileNameByUrl(mShowData.getUpgradeUrl()));
            intent.putExtra(ApkDownloadService.KEY_FILE_DIR, path);
            mContext.startService(intent);
            mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        });
    }

    private String getApkFileNameByUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return "error.apk";
        }
        String[] temp = url.split("/");
        if (temp.length > 0) {
            return temp[temp.length - 1];
        }
        return "error.apk";
    }

    /**
     * ServiceConnection连接
     */
    protected ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.e(TAG, "=== onServiceDisconnected ===>");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.e(TAG, "=== onServiceConnected ===>");
            downloadBinder = (ApkDownloadService.DownloadBinder) service;
            downloadBinder.addCallback(callback);
            downloadBinder.start();
        }
    };

    /**
     * DownloadService回调结果
     */
    protected ApkDownloadService.ICallbackResult callback = new ApkDownloadService.ICallbackResult() {

        @Override
        public void onProgress(int progress) {
            // 进度回调的地方有时间判断，在这里处理下载中的...
            mProgressLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (mIsDownloadApk) {
                        mProgressView.setProgress(Math.max(progress, 0));
                        mProgressTv.setText(Math.max(progress, 0) + "%");
                    }
                }
            });
        }

        @Override
        public void onSuccess(File file) {
            mIsDownloadApk = false;
            mIsDownloadSuccess = true;
            mProgressLayout.post(new Runnable() {
                @Override
                public void run() {
                    showDownloadSuccessView();
                }
            });
            // 安装Apk
            ApkInstallUtils.getInstance().installAPKFile(mContext, file, Constants.FILE_PROVIDER);
        }

        @Override
        public void onFail(String message) {
            mIsDownloadApk = false;
            mIsDownloadSuccess = false;
            mProgressLayout.post(new Runnable() {
                @Override
                public void run() {
                    showDownloadFailView();
                }
            });
        }
    };

    /**
     * 下载中
     */
    private void showDownloadingView() {
        mIsDownloadApk = true;
        mStatusBtnTv.setVisibility(View.GONE);
        mProgressView.setProgress(0);
        mProgressTv.setText("下载中...");
        mProgressLayout.setVisibility(View.VISIBLE);
        mActionBtnLayout.setVisibility(View.GONE);
    }

    /**
     * 下载失败
     */
    private void showDownloadFailView() {
        mIsDownloadApk = false;
        mIsDownloadSuccess = false;
        mStatusBtnTv.setVisibility(View.VISIBLE);
        mStatusBtnTv.setText(mContext.getString(R.string.download_error_retry));
        mProgressView.setProgress(0);
        mProgressTv.setText("下载失败");
        mProgressLayout.setVisibility(View.GONE);
        mActionBtnLayout.setVisibility(View.GONE);
    }

    /**
     * 下载成功
     */
    private void showDownloadSuccessView() {
        mIsDownloadApk = false;
        mIsDownloadSuccess = true;
        mProgressLayout.setVisibility(View.GONE);
        mProgressTv.setText("下载成功");
        mActionBtnLayout.setVisibility(View.VISIBLE);
        mLeftActionBtnTv.setVisibility(View.GONE);
        mRightActionBtnTv.setText("立即安装");
    }

    public interface UpdateVersionDialogCallback {
        void onNotUpdateVersion();
    }

    public void release() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }
}