package com.vansuita.pickimage.sample.act;

import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by jrvansuita on 29/01/17.
 */

public class UI {


    public static void setSummaryForGroups(PreferenceGroup preferenceGroup) {
        for (int i = 0; i < preferenceGroup.getPreferenceCount(); i++) {
            setSummary(preferenceGroup.getSharedPreferences(), preferenceGroup.getPreference(i));
        }
    }

    public static void setSummary(SharedPreferences sharedPreferences, Preference pref) {
        if (pref instanceof EditTextPreference) {
            pref.setSummary(sharedPreferences.getString(pref.getKey(), ""));
        } else if (pref instanceof CheckBoxPreference) {
            ((CheckBoxPreference) pref).setChecked(sharedPreferences.getBoolean(pref.getKey(), false));
        } else if (pref instanceof ListPreference) {
            pref.setSummary(((ListPreference) pref).getEntry());
        }
    }

    public static void setDynamicHeight(ListView mListView) {
        ListAdapter mListAdapter = mListView.getAdapter();
        if (mListAdapter == null) {
            // when adapter is null
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < mListAdapter.getCount(); i++) {
            View listItem = mListAdapter.getView(i, null, mListView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
        mListView.setLayoutParams(params);
        mListView.requestLayout();
    }


}
