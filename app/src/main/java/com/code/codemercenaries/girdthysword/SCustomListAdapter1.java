package com.code.codemercenaries.girdthysword;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Joel Kingsley on 05-12-2017.
 */

public class SCustomListAdapter1 extends ArrayAdapter<String> {

    final String SETTINGS_PREF = "settings_pref";
    final String SYSTEM_PREF = "system_pref";
    int resource;
    Context context;
    Activity activity;
    SharedPreferences settingsPreferences;
    private int settingsItemsSize = 4;


    public SCustomListAdapter1(@NonNull Context context, int resource) {
        super(context, resource);
        this.resource = resource;
        this.context = context;
        settingsPreferences = context.getSharedPreferences(SETTINGS_PREF, 0);
    }

    public SCustomListAdapter1(@NonNull Context context, @NonNull Activity activity, int resource) {
        super(context, resource);
        this.resource = resource;
        this.context = context;
        this.activity = activity;
        settingsPreferences = context.getSharedPreferences(SETTINGS_PREF, 0);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(this.resource, parent, false);

        if (position == 0) {
            TextView title = (TextView) rowView.findViewById(R.id.title);
            TextView desc = (TextView) rowView.findViewById(R.id.desc);
            TextView chunkSize = (TextView) rowView.findViewById(R.id.value);

            int chunk = settingsPreferences.getInt("chunk_size", 3);
            chunkSize.setText(Integer.toString(chunk));

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setChunkSize(v);
                }
            });
        }
        if (position == 1) {
            TextView title = (TextView) rowView.findViewById(R.id.title);
            TextView desc = (TextView) rowView.findViewById(R.id.desc);
            TextView themeValue = (TextView) rowView.findViewById(R.id.value);

            title.setText("Theme");
            desc.setText("Color scheme of the app");

            String theme = settingsPreferences.getString("theme", "original");
            themeValue.setText(theme);

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTheme(v);
                }
            });
        }
        if (position == 2) {
            TextView title = (TextView) rowView.findViewById(R.id.title);
            TextView desc = (TextView) rowView.findViewById(R.id.desc);
            TextView fontValue = (TextView) rowView.findViewById(R.id.value);

            title.setText("Font");
            desc.setText("Typeface of the app");

            String font = settingsPreferences.getString("font", context.getString(R.string.default_font_name));
            fontValue.setText(font);

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setFont(v);
                }
            });
        }
        if (position == 3) {
            TextView title = (TextView) rowView.findViewById(R.id.title);
            TextView desc = (TextView) rowView.findViewById(R.id.desc);
            TextView value = (TextView) rowView.findViewById(R.id.value);

            title.setText("Reset tutorials");
            desc.setText("Resets all the tutorials");
            value.setText("");

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetTutorials(v);
                }
            });
        }
        return rowView;
    }

    @Override
    public int getCount() {
        return settingsItemsSize;
    }

    public void setChunkSize(final View v) {
        CharSequence sizes[] = new CharSequence[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Pick a size");
        builder.setItems(sizes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int size) {

                settingsPreferences.edit().putInt("chunk_size", size + 1).apply();
                Toast.makeText(context, "Chunk Size changed to " + Integer.toString(size + 1),
                        Toast.LENGTH_LONG).show();
                TextView tv = v.findViewById(R.id.value);
                tv.setText(Integer.toString(size + 1));
            }
        });
        builder.show();
    }

    public void setTheme(final View v) {
        final String themes[] = new String[]{"original", "white"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Pick a theme");
        builder.setItems(themes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int pos) {

                settingsPreferences.edit().putString("theme", themes[pos]).apply();
                Toast.makeText(context, "Theme changed to " + themes[pos],
                        Toast.LENGTH_LONG).show();
                TextView tv = v.findViewById(R.id.value);
                tv.setText(themes[pos]);
            }
        });
        builder.show();
    }

    public void setFont(final View v) {
        final String fonts[] = new String[]{context.getString(R.string.default_font_name), "coolvetica_rg"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Pick a font");
        builder.setItems(fonts, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int pos) {

                settingsPreferences.edit().putString("font", fonts[pos]).apply();
                Toast.makeText(context, "Font changed to " + fonts[pos],
                        Toast.LENGTH_LONG).show();
                TextView tv = v.findViewById(R.id.value);
                tv.setText(fonts[pos]);
                activity.finish();
                activity.startActivity(activity.getIntent());
            }
        });
        builder.show();
    }

    public void resetTutorials(final View v) {
        android.support.v7.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new android.support.v7.app.AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new android.support.v7.app.AlertDialog.Builder(context);
        }
        builder.setTitle("Reset tutorials")
                .setMessage("Are you sure you want to reset all tutorials?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        context.getSharedPreferences(SYSTEM_PREF, 0).edit().putBoolean("show_tutorial_nav", true).apply();
                        context.getSharedPreferences(SYSTEM_PREF, 0).edit().putBoolean("show_tutorial_home", true).apply();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
