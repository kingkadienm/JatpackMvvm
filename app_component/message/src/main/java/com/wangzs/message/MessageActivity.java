package com.wangzs.message;

import androidx.fragment.app.FragmentManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.wangzs.base.base.activity.BaseActivity;
import com.wangzs.message.fragment.MessageFragment;
import com.wangzs.router.RouterHelper;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2020-03-12 0:27
 * @Version:
 */
@Route(path = RouterHelper.Message.MESSAGE_ACTIVITY)
public class MessageActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.message_activity_message;
    }

    @Override
    protected void afterSetContentView() {
        ARouter.getInstance().inject(this);
    }

    @Override
    protected void initView() {
        MessageFragment messageFragment = MessageFragment.newInstance();

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction().add(R.id.message_root_view, messageFragment)
                .commit();

    }
}
