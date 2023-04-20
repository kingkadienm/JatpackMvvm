package com.wangzs.base.base.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.wangzs.base.BaseApplication;
import com.wangzs.base.R;
import com.wangzs.base.toolskit.StatusBarUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class WebViewActivity extends BaseActivity {

    public static final String KEY_URL = "WebViewURL";
    public static final String KEY_TITLEL = "WebViewTitle";
    private WebView webView;
    private String title;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initView() {
        super.initView();
        webView = findViewById(R.id.webview_load);
//        AndroidBug5497Workaround.assistActivity(this);
    }


    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        String url = intent.getStringExtra(KEY_URL);
        title = intent.getStringExtra(KEY_TITLEL);
        if (!StringUtils.isEmpty(title)) {
            setTitle(title);
        }
        StatusBarUtil.setDarkMode(this);
//
        webView.setLongClickable(true);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        WebSettings settings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAllowContentAccess(false);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDatabaseEnabled(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setAllowFileAccess(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setDomStorageEnabled(true);
        settings.setBlockNetworkImage(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setLoadsImagesAutomatically(true);
//        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
//        String userAgentString = settings.getUserAgentString();
//        settings.setUserAgentString(userAgentString + getAPPToken());
//        Map<String, String> headerParamsMap = new HashMap<String, String>();
//        headerParamsMap.put("token", SPUtils.getInstance().getString(Constants.TOKEN, ""));
//        headerParamsMap.put("t", MD5Utils.md5(String.valueOf(System.currentTimeMillis())));
//        headerParamsMap.put("deviceLanguage", Locale.getDefault().getLanguage());
//        headerParamsMap.put("deviceType", "ANDROID");

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                LogUtils.e(" url  is " + request.getUrl());
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtils.e(" url  is " + url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                JsApi.getInstance().loginInApp();
                settings.setBlockNetworkImage(false);

            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                // 根据url得到文件名
                LogUtils.e(" Main WebView onPageFinished: " + request.getUrl());

                String fileName = (request.getUrl().toString());
                {
                    // 当前文件可以使用缓存, 根据后缀判断 mimetype
                    InputStream inputStream = null;
                    String mimeType = null;
                    if (fileName.endsWith("css")) {
                        mimeType = "text/css";
                    } else if (fileName.endsWith("js")) {
                        mimeType = "text/javascript";
                    } else if (fileName.endsWith("png")) {
                        mimeType = "image/png";
                    }
                    if (mimeType == null) {
                        return null;
                    }
                    try {
                        inputStream = BaseApplication.getContext().getAssets().open(fileName);
                    } catch (IOException e) {
                        LogUtils.e("read file IOException: " + e.getMessage());
                    }
                    if (inputStream != null) {
                        WebResourceResponse response = new WebResourceResponse(
                                mimeType, "utf-8", inputStream);
                        // 解决css、js的跨域问题
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Access-Control-Allow-Origin", "*");
                        headers.put("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
                        headers.put("Access-Control-Max-Age", "3600");
                        headers.put("Access-Control-Allow-Headers", "Content-Type,Access-Token,Authorization");
                        response.setResponseHeaders(headers);
                        return response;
                    }
                    return null;
                }
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
            }

            @Nullable
            @Override
            public Bitmap getDefaultVideoPoster() {
                return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_4444);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);


            }
        });
//        webView.addJavascriptInterface(new BaseJsApi(this), "App");
        webView.loadUrl(url);
//
    }

    @Override
    protected void onDestroy() {
        webView.destroy();
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}