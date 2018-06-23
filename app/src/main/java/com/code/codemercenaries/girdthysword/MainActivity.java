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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            settingsPreferences.edit().putString("theme", "light_grey").apply();
            settingsPreferences.edit().putString("font", getString(R.string.default_font_name)).apply();

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
