package com.wangzs.base.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.wangzs.base.R;
import com.wangzs.base.toolskit.NetworkUtil;

import java.io.File;

/**
 * 后台静默下载apk
 */
public class ApkDownloadService extends Service {
    private static final String TAG = ApkDownloadService.class.getSimpleName();
    private Context mContext;
    // 是否正在下载
    private boolean mIsDownloadApk;
    // 安装包url
    private String mApkUrl = "";
    // 下载包安装文件名
    private String mDestFileName = "";
    // 下载包安装文路径
    private String mDestFileDir = "";
    // 回调结果
    private ICallbackResult mCallback;
    // 下载的Binder
    private DownloadBinder mBinder;
    // 下载失败重试的次数
    private int mReTryCount = 0;

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        LogUtils.e(TAG, "===" + TAG + " onCreate ===>");
        mBinder = new DownloadBinder();
    }

    public static final String KEY_APK_URL = "apkUrl";
    public static final String KEY_FILE_DIR = "destFileDir";
    public static final String KEY_FILE_NAME = "destFileName";

    @Override
    public IBinder onBind(Intent intent) {
        mApkUrl = intent.getStringExtra(KEY_APK_URL);
        mDestFileDir = intent.getStringExtra(KEY_FILE_DIR);
        mDestFileName = intent.getStringExtra(KEY_FILE_NAME);
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e(TAG, "===" + TAG + " onDestroy ===>");
    }

    public class DownloadBinder extends Binder {
        public void start() {
            if (mIsDownloadApk) {
                LogUtils.e(TAG, "当前正在执行Apk下载");
                return;
            }
            // 下载
            mReTryCount = 0;
            downloadApk();
        }

        public void addCallback(ICallbackResult callback) {
            ApkDownloadService.this.mCallback = callback;
        }

        public void retryDownload() {
            mReTryCount = 0;
            downloadApk();
        }
    }

    /**
     * 下载apk.
     */
    private void downloadApk() {
        if (!NetworkUtil.isConnected(mContext)) {
            ToastUtils.showShort(mContext.getString(R.string.check_connection));
            mCallback.onFail("");
            mIsDownloadApk = false;
            return;
        }
        // 修改下载标志位
        mIsDownloadApk = true;
        delApkFile();
 
        UpdaterDownloader downloader = new UpdaterDownloader( SPUtils.getInstance().getLong("updater_sumByte", 0), new ReadDataListener() {
            @Override
            public void notifyByteProgress(long totalByte, long readByte) {
                int progress = (int) (readByte * 100 / totalByte);
//                LogUtils.d(TAG, "Apk下载进度:" + progress);
                mCallback.onProgress(progress);

            }

            @Override
            public void onComplete(File file) {
                LogUtils.e(TAG, "Apk下载完成：" + file.getAbsolutePath());
                mIsDownloadApk = false;
                mCallback.onSuccess(file);
                ToastUtils.showShort(getString(R.string.download_completed), Toast.LENGTH_LONG);
                // 停掉服务自身
                stopSelf();
            }

            @Override
            public void onCancle() {

            }

            @Override
            public void onFail() {
                LogUtils.e(TAG, "Apk下载失败：");
                delApkFile();
                if (mReTryCount == 2) {
                    mIsDownloadApk = false;
                    ToastUtils.showShort(getString(R.string.server_net_error));
                    stopSelf();// 停掉服务自身
                    mCallback.onFail("");
                    return;
                }
                mReTryCount++;
                downloadApk();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                downloader.doDownload(mApkUrl, mDestFileDir + mDestFileName);
            }
        }).start();

    }

    private void delApkFile() {
        File file = new File(mDestFileDir, mDestFileName);
        if (file.exists()) {//如果本地已存在该文件，删除掉，重新下载
            boolean delResult = file.delete();
            LogUtils.e(TAG, "文件删除结果：" + delResult);
        }
    }


    public interface ICallbackResult {
        void onProgress(int progress);

        void onSuccess(File file);

        void onFail(String message);
    }
}
