package com.code.codemercenaries.girdthysword;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.code.codemercenaries.girdthysword.Font.FontHelper;
import com.code.codemercenaries.girdthysword.Fragments.HomeFragment;
import com.code.codemercenaries.girdthysword.Fragments.LeaderboardFragment;
import com.code.codemercenaries.girdthysword.Fragments.SettingsFragment;
import com.code.codemercenaries.girdthysword.Fragments.StatsFragment;

import java.io.IOException;
import java.io.InputStream;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new FontHelper(this).initialize();

        setContentView(R.layout.activity_home_screen);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
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
                            case R.id.action_settings:
                                selectedFragment = new SettingsFragment();
                                break;
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
}
