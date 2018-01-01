package com.code.codemercenaries.girdthysword;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.VideoView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    final String INDEX_PREF = "index_pref";
    final String SETTINGS_PREF = "settings_pref";
    final String SYSTEM_PREF = "system_pref";
    VideoView videoView;
    String[] bookNames = {"Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy", "Joshua",
            "Judges", "Ruth", "1 Samuel", "2 Samuel", "1 Kings", "2 Kings", "1 Chronicles",
            "2 Chronicles", "Ezra", "Nehemiah", "Esther", "Job", "Psalms", "Proverbs", "Ecclesiastes",
            "Song of Solomon", "Isaiah", "Jeremiah", "Lamentations", "Ezekiel", "Daniel", "Hosea",
            "Joel", "Amos", "Obadiah", "Jonah", "Micah", "Nahum", "Habakkuk", "Zephaniah", "Haggai",
            "Zechariah", "Malachi", "Matthew", "Mark", "Luke", "John", "Acts", "Romans",
            "1 Corinthians", "2 Corinthians", "Galatians", "Ephesians", "Philippians", "Colossians",
            "1 Thessalonians", "2 Thessalonians", "1 Timothy", "2 Timothy", "Titus", "Philemon",
            "Hebrews", "James", "1 Peter", "2 Peter", "1 John", "2 John", "3 John", "Jude", "Revelation"};
    Integer numOfChap[] = {50,40,27,36,34,24,21,4,31,24,22,25,29,36,10,13,10,42,150,31,12,8,66,52,5,48,
            12,14,3,9,1,4,7,3,3,3,2,14,4,28,16,24,21,28,16,16,13,6,6,4,4,5,3,6,4,3,1,13,5,5,3,5,1,1,1,22};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if(getSharedPreferences(SETTINGS_PREF, 0).getString("font",getString(R.string.default_font_name)).equals(getString(R.string.default_font_name))){
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath(getString(R.string.default_font))
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );
        }*/
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        // Example of a call to a native method
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        videoView =(VideoView) findViewById(R.id.videoView);

        Uri video=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.giphy);
        videoView.setVideoURI(video);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        SharedPreferences systemPreferences = getSharedPreferences(SYSTEM_PREF,0);
        SharedPreferences settingsPreferences = getSharedPreferences(SETTINGS_PREF,0);
        SharedPreferences indexPreferences = getSharedPreferences(INDEX_PREF,0);

        if (systemPreferences.getBoolean("initiated", true)) {
            //the app is being launched for first time, do something

            final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "",
                    "Loading. Please wait...", true);
            Handler handler = new Handler();
            //loading screen

            Log.d("Comments", "Initiate");

            settingsPreferences.edit().putInt("chunk_size", 3).apply();
            settingsPreferences.edit().putString("theme", "original").apply();
            settingsPreferences.edit().putString("font", getString(R.string.default_font_name)).apply();

            for(int i=0;i<66;i++){
                indexPreferences.edit().putBoolean(bookNames[i], false).apply();
                for(int j=1;j<=numOfChap[i];j++){
                    indexPreferences.edit().putBoolean(bookNames[i] + "_" + j, false).apply();
                }
            }

            // record the fact that the app has been started at least once
            systemPreferences.edit().putBoolean("initiated", false).apply();

            //dismiss loading screen
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            });
        }
        else{
        }
        videoView.start();
    }

    /*@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }*/

    @Override
    protected void onResume() {
        super.onResume();
    }
}
