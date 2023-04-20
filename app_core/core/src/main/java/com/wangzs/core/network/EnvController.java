package com.wangzs.core.network;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;

public class EnvController {

    public static final String SP_KEY_ENV = "sp_key_env";

    private static String curEnv;

    public static String getCurEnv(){
        if (TextUtils.isEmpty(curEnv)){
            curEnv =  SPUtils.getInstance().getString(SP_KEY_ENV, Env.ENV_TEST);
        }
        return curEnv;
    }
    public static void setCurEnv(String env){
        curEnv = env;
       SPUtils.getInstance().put(SP_KEY_ENV, env);
    }

    public static class Env {
        /**
         * 开发
         */
        public static final String ENV_DEV = "env_dev";
        /**
         * 测试
         */
        public static final String ENV_TEST = "env_test";
        /**
         * 预发
         */
        public static final String ENV_PRE = "env_pre";
        /**
         * 生产
         */
        public static final String ENV_PRODUCT = "env_product";
    }

}
