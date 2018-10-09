package com.code.codemercenaries.girdthysword.Font;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.TextView;

import com.code.codemercenaries.girdthysword.R;

import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Joel Kingsley on 28-04-2018.
 */

public class FontHelper {

    final String SETTINGS_PREF = "settings_pref";
    final String TAMIL_TAG = "tam";
    final String ENG_TAG = "eng";

    Activity activity;

    public FontHelper(Activity activity) {
        this.activity = activity;
    }

    public void initialize() {

        String defaultLanguage = Locale.getDefault().getISO3Language();

        if (defaultLanguage.equals(ENG_TAG)) {
            Log.d("FontHelper:", "Set to English");
            if (activity.getSharedPreferences(SETTINGS_PREF, 0).getString("font", activity.getString(R.string.default_font_name)).equals(activity.getString(R.string.notosans_eng_light_font_name))) {
                Log.d("FontHelper:", "Light");
                CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath(activity.getString(R.string.notosans_eng_light_font))
                        .setFontAttrId(R.attr.fontPath)
                        .build()
                );
            } else if (activity.getSharedPreferences(SETTINGS_PREF, 0).getString("font", activity.getString(R.string.default_font_name)).equals(activity.getString(R.string.notosans_eng_black_font_name))) {
                Log.d("FontHelper:", "Black");
                CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath(activity.getString(R.string.notosans_eng_black_font))
                        .setFontAttrId(R.attr.fontPath)
                        .build()
                );
            }
        } else if (defaultLanguage.equals(TAMIL_TAG)) {
            Log.d("FontHelper:", "Set to Tamil");
            if (activity.getSharedPreferences(SETTINGS_PREF, 0).getString("font", activity.getString(R.string.default_font_name)).equals(activity.getString(R.string.notosans_eng_light_font_name))) {
                Log.d("FontHelper:", "Light");
                CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath(activity.getString(R.string.notosans_tam_light_font))
                        .setFontAttrId(R.attr.fontPath)
                        .build()
                );
            } else if (activity.getSharedPreferences(SETTINGS_PREF, 0).getString("font", activity.getString(R.string.default_font_name)).equals(activity.getString(R.string.notosans_eng_black_font_name))) {
                Log.d("FontHelper:", "Black");
                CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath(activity.getString(R.string.notosans_tam_black_font))
                        .setFontAttrId(R.attr.fontPath)
                        .build()
                );
            }
        }

    }

    public void setBlackLanguage(TextView v, String language) {

        Typeface engBlack = Typeface.createFromAsset(activity.getAssets(), activity.getString(R.string.notosans_eng_black_font));
        Typeface engLight = Typeface.createFromAsset(activity.getAssets(), activity.getString(R.string.notosans_eng_light_font));
        Typeface tamBlack = Typeface.createFromAsset(activity.getAssets(), activity.getString(R.string.notosans_tam_black_font));
        Typeface tamLight = Typeface.createFromAsset(activity.getAssets(), activity.getString(R.string.notosans_tam_light_font));

        if (language.equals(ENG_TAG)) {
            v.setTypeface(engBlack);
        } else if (language.equals(TAMIL_TAG)) {
            v.setTypeface(tamBlack);
        }
    }

    public void setLightLanguage(TextView v, String language) {
        Typeface engBlack = Typeface.createFromAsset(activity.getAssets(), activity.getString(R.string.notosans_eng_black_font));
        Typeface engLight = Typeface.createFromAsset(activity.getAssets(), activity.getString(R.string.notosans_eng_light_font));
        Typeface tamBlack = Typeface.createFromAsset(activity.getAssets(), activity.getString(R.string.notosans_tam_black_font));
        Typeface tamLight = Typeface.createFromAsset(activity.getAssets(), activity.getString(R.string.notosans_tam_light_font));

        if (language.equals(ENG_TAG)) {
            v.setTypeface(engLight);
        } else if (language.equals(TAMIL_TAG)) {
            v.setTypeface(tamLight);
        }
    }

    public void setLanguage(TextView v, String language) {
        Typeface engBlack = Typeface.createFromAsset(activity.getAssets(), activity.getString(R.string.notosans_eng_black_font));
        Typeface engLight = Typeface.createFromAsset(activity.getAssets(), activity.getString(R.string.notosans_eng_light_font));
        Typeface tamBlack = Typeface.createFromAsset(activity.getAssets(), activity.getString(R.string.notosans_tam_black_font));
        Typeface tamLight = Typeface.createFromAsset(activity.getAssets(), activity.getString(R.string.notosans_tam_light_font));

        if (language.equals(ENG_TAG)) {
            if (activity.getSharedPreferences(SETTINGS_PREF, 0).getString("font", activity.getString(R.string.default_font_name)).equals(activity.getString(R.string.notosans_eng_light_font_name))) {
                v.setTypeface(engLight);
            } else if (activity.getSharedPreferences(SETTINGS_PREF, 0).getString("font", activity.getString(R.string.default_font_name)).equals(activity.getString(R.string.notosans_eng_black_font_name))) {
                v.setTypeface(engBlack);
            }
        } else if (language.equals(TAMIL_TAG)) {
            if (activity.getSharedPreferences(SETTINGS_PREF, 0).getString("font", activity.getString(R.string.default_font_name)).equals(activity.getString(R.string.notosans_eng_light_font_name))) {
                v.setTypeface(tamLight);
            } else if (activity.getSharedPreferences(SETTINGS_PREF, 0).getString("font", activity.getString(R.string.default_font_name)).equals(activity.getString(R.string.notosans_eng_black_font_name))) {
                v.setTypeface(tamBlack);
            }
        }
    }
}
