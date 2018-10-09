package com.code.codemercenaries.girdthysword.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.code.codemercenaries.girdthysword.Database.DBHandler;
import com.code.codemercenaries.girdthysword.Font.FontHelper;
import com.code.codemercenaries.girdthysword.Fragments.HomeFragment;
import com.code.codemercenaries.girdthysword.Fragments.LeaderboardFragment;
import com.code.codemercenaries.girdthysword.Fragments.StatsFragment;
import com.code.codemercenaries.girdthysword.Objects.Version;
import com.code.codemercenaries.girdthysword.R;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeScreenActivity extends AppCompatActivity {

    final String SYSTEM_PREF = "system_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        new FontHelper(this).initialize();

        setContentView(R.layout.activity_home_screen);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user-bible").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.setValue("Works");*/

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ImageView profile = findViewById(R.id.profile);
        TextView displayName = findViewById(R.id.displayName);
        TextView level = findViewById(R.id.level);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String imgurl = mAuth.getCurrentUser().getPhotoUrl().toString();
            Glide.with(this).load(imgurl).into(profile);
            displayName.setText(mAuth.getCurrentUser().getDisplayName());
            level.setText(getResources().getString(R.string.default_level, 1, "Beginner"));
        }

        ImageView logo = findViewById(R.id.logo);
        InputStream ims = null;
        try {
            ims = getAssets().open("dark_logo.png");
            Drawable d = Drawable.createFromStream(ims, null);
            logo.setImageDrawable(d);
            logo.setScaleX(0.6f);
            logo.setScaleY(0.6f);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        android.support.v4.app.Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                selectedFragment = new HomeFragment();
                                break;
                            case R.id.action_stats:
                                selectedFragment = new StatsFragment();
                                break;
                            case R.id.action_leaderboard:
                                selectedFragment = new LeaderboardFragment();
                                break;
                            /*case R.id.action_settings:
                                selectedFragment = new SettingsFragment();
                                break;*/
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new HomeFragment());
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView versesText = findViewById(R.id.verses_memorized);

        SharedPreferences systemPreferences = getSharedPreferences(SYSTEM_PREF, 0);

        DBHandler dbHandler = new DBHandler(getApplicationContext());
        List<Version> versionList = dbHandler.getVersions();

        Long versesMemorized = 0L;
        for (Version version : versionList) {
            versesMemorized += dbHandler.getTotalNumberOfVersesMemorized(version.get_id());
        }
        versesText.setText(Long.toString(versesMemorized));
        systemPreferences.edit().putLong("verses_memorized", versesMemorized).apply();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(HomeScreenActivity.this, SettingsScreenActivity.class));
                break;
            case R.id.action_language:
                Intent languageIntent = new
                        Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(languageIntent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
