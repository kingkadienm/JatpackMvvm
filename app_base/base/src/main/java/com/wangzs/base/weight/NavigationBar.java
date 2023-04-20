package com.wangzs.base.weight;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.wangzs.base.R;
import com.wangzs.base.toolskit.StatusBarUtil;


/**
 * 统一标题栏
 * Created by wangzs on 2021/1/7.
 */
public class NavigationBar extends RelativeLayout implements View.OnClickListener {

    private Context mContext;
    LinearLayout mRootLayout;
    RelativeLayout mParentLayout, mRightLayout;
    View mLineView;
    ImageView mBackIv;
    TextView mTitleTv;

    public NavigationBar(Context context) {
        this(context, null);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        inflate(context, R.layout.view_navigation_bar, this);
        mRootLayout = findViewById(R.id.ll_navigation_root);
        mParentLayout = findViewById(R.id.rl_navigation_parent);
        mLineView = findViewById(R.id.view_common_title_line);
        mBackIv = findViewById(R.id.iv_common_title_back);
        mTitleTv = findViewById(R.id.iv_common_title_text);
        mRightLayout = findViewById(R.id.rl_navigation_right_parent);
//        ((LinearLayout.LayoutParams) mParentLayout.getLayoutParams()).topMargin = StatusBarUtil.getStatusBarHeight(mContext);
        mBackIv.setOnClickListener(this);
     }

    /**
     * 设置沉浸式
     */
    public void initTitleBar() {
        // 沉浸式设置，使用下面的标题栏的时候必须重新设置fitsSystemWindows为false

    }

    public void setLeftBackIconShow(boolean show) {
        if (mBackIv != null) {
            mBackIv.setVisibility(show ? VISIBLE : GONE);
        }
    }

    public void setTitle(int titleRes) {
        if (mTitleTv != null) {
            mTitleTv.setText((getContext().getString(titleRes)));
        }
    }

    public void setTitle(String title) {
        if (mTitleTv != null) {
            mTitleTv.setText((title));
        }
    }

    public void setBottomLineVisibility(int visible) {
        if (mLineView != null) {
            mLineView.setVisibility(visible);
        }
    }

    public void setRightText(TextView textView) {
        if (mRightLayout != null && textView != null) {
            mRightLayout.removeAllViews();
            mRightLayout.addView(textView);
        }
    }

    public void setRightText(String text) {
        if (mRightLayout != null) {
            mRightLayout.removeAllViews();
            if (StringUtils.isEmpty(text)) {
                return;
            }
            TextView textView = new TextView(getContext());
            textView.setText(text);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            textView.setTextColor(ContextCompat.getColor(getContext(), com.wangzs.res.R.color.color_333333));
            textView.setPadding(dp2px(10), dp2px(5), dp2px(10), dp2px(5));
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin = dp2px(2);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            textView.setLayoutParams(params);
            mRightLayout.addView(textView);
        }
    }

    public void setRightImage(int imageRes) {
        if (mRightLayout != null) {
            mRightLayout.removeAllViews();
            if (imageRes == 0) {
                return;
            }
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(imageRes);
            imageView.setPadding(dp2px(5), dp2px(5), dp2px(5), dp2px(5));
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin = dp2px(7);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            imageView.setLayoutParams(params);
            mRightLayout.addView(imageView);
        }
    }

    public void setRightView(View rightView) {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        rightView.setLayoutParams(params);
        mRightLayout.addView(rightView);
    }

    public View getRightView() {
        return mRightLayout;
    }

    public void setRightClickListener(OnClickListener listener) {
        if (mRightLayout != null) {
            mRightLayout.setOnClickListener(listener);
        }
    }

    public void setLeftClickListener(OnClickListener listener) {
        if (mBackIv != null) {
            mBackIv.setOnClickListener(listener);
        }
    }

    public void setTitleBackground(int resId) {
        if (mRootLayout != null) {
            setTitleBackground(resId == 0 ? null : ContextCompat.getDrawable(getContext(), resId));
        }
    }

    public void setTitleBackground(Drawable drawable) {
        if (mRootLayout != null) {
            mRootLayout.setBackground(drawable);
        }
    }


    public void setBackClick(OnClickListener listener) {
        mBackIv.setOnClickListener(listener);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_common_title_back) {
            ((Activity) mContext).finish();
        }
    }

    public int dp2px(float value) {
        return SizeUtils.dp2px(value);
    }
}