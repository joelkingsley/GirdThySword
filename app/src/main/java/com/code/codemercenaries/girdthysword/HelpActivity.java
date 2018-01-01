package com.code.codemercenaries.girdthysword;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HelpActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String SETTINGS_PREF = "settings_pref";
        if (getSharedPreferences(SETTINGS_PREF, 0).getString("font", getString(R.string.default_font_name)).equals(getString(R.string.default_font_name))) {
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath(getString(R.string.default_font))
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );
        }
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        questionList = (ListView) findViewById(R.id.questions);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        MenuItem tools = menu.findItem(R.id.nav_title_about);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.whiteText), 0, s.length(), 0);
        tools.setTitle(s);

        setupList();
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
        getMenuInflater().inflate(R.menu.help, menu);
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
            Intent intent = new Intent(HelpActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(HelpActivity.this, HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_bible) {
            Intent intent = new Intent(HelpActivity.this, BibleActivity.class);
            startActivity(intent);
        } /*else if (id == R.id.nav_rewards) {
            Intent intent = new Intent(ChapterListActivity.this,RewardsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_statistics) {
            Intent intent = new Intent(ChapterListActivity.this,StatsActivity.class);
            startActivity(intent);
        }*/ else if (id == R.id.nav_profile) {
            Intent intent = new Intent(
                    HelpActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(HelpActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(HelpActivity.this, AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupList() {

        List<String> questions = new ArrayList<>(Arrays.asList("I'm confused. Where do I get an overview?"
                , "How do I add new sections to memorize?"
                , "How do I know when to review a particular chunk?"
                , "The speech recognition seems to be very inaccurate."
                , "How do I report a bug or request a feature?"));

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, questions);
        questionList.setAdapter(adapter);
        questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HelpActivity.this, AnswerActivity.class);
                intent.putExtra("EXTRA_QUES_ID", position + 1);
                startActivity(intent);
            }
        });
    }
}
