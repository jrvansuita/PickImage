package com.vansuita.pickimage.util;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.StateSet;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Created by jrvansuita build 01/11/16.
 */

public class Util {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void background(View v, Drawable d) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackgroundDrawable(d);
        } else {
            v.setBackground(d);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void background(View v, Bitmap b) {
        background(v, new BitmapDrawable(v.getResources(), b));
    }


    public static void setDimAmount(float dim, Dialog dialog) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            dialog.getWindow().setDimAmount(dim);
        } else {
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.dimAmount = dim;
            dialog.getWindow().setAttributes(lp);
        }
    }

    public static void gone(View v, boolean gone) {
        if (gone) {
            v.setVisibility(View.GONE);
        } else {
            v.setVisibility(View.VISIBLE);
        }
    }


    public static void setIcon(TextView tv, int icon, int gravity) {
        int left;
        int right = 0;
        int bottom = 0;
        int top = 0;

        if (gravity > 0) {
            left = gravity == Gravity.LEFT ? icon : 0;
            right = gravity == Gravity.RIGHT ? icon : 0;
            bottom = gravity == Gravity.BOTTOM ? icon : 0;
            top = gravity == Gravity.TOP ? icon : 0;
        } else {
            left = icon;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            tv.setCompoundDrawablesRelativeWithIntrinsicBounds(left, top, right, bottom);
        } else {
            tv.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        }

        if (bottom + top != 0) {
            tv.setGravity(Gravity.CENTER);
        }
    }


    public static Drawable getAdaptiveRippleDrawable(
            int normalColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new RippleDrawable(ColorStateList.valueOf(darker(normalColor)), getRippleMask(normalColor), null);
        } else {
            return getStateListDrawable(normalColor, darker(normalColor));
        }
    }

    private static Drawable getRippleMask(int color) {
        float[] outerRadii = new float[8];
        // 3 is radius of final ripple,
        // instead of 3 you can give required final radius
        Arrays.fill(outerRadii, 3);

        RoundRectShape r = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(r);
        shapeDrawable.getPaint().setColor(color);

        return shapeDrawable;
    }

    private static StateListDrawable getStateListDrawable(
            int normalColor, int pressedColor) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed},
                new ColorDrawable(pressedColor));
        states.addState(new int[]{android.R.attr.state_focused},
                new ColorDrawable(pressedColor));
        states.addState(new int[]{android.R.attr.state_activated},
                new ColorDrawable(pressedColor));
        states.addState(new int[]{}, new ColorDrawable(normalColor));
        states.addState(StateSet.WILD_CARD, new ColorDrawable(normalColor));
        return states;
    }

    public static int darker(int color) {
        int r = Color.red(color);
        int b = Color.blue(color);
        int g = Color.green(color);

        return Color.rgb((int) (r * .9), (int) (g * .9), (int) (b * .9));
    }


}