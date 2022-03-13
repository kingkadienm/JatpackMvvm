package com.wangzs.router;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;

@Route(path = RouterHelper.SERVICE_NAME)
public class RouterServiceImpl implements RouterService {


    private AccountService accountService;
    private HomeService homeService;
    private MessageService messageService;
    private ContactService contactService;

    @Override
    public void init(Context context) {

    }

    @Override
    public AccountService getAccountService() {
        return accountService;
    }

    @Override
    public void setAccountService(AccountService service) {
        this.accountService = service;
    }

    @Override
    public HomeService getHomeService() {
        return homeService;
    }

    @Override
    public void setHomeService(HomeService service) {
        this.homeService = service;
    }

    @Override
    public MessageService getMessageService() {
        return messageService;
    }

    @Override
    public void setMessageService(MessageService service) {
        this.messageService = service;
    }

    @Override
    public ContactService getContactService() {
        return contactService;
    }

    @Override
    public void setContactService(ContactService service) {
        this.contactService = service;
    }
}
