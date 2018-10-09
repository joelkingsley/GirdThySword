package com.code.codemercenaries.girdthysword.Color;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;

import com.code.codemercenaries.girdthysword.R;

/**
 * Created by Joel Kingsley on 30-04-2018.
 */

public class ColorHelper {

    final String SETTINGS_PREF = "settings_pref";

    Activity activity;

    public ColorHelper(Activity activity) {
        this.activity = activity;
    }

    public void setDefaultTheme(android.support.v7.widget.Toolbar toolbar, View back) {

        SharedPreferences settingsPreferences = activity.getSharedPreferences(SETTINGS_PREF, 0);
        String theme = settingsPreferences.getString("theme", "light_grey");
        if (theme.equals("light_grey")) {
            toolbar.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
            back.setBackgroundResource(R.drawable.app_theme_gradient);
        }
    }
}
