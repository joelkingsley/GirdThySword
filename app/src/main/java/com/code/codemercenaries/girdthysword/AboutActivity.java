package com.code.codemercenaries.girdthysword;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AboutActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final String SETTINGS_PREF = "settings_pref";
    Button report;
    Button feedback;
    Button source;
    Button rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSharedPreferences(SETTINGS_PREF, 0).getString("font", getString(R.string.default_font_name)).equals(getString(R.string.gnuolane_font_name))) {
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath(getString(R.string.gnuolane_font))
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );
        } else if (getSharedPreferences(SETTINGS_PREF, 0).getString("font", getString(R.string.default_font_name)).equals(getString(R.string.coolvetica_font_name))) {
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath(getString(R.string.coolvetica_font))
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );
        }
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        MenuItem tools = menu.findItem(R.id.nav_title_about);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.whiteText), 0, s.length(), 0);
        tools.setTitle(s);

        rate = (Button) findViewById(R.id.rate);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });

        report = (Button) findViewById(R.id.report);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "codemercenaries@gmail.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "[GirdThySword-Android] Bug Report");
                    intent.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(AboutActivity.this, "No application can handle this request."
                            + "Please install an email client", Toast.LENGTH_LONG);
                    e.printStackTrace();
                }
            }
        });

        feedback = (Button) findViewById(R.id.feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "codemercenaries@gmail.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "[GirdThySword-Android] Feedback");
                    intent.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(AboutActivity.this, "No application can handle this request."
                            + "Please install an email client", Toast.LENGTH_LONG);
                    e.printStackTrace();
                }
            }
        });

        source = (Button) findViewById(R.id.source);
        source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/CodeMercenaries/GirdThySword"));
                    startActivity(browserIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(AboutActivity.this, "No application can handle this request."
                            + "Please install a web browser", Toast.LENGTH_LONG);
                    e.printStackTrace();
                }

            }
        });

        /*new ShowcaseView.Builder(AboutActivity.this, true)
                .setTarget(new ViewTarget(R.id.imageView2,this))
                .setContentTitle("Logo")
                .setContentText("content").build();*/

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(AboutActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(AboutActivity.this, HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_bible) {
            Intent intent = new Intent(AboutActivity.this, BibleActivity.class);
            startActivity(intent);
        } /*else if (id == R.id.nav_rewards) {
            Intent intent = new Intent(BibleActivity.this,RewardsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_statistics) {
            Intent intent = new Intent(BibleActivity.this,StatsActivity.class);
            startActivity(intent);
        }*/ else if (id == R.id.nav_profile) {
            Intent intent = new Intent(AboutActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(AboutActivity.this, HelpActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(AboutActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
