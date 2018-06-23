package com.code.codemercenaries.girdthysword.ListAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.code.codemercenaries.girdthysword.R;
import com.code.codemercenaries.girdthysword.ReadableVerse;

import java.util.List;

/**
 * Created by Joel Kingsley on 12-11-2017.
 */

public class BCustomListAdapter3 extends ArrayAdapter<ReadableVerse> {
    Activity context;
    int resource;

    List<ReadableVerse> verses;

    public BCustomListAdapter3(@NonNull Activity context, @LayoutRes int resource, @NonNull List<ReadableVerse> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;

        verses = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(this.resource, parent, false);

        TextView number = (TextView) rowView.findViewById(R.id.number);
        TextView text = (TextView) rowView.findViewById(R.id.text);

        number.setText(String.valueOf(position + 1));
        text.setText(verses.get(position).get_verse_text());

        int memory = verses.get(position).get_memory();

        Log.d("BibleVerses:", "memory = " + memory);

        switch (memory) {
            case 0:
                break;
            case 2:
                rowView.setBackgroundColor(Color.parseColor("#edffa0"));
                break;
            case 3:
                rowView.setBackgroundColor(Color.parseColor("#a0ffb6"));
                break;
            default:
                Log.d("BCustomListAdapter3", "Invalid Memory");
                break;
        }

        return rowView;
    }
}
