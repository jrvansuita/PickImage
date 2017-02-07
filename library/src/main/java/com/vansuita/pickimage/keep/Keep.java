package com.vansuita.pickimage.keep;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jrvansuita on 07/02/17.
 */

public class Keep {
    SharedPreferences pref;
    private static final String KEEP = "KEEP";
    private static final String ASKED = "ASKED";

    Keep(Context context) {
        this.pref = context.getSharedPreferences(KEEP, Context.MODE_PRIVATE);
    }

    public static Keep with(Context context) {
        return new Keep(context);
    }

    public void asked() {
        pref.edit().putBoolean(ASKED, true).commit();
    }

    public boolean neverAskedYet() {
        return !pref.getBoolean(ASKED, false);
    }

}
