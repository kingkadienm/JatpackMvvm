package com.wangzs.base.weight;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.wangzs.base.R;

public class ColorUtil {

    /**
     * Drawable 染色
     *
     * @param drawable 染色对象
     * @param color    颜色
     * @return 染色后的资源
     */
    public static Drawable tinting(Drawable drawable, int color) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        wrappedDrawable.mutate();
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }

    public static Drawable newDrawable(Drawable drawable) {
        Drawable.ConstantState constantState = drawable.getConstantState();
        return constantState != null ? constantState.newDrawable() : drawable;
    }

    /**
     * 通过 ID 获取色值
     *
     * @param context
     * @param resId
     * @return
     */
    public static int getColorByResId(Context context, int resId) {
        return ContextCompat.getColor(context, resId);
    }

    /**
     * 获取colorPrimary的颜色,需要V7包的支持
     *
     * @param context 上下文
     * @return 0xAARRGGBB
     */
    public static int getColorPrimary(Context context) {
        return getColorByResId(context, R.color.colorPrimary);
    }

    /**
     * 根据attr获取颜色，主要用于颜色主题的获取
     *
     * @param context
     * @param colorAttr
     * @return
     */
    public static int getColorByAttr(Context context, String colorAttr) {
        return getColorByResId(context, R.color.colorPrimary);
    }

    /**
     * 获取自定义属性的资源ID
     *
     * @param context 上下文
     * @param attrRes 自定义属性
     * @return resourceId
     */
    private static int getResourceId(Context context, int attrRes) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attrRes, typedValue, true);
        return typedValue.resourceId;
    }

}
