package com.vansuita.pickimage.sample.act;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.vansuita.pickimage.EPickTypes;
import com.vansuita.pickimage.PickSetup;
import com.vansuita.pickimage.sample.R;

/**
 * Created by jrvansuita on 30/01/17.
 */

public abstract class BaseSampleActivity extends AppCompatActivity {

    private ImageView imageView;
    private ScrollView scrollView;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_layout);

        scrollView = ((ScrollView) findViewById(R.id.scroll));
        imageView = ((ImageView) findViewById(R.id.result_image));

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        getFragmentManager().beginTransaction().replace(R.id.prefs_holder, new SampleActivity.InnerPreferenceFragment()).commit();

        initialize();
    }


    public static class InnerPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            UI.setSummaryForGroups((PreferenceGroup) findPreference(getString(R.string.key_text_preference_group)));
            UI.setSummaryForGroups((PreferenceGroup) findPreference(getString(R.string.key_additional_preference_group)));

            UI.setDynamicHeight((ListView) getView().findViewById(android.R.id.list));
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            UI.setSummary(sharedPreferences, findPreference(key));
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        scrollToTop();
    }

    public void scrollToTop() {
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(scrollView.FOCUS_UP);
            }
        });
    }


    protected void initialize() {
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                imageView.setImageResource(R.mipmap.default_image);
                return true;
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImageViewClick();
            }
        });
    }

    public ImageView getImageView() {
        return imageView;
    }

    protected abstract void onImageViewClick();

    protected String getStr(int key) {
        return prefs.getString(getString(key), "");
    }

    protected int getInt(int key) {
        return prefs.getInt(getString(key), 0);
    }

    protected float getFloat(int key) {
        return Float.parseFloat(prefs.getString(getString(key), "0"));
    }

    protected int getNum(int key) {
        return Integer.parseInt(prefs.getString(getString(key), "0"));
    }

    protected boolean getBool(int key) {
        return prefs.getBoolean(getString(key), false);
    }

    protected void customize(PickSetup setup) {
        setup.setTitle(getStr(R.string.key_dialog_title));
        setup.setTitleColor(getInt(R.string.key_title_color));

        setup.setBackgroundColor(getInt(R.string.key_background_color));

        setup.setProgressText(getStr(R.string.key_progress_text));
        setup.setProgressTextColor(getInt(R.string.key_progress_text_color));

        setup.setCancelText(getStr(R.string.key_cancel_text));
        setup.setCancelTextColor(getInt(R.string.key_cancel_text_color));

        setup.setButtonTextColor(getInt(R.string.key_button_text_color));

        setup.setDimAmount(getFloat(R.string.key_dim_amount));
        setup.setFlip(getBool(R.string.key_flip_image));
        setup.setImageSize(getNum(R.string.key_image_size));

        setup.setPickTypes(EPickTypes.fromInt(getNum(R.string.key_buttons_available)));

        setup.setCameraButtonText(getStr(R.string.key_camera_button_text));
        setup.setGalleryButtonText(getStr(R.string.key_gallery_button_text));
        setup.setIconGravityInt(getNum(R.string.key_icon_gravity));

        setup.setButtonOrientationInt(getNum(R.string.key_buttons_orientation));

        if (getBool(R.string.key_colored_icons)) {
            setup.setGalleryIcon(R.mipmap.gallery_colored);
            setup.setCameraIcon(R.mipmap.camera_colored);
        }
    }
}
