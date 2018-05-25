package com.code.codemercenaries.girdthysword.Font;

import android.app.Activity;

import com.code.codemercenaries.girdthysword.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Joel Kingsley on 28-04-2018.
 */

public class FontHelper {

    final String SETTINGS_PREF = "settings_pref";

    Activity activity;

    public FontHelper(Activity activity) {
        this.activity = activity;
    }

    public void initialize() {
        if (activity.getSharedPreferences(SETTINGS_PREF, 0).getString("font", activity.getString(R.string.default_font_name)).equals(activity.getString(R.string.notosans_eng_light_font_name))) {
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath(activity.getString(R.string.notosans_eng_light_font))
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );
        } else if (activity.getSharedPreferences(SETTINGS_PREF, 0).getString("font", activity.getString(R.string.default_font_name)).equals(activity.getString(R.string.notosans_eng_black_font_name))) {
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath(activity.getString(R.string.notosans_eng_black_font))
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );
        }
    }
}
