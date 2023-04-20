package com.wangzs.base.toolskit;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.text.TextUtils;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;

import java.util.Locale;

/**
 * 静态换肤、换语言管理
 */
public class LanguageUtils {
    private static final String TAG = LanguageUtils.class.getSimpleName();

    private static final String SP_THEME_AND_LANGUAGE_NAME = "spUtils";
    private static final String SP_KEY_LANGUAGE = "constants_language";
    private static final String SP_KEY_THEME = "theme";


    private static final class ThemeManagerHolder {
        private static final LanguageUtils instance = new LanguageUtils();
    }

    public static LanguageUtils getInstance() {
        return ThemeManagerHolder.instance;
    }

    private LanguageUtils() {
    }

    private boolean isInit = false;


    private String currentLanguage = "";
    private Locale defaultLocale = null;

    public static void setTheme(Context context) {
        getInstance().setThemeInternal(context);
    }

    private void setThemeInternal(Context context) {
        if (context == null) {
            return;
        }

        Context appContext = context.getApplicationContext();
        if (!isInit) {
            isInit = true;
            if (appContext instanceof Application) {
                ((Application) appContext).registerActivityLifecycleCallbacks(new ThemeAndLanguageCallback());
            }

            Locale defaultLocale = getLocale(appContext);
//            changeLanguage(context,defaultLocale.getLanguage());
            String string = SPUtils.getInstance().getString(SP_KEY_LANGUAGE);
            if (TextUtils.isEmpty(string)) {
                SPUtils.getInstance().put(SP_KEY_LANGUAGE, defaultLocale.getLanguage());
                currentLanguage = defaultLocale.getLanguage();
            } else {
                currentLanguage = string;
            }
            // 语言只需要初始化一次
            applyLanguage(appContext);
        }
        // 主题需要更新多次
    }

    private String getProcessName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
            if (processInfo.pid == Process.myPid()) {
                return processInfo.processName;
            }
        }
        return "";
    }

    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }


    public void changeLanguage(Context context, String language) {
        if (context == null) {
            return;
        }

        if (TextUtils.equals(language, currentLanguage)) {
            return;
        }
        currentLanguage = language;

        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_THEME_AND_LANGUAGE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SP_KEY_LANGUAGE, language);
        editor.commit();

        applyLanguage(context.getApplicationContext());
        applyLanguage(context);
    }

    public void applyLanguage(Context context) {
        if (context == null) {
            return;
        }

        Locale locale = getLocale(context);
        if ("en".equals(currentLanguage)) {
            locale = Locale.ENGLISH;
        } else if ("zh".equals(currentLanguage)) {
            locale = Locale.CHINA;
        } else if (defaultLocale != null) {
            locale = defaultLocale;
        }

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        }
        resources.updateConfiguration(configuration, null);

        if (Build.VERSION.SDK_INT >= 25) {
            context = context.createConfigurationContext(configuration);
            context.getResources().updateConfiguration(configuration,
                    resources.getDisplayMetrics());
        }
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public Locale getLocale(Context context) {
        Locale locale;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().locale;
        } else {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        }
        return locale;
    }


    /**
     * 获取参与换肤的资源 id
     *
     * @param context 一般为当前界面的 Activity，此 Activity 实现了 ITUIThemeChangeable 接口
     * @param attrId  attr 自定义的要变换主题的 attr
     * @return 当前主题下的资源 id
     */
    public static int getAttrResId(Context context, int attrId) {
        if (context == null || attrId == 0) {
            return 0;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attrId, typedValue, true);
        return typedValue.resourceId;
    }

    static class ThemeAndLanguageCallback implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
//            LanguageUtils.getInstance().applyTheme(activity);
            LanguageUtils.getInstance().applyLanguage(activity);
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {

        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {

        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {

        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {

        }
    }

}