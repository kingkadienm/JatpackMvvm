package com.wangzs.router;

import com.alibaba.android.arouter.facade.template.IProvider;

public interface RouterService extends IProvider {


    /**
     * 个人模块
     * @return
     */
    AccountService getAccountService();

    void setAccountService(AccountService service);

    HomeService getHomeService();

    void setHomeService(HomeService service);

    MessageService getMessageService();

    void setMessageService(MessageService service);

    ContactService getContactService();

    void setContactService(ContactService service);
}
