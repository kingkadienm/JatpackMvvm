package com.wangzs.jatpackmvvm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.wangzs.base.Constants;
import com.wangzs.base.base.activity.BaseActivity;
import com.wangzs.router.RouterHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 新手引导页
 *
 * @author wangzs
 */
public class GuideActivity extends BaseActivity {


    private List<Integer> mData = new ArrayList<>(Arrays.asList(
            R.layout.layout_item_guide_one,
            R.layout.layout_item_guide_one,
            R.layout.layout_item_guide_one));
    private int mCurrentPosition = 0;
    private LinearLayout mIndicatorLayout;

    @Override
    protected int getLayoutResId() {
        //无title
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_guide;
    }


    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        TextView button = findViewById(R.id.guide_ib_start);
        ViewPager mViewPager = findViewById(R.id.vp_guide);
        mIndicatorLayout = findViewById(R.id.ll_guide_indicator);
        mViewPager.setAdapter(new MyPagerAdapter());
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                updateIndicator();
                if (position == mData.size() - 1) {
                    button.setVisibility(View.VISIBLE);
                    mIndicatorLayout.setVisibility(View.GONE);
                } else {
                    button.setVisibility(View.GONE);
                    mIndicatorLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        button.setOnClickListener(v -> {
            SPUtils.getInstance().put(Constants.IS_FIRST, true);
            RouterHelper.getActivity(RouterHelper.Account.LOGIN_ACTIVITY).navigation();
            finish();
        });
        mIndicatorLayout.removeAllViews();
        for (int i = 0; i < mData.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.drawable.selector_guide_indicator);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin = i == mData.size() - 1 ? 0 : SizeUtils.dp2px(15);
            imageView.setLayoutParams(params);
            imageView.setSelected(mCurrentPosition == i);
            mIndicatorLayout.addView(imageView);
        }
    }

    /**
     * 更新指示器
     */
    private void updateIndicator() {
        for (int i = 0; i < mIndicatorLayout.getChildCount(); i++) {
            mIndicatorLayout.getChildAt(i).setSelected(i <= mCurrentPosition);
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mData == null ? 0 : mData.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View inflate = LayoutInflater.from(mContext).inflate(mData.get(position), null);
            container.addView(inflate);
            return inflate;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtils.getInstance().put(Constants.KEY_SHOW_GUIDE_PAGE, true);
    }
}

