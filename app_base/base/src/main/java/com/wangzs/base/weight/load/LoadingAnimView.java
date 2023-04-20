package com.wangzs.base.weight.load;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.wangzs.base.R;

/**
 * @Description
 * @Date 2022/4/25 025 13:38
 * @Created by wangzs
 */
public class LoadingAnimView extends RelativeLayout {
    private ObjectAnimator mRotationAnimator;

    public LoadingAnimView(Context context) {
        this(context, null);
    }

    public LoadingAnimView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_loading_image, this);
        ImageView imageView = findViewById(R.id.iv_view_loading_anim_top);
        mRotationAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0, 359f);
        mRotationAnimator.setInterpolator(new LinearInterpolator());
        mRotationAnimator.setDuration(1000);
        mRotationAnimator.setRepeatCount(ObjectAnimator.INFINITE);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mRotationAnimator != null) {
            mRotationAnimator.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mRotationAnimator != null && mRotationAnimator.isRunning()) {
            mRotationAnimator.cancel();
        }
        clearAnimation();
    }
}
