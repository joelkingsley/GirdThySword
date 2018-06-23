package com.code.codemercenaries.girdthysword.ListAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.code.codemercenaries.girdthysword.R;

import java.util.List;

/**
 * Created by Joel Kingsley on 08-11-2017.
 */

public class BCustomListAdapter1 extends ArrayAdapter<String> {
    Activity context;
    int resource;
    List<String> bookNames;

    public BCustomListAdapter1(@NonNull Activity context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        bookNames = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(this.resource, parent, false);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
        TextView heading = (TextView) rowView.findViewById(R.id.heading);
        TextView percent = (TextView) rowView.findViewById(R.id.percent);
        TextView number = (TextView) rowView.findViewById(R.id.number);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_menu_bible);
        imageView.setImageBitmap(bitmap);
        heading.setText(bookNames.get(position));
        percent.setText("");
        number.setText("");

        return rowView;
    }
}
