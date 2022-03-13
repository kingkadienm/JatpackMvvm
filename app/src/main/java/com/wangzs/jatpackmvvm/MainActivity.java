package com.wangzs.jatpackmvvm;



import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wangzs.base.base.activity.BaseActivity;
import com.wangzs.base.base.fragment.BaseFragment;
import com.wangzs.base.toolskit.LogUtil;
import com.wangzs.base.weight.navigation.NavigationController;
import com.wangzs.base.weight.navigation.PageNavigationView;
import com.wangzs.base.weight.navigation.ext.SpecialTabRound;
import com.wangzs.base.weight.navigation.item.BaseTabItem;
import com.wangzs.base.weight.navigation.item.MaterialItemView;
import com.wangzs.base.weight.navigation.listener.OnTabItemSelectedListener;
import com.wangzs.core.base.IMainFragment;
import com.wangzs.core.component.ViewManager;
import com.wangzs.jatpackmvvm.views.MainTab;
import com.wangzs.router.RouterHelper;

import java.util.List;

@Route(path = RouterHelper.App.MAIN_ACTIVITY)
public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private NavigationController mNavController;
    private ViewPager2 mViewPager;

    List<BaseFragment> mFragments;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        PageNavigationView tab = findViewById(R.id.main_nav);

        PageNavigationView.CustomBuilder builder = tab.custom();

        mFragments = ViewManager.getInstance().getAllFragment(this);

        int i = 0;

        for (BaseFragment fragment : mFragments) {

            if (fragment instanceof IMainFragment) {
                IMainFragment mainFragment = (IMainFragment) fragment;
                builder.addItem(newItem(mainFragment.getTabIconRes(),
                        mainFragment.getTabIconCheckRes(),mainFragment.getTitleTxt()));

//                builder.addItem(newMaterialItem(mainFragment.getTitleTxt(),mainFragment.getTabIconRes(),
//                         mainFragment.getTabIconCheckRes(),false,0,0));
            }

            i++;
        }

        mNavController = builder.build();

        mViewPager = findViewById(R.id.main_viewpager);
        mViewPager.setUserInputEnabled(false);
        mViewPager.setAdapter(new MainPagerAdapter(this));

        //自动适配ViewPager页面切换
        mNavController.setupWithViewPager(mViewPager);

        //设置消息数
//        mNavController.setMessageNumber(0, 8);

        //设置显示小圆点
//        mNavController.setHasMessage(1, true);

        mNavController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                LogUtil.i(TAG, "tab onSelected index:" + index + ",old:" + old);
            }

            @Override
            public void onRepeat(int index) {
                LogUtil.i(TAG, "tab onRepeat index:" + index);
            }
        });
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    /**
     * 正常tab
     */
    private BaseTabItem newItem(int drawable, int checkedDrawable,String titleRes) {
        MainTab mainTab = new MainTab(this);
        mainTab.initialize(drawable, checkedDrawable,titleRes);
        return mainTab;
    }



    /**
     * 圆形tab
     */
    private BaseTabItem newRoundItem(int drawable, int checkedDrawable, String text) {
        SpecialTabRound mainTab = new SpecialTabRound(this);
        mainTab.initialize(drawable, checkedDrawable, text);
        mainTab.setTextDefaultColor(0xFF888888);
        mainTab.setTextCheckedColor(0xFF009688);
        // ColorUtil.getColorByResId(this, R.color.navigationBarTextActivatedColor)
        return mainTab;
    }

//    private BaseTabItem newMaterialItem(String title, int drawable, int checkedDrawable, boolean tintIcon, int color, int checkedColor){
//        MaterialItemView mainTab = new MaterialItemView(this);
//        mainTab.initialization(title, ContextCompat.getDrawable(this, drawable), ContextCompat.getDrawable(this, checkedDrawable), tintIcon, color, checkedColor);
//        return mainTab;
//    }

    private class MainPagerAdapter extends FragmentStateAdapter {

        public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getItemCount() {
            return mFragments.size();
        }
    }
}