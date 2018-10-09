package com.code.codemercenaries.girdthysword.ListAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.code.codemercenaries.girdthysword.Database.DBHandler;
import com.code.codemercenaries.girdthysword.Font.FontHelper;
import com.code.codemercenaries.girdthysword.Objects.Chunk;
import com.code.codemercenaries.girdthysword.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel Kingsley on 05-10-2017.
 */

//Custom List Adapter for Today and Overdue lists in TabHost

public class CustomListAdapter1 extends ArrayAdapter<Chunk> {
    Activity context;
    int resource;
    List<Chunk> objects = new ArrayList<Chunk>();
    List<String> heading = new ArrayList<String>();
    List<String> subheading = new ArrayList<String>();

    public CustomListAdapter1(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Chunk> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        DBHandler dbHandler = new DBHandler(context);
        /*DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sections").child(FirebaseAuth.getInstance().getCurrentUser().getUid());*/

        for (int i = 0; i < objects.size(); i++) {
            this.objects.add(objects.get(i));
            this.heading.add(objects.get(i).toString());
            this.subheading.add(objects.get(i).getBookName() + " " + objects.get(i).getChapNum() + ":" + dbHandler.retSection(objects.get(i).getSecId()).get_start_verse_num() + "-" + dbHandler.retSection(objects.get(i).getSecId()).get_end_verse_num());
            Log.d("Subheading:", String.valueOf(objects.get(i).getSecId()));
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(this.resource, parent, false);



        if (this.resource == R.layout.chunk_custom_list1) {
            ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
            TextView heading = (TextView) rowView.findViewById(R.id.heading);
            TextView subheading = (TextView) rowView.findViewById(R.id.subheading);
            TextView version = rowView.findViewById(R.id.version);

            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_menu_bible);
            imageView.setImageBitmap(bitmap);

            FontHelper fontHelper = new FontHelper(context);

            if (objects.get(position).get_version().equals("tam_org")) {
                fontHelper.setBlackLanguage(heading, context.getString(R.string.tamil_iso));
                fontHelper.setLightLanguage(subheading, context.getString(R.string.tamil_iso));
            } else if (objects.get(position).get_version().equals("en_kjv")) {
                fontHelper.setBlackLanguage(heading, context.getString(R.string.english_iso));
                fontHelper.setLightLanguage(subheading, context.getString(R.string.english_iso));
            }

            heading.setText(this.heading.get(position));
            subheading.setText(this.subheading.get(position));
            version.setText(this.objects.get(position).get_version());
        } else if (this.resource == R.layout.chunk_custom_list2) {
            /*ImageView imageView = (ImageView) rowView.findViewById(R.id.image);*/
            TextView heading = (TextView) rowView.findViewById(R.id.heading);
            TextView subheading = (TextView) rowView.findViewById(R.id.subheading);
            TextView date = (TextView) rowView.findViewById(R.id.date);
            TextView version = rowView.findViewById(R.id.version);

            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_menu_bible);
            /*imageView.setImageBitmap(bitmap);*/

            FontHelper fontHelper = new FontHelper(context);

            if (objects.get(position).get_version().equals("tam_org")) {
                fontHelper.setBlackLanguage(heading, context.getString(R.string.tamil_iso));
                fontHelper.setLightLanguage(subheading, context.getString(R.string.tamil_iso));
            } else if (objects.get(position).get_version().equals("en_kjv")) {
                fontHelper.setBlackLanguage(heading, context.getString(R.string.english_iso));
                fontHelper.setLightLanguage(subheading, context.getString(R.string.english_iso));
            }

            heading.setText(this.heading.get(position));
            subheading.setText(this.subheading.get(position));
            String auxDate = this.objects.get(position).getNextDateOfReview();
            if (auxDate.equals("NA")) {
                auxDate = "Not Scheduled";
            }
            date.setText(auxDate);
            version.setText(this.objects.get(position).get_version());
        }

        return rowView;
    }
}
