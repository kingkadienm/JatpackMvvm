package com.wangzs.base.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 版本信息
 */
public class VersionBean extends RxBean {
    private static final long serialVersionUID = 1L;


    @SerializedName("Version")
    private String versionName;
    private String VersionCode;
    private String isForcedUpdate; //是否强制更新
    @SerializedName("versionIntroduce")
    private String updateInfo;//更新内容
    @SerializedName("md5")
    private String md5;//文件MD5
    private String clientType;// 客户端类型
    @SerializedName("Download")
    private String upgradeUrl;// 加密的下载链接


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean isForceUpdate() {
        return "1".equals(isForcedUpdate);
    }


    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return Integer.parseInt(VersionCode);
    }

    public void setVersionCode(String versionCode) {
        this.VersionCode = versionCode;
    }

    public String getIsForcedUpdate() {
        return isForcedUpdate;
    }

    public void setIsForcedUpdate(String isForcedUpdate) {
        this.isForcedUpdate = isForcedUpdate;
    }


    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getUpgradeUrl() {
        return upgradeUrl;
    }

    public void setUpgradeUrl(String upgradeUrl) {
        this.upgradeUrl = upgradeUrl;
    }


}
