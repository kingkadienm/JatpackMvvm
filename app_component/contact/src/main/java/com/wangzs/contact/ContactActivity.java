package com.wangzs.contact;

import androidx.fragment.app.FragmentManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.wangzs.base.base.activity.BaseActivity;
import com.wangzs.contact.fragment.ContactFragment;
import com.wangzs.router.RouterHelper;

/**
 * @Description:
 * @Author: wangzs
 * @Date: 2022-03-12 0:04
 * @Version:
 */
@Route(path = RouterHelper.Contact.CONTACT_ACTIVITY)
public class ContactActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.contact_activity_contact;
    }

    @Override
    protected void afterSetContentView() {
        ARouter.getInstance().inject(this);
    }

    @Override
    protected void initView() {
        ContactFragment contactFragment = ContactFragment.newInstance();
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction().add(R.id.contact_root_view, contactFragment)
                .commit();
    }
}
