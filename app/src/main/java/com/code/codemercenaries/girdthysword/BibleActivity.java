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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BibleActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TabHost tabHost;
    ListView ot;
    ListView nt;

    String[] otBooks = new String[]{"Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy", "Joshua",
            "Judges", "Ruth", "1 Samuel", "2 Samuel", "1 Kings", "2 Kings", "1 Chronicles",
            "2 Chronicles", "Ezra", "Nehemiah", "Esther", "Job", "Psalms", "Proverbs", "Ecclesiastes",
            "Song of Solomon", "Isaiah", "Jeremiah", "Lamentations", "Ezekiel", "Daniel", "Hosea",
            "Joel", "Amos", "Obadiah", "Jonah", "Micah", "Nahum", "Habakkuk", "Zephaniah", "Haggai",
            "Zechariah", "Malachi"};
    String[] ntBooks = new String[]{"Matthew", "Mark", "Luke", "John", "Acts", "Romans",
            "1 Corinthians", "2 Corinthians", "Galatians", "Ephesians", "Philippians", "Colossians",
            "1 Thessalonians", "2 Thessalonians", "1 Timothy", "2 Timothy", "Titus", "Philemon",
            "Hebrews", "James", "1 Peter", "2 Peter", "1 John", "2 John", "3 John", "Jude", "Revelation"};

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
        setContentView(R.layout.activity_bible);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navigationView.getMenu();
        MenuItem tools= menu.findItem(R.id.nav_title_about);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.whiteText), 0, s.length(), 0);
        tools.setTitle(s);

        navigationView.setNavigationItemSelectedListener(this);

        tabHost = (TabHost) findViewById(R.id.tabhost2);
        ot = (ListView) findViewById(R.id.ot_list);
        nt = (ListView) findViewById(R.id.nt_list);

        setupTabHost();
        setupLists();
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
        getMenuInflater().inflate(R.menu.bible, menu);
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
            Intent intent = new Intent(BibleActivity.this, SettingsActivity.class);
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
            Intent intent = new Intent(BibleActivity.this,HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_bible) {

        } /*else if (id == R.id.nav_rewards) {
            Intent intent = new Intent(BibleActivity.this,RewardsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_statistics) {
            Intent intent = new Intent(BibleActivity.this,StatsActivity.class);
            startActivity(intent);
        }*/ else if (id == R.id.nav_profile) {
            Intent intent = new Intent(BibleActivity.this,ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(BibleActivity.this, HelpActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(BibleActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(BibleActivity.this, AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupTabHost() {

        TabHost.TabSpec spec; // Reusable TabSpec for each tab
        tabHost.setup();

        spec = tabHost.newTabSpec("Old Testament");
        spec.setIndicator("OLD TESTAMENT");
        spec.setContent(R.id.tab1);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("New Testament");
        spec.setIndicator("NEW TESTAMENT");
        spec.setContent(R.id.tab2);
        tabHost.addTab(spec);


        //set tab which one you want to open first time 0 or 1 or 2
        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch(tabId){
                    case "Old Testament":
                        break;
                    case "New Testament":
                        break;
                    default:
                }
            }
        });
    }

    private void setupLists(){
        BCustomListAdapter1 otAdapter = new BCustomListAdapter1(this, R.layout.bible_custom_list1, otBooks);
        ot.setAdapter(otAdapter);
        ot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent  = new Intent(BibleActivity.this,ChapterListActivity.class);
                intent.putExtra("EXTRA_BOOK_NAME",otBooks[i]);
                /*final ProgressDialog dialog = ProgressDialog.show(BibleActivity.this, "",
                        "Loading. Please wait...", true);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                },3000);*/
                startActivity(intent);

            }
        });

        BCustomListAdapter1 ntAdapter = new BCustomListAdapter1(this, R.layout.bible_custom_list1, ntBooks);
        nt.setAdapter(ntAdapter);
        nt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent  = new Intent(BibleActivity.this,ChapterListActivity.class);
                intent.putExtra("EXTRA_BOOK_NAME",ntBooks[i]);
                Log.d("Intent call:","to ChapterList from Bible, Extra:"+ntBooks[i]);
                startActivity(intent);
            }
        });
    }


}
