package com.code.codemercenaries.girdthysword;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.code.codemercenaries.girdthysword.Font.FontHelper;

import java.io.IOException;
import java.io.InputStream;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new FontHelper(this).initialize();

        setContentView(R.layout.activity_main_screen);

        InputStream ims;
        try {
            ImageView logo = findViewById(R.id.logo);
            Button read = findViewById(R.id.read);
            Button memorize = findViewById(R.id.memorize);
            Button help = findViewById(R.id.help);
            Button settings = findViewById(R.id.settings);

            Typeface buttonFont = Typeface.createFromAsset(getAssets(), getString(R.string.notosans_eng_black_font));
            read.setTypeface(buttonFont);
            memorize.setTypeface(buttonFont);
            help.setTypeface(buttonFont);
            settings.setTypeface(buttonFont);

            ims = getAssets().open("dark_logo.png");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            logo.setImageDrawable(d);

            read.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainScreenActivity.this, SelectVersionActivity.class));
                }
            });

            memorize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainScreenActivity.this, HomeScreenActivity.class));
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
