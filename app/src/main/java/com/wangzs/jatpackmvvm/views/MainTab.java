package com.wangzs.jatpackmvvm.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.wangzs.base.weight.navigation.internal.RoundMessageView;
import com.wangzs.base.weight.navigation.item.BaseTabItem;
import com.wangzs.jatpackmvvm.R;


public class MainTab extends BaseTabItem {

    private ImageView mIcon;
    private TextView mTabText;
    private final RoundMessageView mMessages;

    private Drawable mDefaultDrawable;
    private Drawable mCheckedDrawable;
    private String mTitleRes;

    private boolean mChecked;

    public MainTab(Context context) {
        this(context, null);
    }

    public MainTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.activity_main_tab, this, true);

        mIcon = findViewById(R.id.main_tab_icon);
        mTabText = findViewById(R.id.main_tab_text);
        mMessages = findViewById(R.id.messages);
    }

    @Override
    public void setOnClickListener(@Nullable View.OnClickListener l) {
        View view = getChildAt(0);
        if (view != null) {
            view.setOnClickListener(l);
        }
    }

    /**
     * 方便初始化的方法
     *
     * @param drawableRes        默认状态的图标
     * @param checkedDrawableRes 选中状态的图标
     */
    public void initialize(@DrawableRes int drawableRes, @DrawableRes int checkedDrawableRes) {
        mDefaultDrawable = ContextCompat.getDrawable(getContext(), drawableRes);
        mCheckedDrawable = ContextCompat.getDrawable(getContext(), checkedDrawableRes);
    }
    /**
     * 方便初始化的方法
     *
     * @param drawableRes        默认状态的图标
     * @param checkedDrawableRes 选中状态的图标
     */
    public void initialize(@DrawableRes int drawableRes, @DrawableRes int checkedDrawableRes, String titleRes) {
        mDefaultDrawable = ContextCompat.getDrawable(getContext(), drawableRes);
        mCheckedDrawable = ContextCompat.getDrawable(getContext(), checkedDrawableRes);
        mTitleRes =  titleRes;
        setTitle(titleRes);
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked) {
            mIcon.setImageDrawable(mCheckedDrawable);
            mTabText.setTextColor(Color.parseColor("#333333"));
        } else {
            mIcon.setImageDrawable(mDefaultDrawable);
            mTabText.setTextColor(Color.parseColor("#a9a9a9"));
        }
        mChecked = checked;
    }

    @Override
    public void setMessageNumber(int number) {
        mMessages.setMessageNumber(number);
    }

    @Override
    public void setHasMessage(boolean hasMessage) {
        mMessages.setHasMessage(hasMessage);
    }

    @Override
    public void setTitle(String title) {
        mTabText.setText(title);
    }

    @Override
    public void setDefaultDrawable(Drawable drawable) {
        mDefaultDrawable = drawable;
        if (!mChecked) {
            mIcon.setImageDrawable(drawable);
        }
    }

    @Override
    public void setSelectedDrawable(Drawable drawable) {
        mCheckedDrawable = drawable;
        if (mChecked) {
            mIcon.setImageDrawable(drawable);
        }
    }

    @Override
    public String getTitle() {
        return  mTitleRes;
    }
}
