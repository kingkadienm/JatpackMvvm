package com.wangzs.router;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;

public class RouterHelper {

    public static final String SERVICE_NAME = "/router/RouterService";

    public static RouterService getService() {
        return (RouterService) ARouter.getInstance().build(SERVICE_NAME).navigation();
    }

    public static Postcard getActivity(String router) {
        return ARouter.getInstance().build(router);
    }

    public static class App {
        public static final String MAIN_ACTIVITY = "/app/MainActivity";
    }

    public static class Home {
        public static final String HOME_ACTIVITY = "/home/HomeActivity";

    }

    public static class Contact {
        public static final String CONTACT_ACTIVITY = "/contact/ContactActivity";
    }

    public static class Account {
        public static final String ACCOUNT_ACTIVITY = "/account/AccountActivity";
    }

    public static class Message {
        public static final String MESSAGE_ACTIVITY = "/message/MessageActivity";
    }
}
