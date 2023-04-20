package com.wangzs.base.toolskit;

import android.content.Context;
import android.os.Environment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;

import java.io.File;
import java.util.List;

/**
 * 存储相关工具类
 * Created by wangzs on 2020/8/3.
 */
public class StorageUtils {
    private static final String TAG = StorageUtils.class.getSimpleName();

    private static final String LOG_PATH = "/log/";
    private static final String APK_DOWNLOAD_PATH = "/apkDownload/";
    private static final String CAMERA_TEMP_PATH = "/cameraTemp/";
    private static final String FILE_TEMP_PATH = "/fileTemp/";
    private static final String BITMAP_TEMP_PATH = "/bitmapCache/";
    private static final String WELCOME_PNG_PATH = "/welcome/";

    /**
     * 检测SD卡是否可
     */
    public static boolean checkSDCardState() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private static Context getContext(Object target) {
        Context context = null;
        if (target instanceof FragmentActivity) {
            context = (FragmentActivity) target;
        } else if (target instanceof Fragment) {
            context = ((Fragment) target).getContext();
        }
        return context;
    }

    /**
     * 获取存储路径
     */
    public static void getStoragePath(Object target, StorageCallback callback) {
        Context context = getContext(target);
        if (context == null) {
            onPermissionComplete("", callback);
            return;
        }

        if (PermissionUtils.isGranted(PermissionConstants.STORAGE)) {
            onPermissionComplete(SDCardPath(context), callback);
            return;
        }
        if (!checkSDCardState()) {
            onPermissionComplete(DataPath(context), callback);
        }
        PermissionUtils.permission(PermissionConstants.STORAGE).callback(new PermissionUtils.FullCallback() {
            @Override
            public void onGranted(List<String> permissionsGranted) {
                LogUtils.e(TAG, "读写外部存储权限已经授予");
                onPermissionComplete(SDCardPath(context), callback);

            }

            @Override
            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                 onPermissionComplete(DataPath(context), callback);
            }
        }).request();

    }

    /**
     * 获取外置存储卡路径
     */
    private static String SDCardPath(Context context) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + context.getPackageName();
    }

    /**
     * 获取内部存储路径：data/data/包名/file（实际不同机型目录可能不同）
     */
    private static String DataPath(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }

    /**
     * 统一处理路径文件夹创建
     */
    private static void onPermissionComplete(String path, StorageCallback callback) {
        if (callback != null) {
            File file = new File(path);
            if (!file.exists()) {
                boolean result = file.mkdirs();
                LogUtils.e(TAG, "路径文件夹创建结果：" + result);
            }
            callback.onComplete(path);
        }
    }

    /**
     * 获取应用的BitmapCache路径.
     */
    public static void getBitmapCachePath(Object target, StorageCallback callback) {
        getStoragePath(target, path -> {
            String fileTempPath = path + BITMAP_TEMP_PATH;
            onPermissionComplete(fileTempPath, callback);
        });
    }

    /**
     * 获取文件缓存路径.
     */
    public static void getFileTempPath(Object target, StorageCallback callback) {
        getStoragePath(target, path -> {
            String fileTempPath = path + FILE_TEMP_PATH;
            onPermissionComplete(fileTempPath, callback);
        });
    }

    /**
     * 获取启动页图片缓存路径.
     */
    public static void getFileWelComePngPath(Object target, StorageCallback callback) {
        getStoragePath(target, path -> {
            String fileTempPath = path + WELCOME_PNG_PATH;
            onPermissionComplete(fileTempPath, callback);
        });
    }

    /**
     * 获取应用的拍照临时保存的路径.
     */
    public static void getCameraTempPath(Object target, StorageCallback callback) {
        getStoragePath(target, path -> {
            String fileTempPath = path + CAMERA_TEMP_PATH;
            onPermissionComplete(fileTempPath, callback);
        });
    }


    /**
     * 功能:获取apk文件更新目录
     */
    public static void getUpdatePath(Object target, StorageCallback callback) {
        getStoragePath(target, path -> {
            String fileTempPath = path + APK_DOWNLOAD_PATH;
            LogUtils.e(TAG, "Apk存储目录是：" + fileTempPath);
            onPermissionComplete(fileTempPath, callback);
        });
    }


    /**
     * 日志文件存储路径
     */
    public static void getLogPath(Object target, StorageCallback callback) {
        getStoragePath(target, path -> {
            String fileTempPath = path + LOG_PATH;
            onPermissionComplete(fileTempPath, callback);
        });
    }

    /**
     * 路径获取回调
     */
    public interface StorageCallback {
        void onComplete(String path);
    }
}