package com.wangzs.jatpackmvvm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.wangzs.base.Constants;
import com.wangzs.base.base.activity.BaseActivity;
import com.wangzs.base.base.activity.ChangeLanguageActivity;
import com.wangzs.core.manager.ActivityLifecycleManager;
import com.wangzs.router.RouterHelper;

import java.util.List;

/**
 * @Description 启动页
 * @Date 2022/3/23 023 15:51
 * @Created by wangzs
 */
@Route(path = RouterHelper.App.SPLASH_ACTIVITY)
public class SplashActivity extends BaseActivity {


    private PrivacyPolicyDialog mPrivacyPolicyDialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_splash;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
    }

    @Override
    protected void initView() {
        super.initView();
        boolean isAgree = SPUtils.getInstance().getBoolean(Constants.ISAGREE, false);
        if (!isAgree) {
            initPrivacyPolicyDialog();
        } else {
            boolean isFirst = SPUtils.getInstance().getBoolean(Constants.KEY_SHOW_GUIDE_PAGE, false);
            if (!isFirst) {
                startActivity(new Intent(this, GuideActivity.class));
                finish();
            } else {
                new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        LogUtils.i("SplashActivity", "倒计时结束");
                        boolean isLogin = SPUtils.getInstance().getBoolean(Constants.IS_LOGIN, false);
                        if (isLogin) {
                            try {
                                Activity topActivity = ActivityLifecycleManager.getInstance().getTopActivity();
                                LogUtils.i("SplashActivity", " 启动 " + topActivity + " activity");
                                if (topActivity instanceof SplashActivity) {
                                    RouterHelper.getActivity(RouterHelper.App.MAIN_ACTIVITY).navigation();
                                    finish();
                                } else {
                                    Intent intent = new Intent(mContext, topActivity.getClass());
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (Exception e) {
                                LogUtils.i("SplashActivity", " 启动  Exception  activity");
                                RouterHelper.getActivity(RouterHelper.App.MAIN_ACTIVITY).navigation();
                                finish();
                            }
                        } else {
                            LogUtils.e("启动页跳转login activity");
//                            startActivity(new Intent(mContext, ChangeLanguageActivity.class));
                            RouterHelper.getActivity(RouterHelper.Account.LOGIN_ACTIVITY).navigation();
                            finish();
                        }
                    }
                }.start();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initPrivacyPolicyDialog() {
        mPrivacyPolicyDialog = new PrivacyPolicyDialog(this);
        mPrivacyPolicyDialog.show(true, new PrivacyPolicyDialog.PrivacyPolicyDialogCallback() {
            @Override
            public void onComplete() {
//                requestPermission();
                CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
                strategy.setAppVersion(AppUtils.getAppVersionName());
                CrashReport.initCrashReport(getApplicationContext(), PrivateConstants.BUGLY_APPID, true, strategy);
                boolean isFirst = SPUtils.getInstance().getBoolean(Constants.KEY_SHOW_GUIDE_PAGE, false);
                if (!isFirst) {
                    startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    finish();
                } else {
                    LogUtils.e("启动页跳转login activity");
                    RouterHelper.getActivity(RouterHelper.Account.LOGIN_ACTIVITY).navigation();
                    finish();
                }
            }
        });
    }

    private void requestPermission() {
        PermissionUtils.permission(PermissionConstants.STORAGE, PermissionConstants.CAMERA, PermissionConstants.MICROPHONE).callback(
                new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        boolean isFirst = SPUtils.getInstance().getBoolean(Constants.KEY_SHOW_GUIDE_PAGE, false);
                        if (!isFirst) {
                            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                            finish();
                        } else {
                            LogUtils.e("启动页跳转login activity");
                            RouterHelper.getActivity(RouterHelper.Account.LOGIN_ACTIVITY).navigation();
                            finish();
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {

                        boolean isFirst = SPUtils.getInstance().getBoolean(Constants.KEY_SHOW_GUIDE_PAGE, false);
                        if (!isFirst) {
                            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                            finish();
                        } else {
                            LogUtils.e("启动页跳转login activity");
                            RouterHelper.getActivity(RouterHelper.Account.LOGIN_ACTIVITY).navigation();
                            finish();
                        }
                    }
                }).request();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPrivacyPolicyDialog != null) {
            mPrivacyPolicyDialog.release();
            mPrivacyPolicyDialog = null;
        }
    }


}


