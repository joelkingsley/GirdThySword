package com.code.codemercenaries.girdthysword;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.code.codemercenaries.girdthysword.Database.DBHandler;
import com.code.codemercenaries.girdthysword.Font.FontHelper;
import com.code.codemercenaries.girdthysword.Objects.LeaderboardUser;
import com.code.codemercenaries.girdthysword.Objects.Version;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainScreenActivity extends AppCompatActivity {

    TextToSpeech tts;
    String SYSTEM_PREF = "system_pref";
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new FontHelper(this).initialize();

        setContentView(R.layout.activity_main_screen);

        SharedPreferences systemPreferences = getSharedPreferences(SYSTEM_PREF, 0);

        /*if(!systemPreferences.getBoolean("first_time",false)){
            tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status != TextToSpeech.ERROR){
                        tts.setLanguage(Locale.UK);
                        tts.speak("This is the main menu. You can either read and listen to the Bible, or memorize verses by clicking on the retain button.", TextToSpeech.QUEUE_ADD, null);
                    }
                }
            });
            systemPreferences.edit().putBoolean("first_time", true).apply();
        }*/

        DBHandler dbHandler = new DBHandler(this);
        List<Version> versionList = dbHandler.getVersions();
        int versesMemorized = 0;
        for (Version version : versionList) {
            versesMemorized += dbHandler.getTotalNumberOfVersesMemorized(version.get_id());
        }
        systemPreferences.edit().putLong("verses_memorized", versesMemorized).commit();

        mAuth = FirebaseAuth.getInstance();
        DatabaseReference leaderboard = FirebaseDatabase.getInstance().getReference("leaderboard").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        leaderboard.setValue(new LeaderboardUser(0, mAuth.getCurrentUser().getPhotoUrl().toString(), mAuth.getCurrentUser().getDisplayName(), 1, "Beginner", -systemPreferences.getLong("verses_memorized", 0)));

        InputStream ims;
        try {
            ImageView logo = findViewById(R.id.logo);
            CardView readCard = findViewById(R.id.readCard);
            CardView memorizeCard = findViewById(R.id.memorizeCard);
            TextView read = findViewById(R.id.read);
            TextView memorize = findViewById(R.id.memorize);
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

            readCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainScreenActivity.this, SelectVersionActivity.class));
                }
            });

            memorizeCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainScreenActivity.this, HomeScreenActivity.class));
                }
            });

            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainScreenActivity.this, SettingsScreenActivity.class));
                }
            });

            help.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainScreenActivity.this, HelpScreenActivity.class));
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
