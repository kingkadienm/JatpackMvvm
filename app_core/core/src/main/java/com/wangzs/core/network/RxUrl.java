package com.wangzs.core.network;

import android.text.TextUtils;


import com.wangzs.base.AppGlobal;

import java.util.HashMap;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2022-03-10
 * @Version:
 */

public class RxUrl {

    private static final String DEV_BASE_URL = "xxxxxxxx";
    private static final String TEST_BASE_URL = "xxxxxx";
    private static final String PRE_BASE_URL = "xxxxxxx";
    private static final String PRODUCT_BASE_URL = "xxxxxxx";

    private static final String URL = "xxxxxxxxx";

    /**
     * 信息流图片服务器地址
     */
    private static final String IMAGE_URL = "xxxxxxxxx";

    private static final HashMap<String, String> baseUrlMap = new HashMap<>();

    static {
        baseUrlMap.put(EnvController.Env.ENV_DEV, DEV_BASE_URL);
        baseUrlMap.put(EnvController.Env.ENV_TEST, TEST_BASE_URL);
        baseUrlMap.put(EnvController.Env.ENV_PRE, PRE_BASE_URL);
        baseUrlMap.put(EnvController.Env.ENV_PRODUCT, PRODUCT_BASE_URL);
    }

    public static String getBaseUrl() {
//        return URL;
        if (!AppGlobal.isDebug) {
            return PRODUCT_BASE_URL;
        } else {
            String baseUrl = baseUrlMap.get(EnvController.getCurEnv());
            return !TextUtils.isEmpty(baseUrl) ? baseUrl : TEST_BASE_URL;
        }
    }

    public static String getImageUrl() {
        return IMAGE_URL;
    }

}
