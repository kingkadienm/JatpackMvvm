package com.wangzs.core.checkVersion;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.wangzs.base.R;
import com.wangzs.base.apk_update.UpdateVersionDialog;
import com.wangzs.base.bean.VersionBean;
import com.wangzs.base.weight.dialog.AlertDialog;
import com.wangzs.base.weight.dialog.LoadingDialog;
import com.wangzs.core.api.CoreApi;
import com.wangzs.core.manager.ActivityLifecycleManager;
import com.wangzs.core.network.RxRetry;
import com.wangzs.core.network.RxSchedulers;
import com.wangzs.core.network.bean.RxResult;
import com.wangzs.core.network.bean.RxStatus;
import com.wangzs.core.network.livedata.LiveDataObservableAdapter;
import com.wangzs.router.RouterHelper;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;


/**
 * 版本更新工具类
 * Created by wangzs on 2020/10/19.
 */
public class UpdateUtils {
    private static final String TAG = UpdateUtils.class.getSimpleName();

    private FragmentActivity mContext;
    private UpdateVersionDialog mUpdateVersionDialog;
    private int mCurrentRequestCount;
    private AlertDialog mErrorDialog;// 检查更新失败的弹窗
    private LoadingDialog mLoadingDialog;

    private static final int MAX_REQUEST_COUNT = 3;

    public UpdateUtils(FragmentActivity context) {
        this.mContext = context;
    }

    public void checkVersion(CheckVersionCallback callback) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mContext);
        }
        mLoadingDialog.show();
        requestVersionInfo(new DefaultHttpCallback() {
            @Override
            public void onResponseSuccess(VersionBean data) {
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }

                int versionCode = data == null ? AppUtils.getAppVersionCode() : data.getVersionCode();
//                int localVersionCode = MMKV.defaultMMKV().getInt(Constants.KEY_NEW_VERSION_CODE, -1);
                // 非强制更新，只显示一次
                if (versionCode > AppUtils.getAppVersionCode()
//                        && versionCode > localVersionCode
                ) {
                    if (mUpdateVersionDialog == null) {
                        mUpdateVersionDialog = new UpdateVersionDialog(mContext);
                    }
                    mUpdateVersionDialog.show(data, () -> {
                        LogUtils.e(TAG, "=========有新版本，取消更新=========>");
                        callback.onNotUpdateVersion();
                    });
                    // 非强更的情况下记录版本号到本地
//                    if (data != null && !data.isForceUpdate()) {
//                        MMKV.defaultMMKV().putInt(Constants.KEY_NEW_VERSION_CODE, versionCode);
//                    }
                } else {
                    LogUtils.e(TAG, "=========无新版本，无需更新=========>");
                    if (callback != null) {
                        callback.onNotUpdateVersion();
                    }
                }
            }

            @Override
            public void onResponseFail(Context context, String errorCode, String errorMsg) {

                // 检查3次，不过的话退出app
                if (mCurrentRequestCount < MAX_REQUEST_COUNT) {
                    checkVersion(callback);
                } else {
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
//                    showErrorDialog();
                    if (callback != null) {
                        callback.onNotUpdateVersion();
                    }
                }

                mCurrentRequestCount++;
            }
        });
    }

    private void showErrorDialog() {
        if (mErrorDialog == null) {
            mErrorDialog = new AlertDialog.Builder(mContext)
                    .setContentView(R.layout.layout_dialog_check_version_error)
                    .setOnClickListener(R.id.tv_dialog_check_version_error_exit, v -> {
                        mErrorDialog.dismiss();
                        ActivityLifecycleManager.getInstance().finishAllActivity();
                        RouterHelper.getActivity(RouterHelper.App.SPLASH_ACTIVITY).navigation();
                    })
                    .fullWidth()
                    .setCanceledOnTouchOutside(false)
                    .setCancelable(false)
                    .create();
        }
        mErrorDialog.show();
    }

    public void getVersionInfo(GetVersionInfoCallback callback) {
        requestVersionInfo(new DefaultHttpCallback() {
            @Override
            public void onResponseSuccess(VersionBean data) {
                int versionCode = data == null ? AppUtils.getAppVersionCode() : data.getVersionCode();
                if (callback != null) {
                    callback.onSuccess(versionCode > AppUtils.getAppVersionCode(), data);
                }
            }

            @Override
            public void onResponseFail(Context context, String errorCode, String errorMsg) {
                if (callback != null) {
                    callback.onFail(errorMsg);
                }
            }
        });
    }

    private Function convertVersionInfoJSONObject() {
        return (Function<String, ObservableSource<VersionBean>>) result -> {
            String jsonStr = result.toString();
            VersionBean versionBean = GsonUtils.fromJson(jsonStr, VersionBean.class);
            return Observable.just(versionBean);
        };
    }
//   private Function convertVersionInfoJSONObject() {
//        return (Function<VersionBean, ObservableSource<VersionBean>>) Observable::just;
//    }

    private void requestVersionInfo(DefaultHttpCallback callback) {
        LiveDataObservableAdapter.fromObservable(
                        CoreApi.getInstance().getVersionInfo(AppUtils.getAppVersionName(),
                                        "android", AppUtils.getAppVersionCode(), 15)
                                .compose(RxSchedulers.io())
                                .compose(RxSchedulers.handleResult())
                                .flatMap(convertVersionInfoJSONObject())
                                .retryWhen(new RxRetry()))
                .observe(mContext, new Observer<RxResult<VersionBean>>() {
                    @Override
                    public void onChanged(RxResult<VersionBean> versionBeanRxResult) {
                        if (versionBeanRxResult.status == RxStatus.Loading) {

                        } else if (versionBeanRxResult.status == RxStatus.Success) {
                            if (callback != null) {
                                callback.onResponseSuccess(versionBeanRxResult.data);
                            }
                        } else {
                            if (callback != null) {
                                callback.onResponseFail(mContext, versionBeanRxResult.error.getCode() + "", versionBeanRxResult.error.getMessage());
                            }
                        }
                    }
                });

    }

    public interface CheckVersionCallback {
        void onNotUpdateVersion();
    }

    public interface DefaultHttpCallback {
        void onResponseSuccess(VersionBean data);

        void onResponseFail(Context context, String errorCode, String errorMsg);
    }

    public interface GetVersionInfoCallback {
        void onSuccess(boolean hasNewVersion, VersionBean bean);

        void onFail(String message);
    }

    public void release() {
        if (mUpdateVersionDialog != null) {
            mUpdateVersionDialog.release();
            mUpdateVersionDialog = null;
        }
        if (mErrorDialog != null) {
            mErrorDialog.dismiss();
            mErrorDialog = null;
        }
        if (mLoadingDialog != null) {
            mLoadingDialog.release();
            mLoadingDialog = null;
        }
    }
}