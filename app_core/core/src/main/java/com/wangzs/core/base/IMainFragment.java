package com.wangzs.core.base;

public interface IMainFragment {

    /**
     * 首页 Fragment 的位置索引 - 首页
     */
    int FRAGMENT_HOME_INDEX = 0;
    /**
     * 首页 Fragment 的位置索引 - 联系人
     */
    int FRAGMENT_CONTACT_INDEX = 1;
    /**
     * 首页 Fragment 的位置索引 - 消息
     */
    int FRAGMENT_MESSAGE_INDEX = 2;
    /**
     * 首页 Fragment 的位置索引 - 我的
     */
    int FRAGMENT_ACCOUNT_INDEX = 3;

    /**
     * tab 的图标
     */
    int getTabIconRes();

    /**
     * tab 的图标
     */
    int getTabIconCheckRes();

    /**
     * tab 的位置
     */
    int getTabIndex();

    String getTitleTxt();
}
