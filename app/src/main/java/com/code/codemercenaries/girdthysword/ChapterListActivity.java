package com.code.codemercenaries.girdthysword;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.widget.ListView;
import android.widget.TextView;

import com.code.codemercenaries.girdthysword.Database.DBHandler;
import com.code.codemercenaries.girdthysword.Font.FontHelper;
import com.code.codemercenaries.girdthysword.ListAdapters.BCustomListAdapter2;
import com.code.codemercenaries.girdthysword.Objects.ChapterDetail;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ChapterListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final String SYSTEM_PREF = "system_pref";
    SharedPreferences systemPreferences;
    ProgressDialog dialog;
    ListView chapList;
    TextView bt;
    List<ChapterDetail> chapterDetails;
    String bookName;
    int numOfChap;
    BCustomListAdapter2 adapter;
    String version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new FontHelper(this).initialize();
        //final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        setContentView(R.layout.activity_chapter_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        MenuItem tools= menu.findItem(R.id.nav_title_about);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.whiteText), 0, s.length(), 0);
        tools.setTitle(s);

        chapList = (ListView) findViewById(R.id.chapterList);
        bt = (TextView) findViewById(R.id.bookTitle);

        if (getIntent().getExtras() != null) {
            bookName = getIntent().getExtras().getString("EXTRA_BOOK_NAME", "Genesis");
            version = getIntent().getExtras().getString("EXTRA_VERSION", "en_kjv");
        }
        else{
            systemPreferences = getSharedPreferences(SYSTEM_PREF,0);
            bookName = systemPreferences.getString("curr_book_name","Genesis");
            version = systemPreferences.getString("curr_version", "en_kjv");
        }

        /*if(customTitleSupported){
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.activity_chapter_list);
            bt.setText(bookName);
        }*/

        if(bookName!=null){
            bt.setText(bookName);
            setupList();
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setupList() {

        DBHandler dbHandler = new DBHandler(this);

        chapterDetails = new ArrayList<>();

        numOfChap = dbHandler.getNumofChap(version, bookName);
        for (int i = 0; i < numOfChap; i++) {
            chapterDetails.add(new ChapterDetail());
            chapterDetails.get(i).setBookName(bookName);
            chapterDetails.get(i).setChapNum(i + 1);
        }

        new UpdateStats().execute();

        adapter = new BCustomListAdapter2(ChapterListActivity.this, R.layout.bible_custom_list2, chapterDetails);
        chapList.setAdapter(adapter);

        chapList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ChapterListActivity.this,VerseListActivity.class);
                intent.putExtra("EXTRA_VERSION", version);
                intent.putExtra("EXTRA_BOOK_NAME",bookName);
                intent.putExtra("EXTRA_CHAP_NUM",i+1);
                startActivity(intent);
            }
        });

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
        getMenuInflater().inflate(R.menu.chapter_list, menu);
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
            Intent intent = new Intent(ChapterListActivity.this, SettingsActivity.class);
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
            Intent intent = new Intent(ChapterListActivity.this,HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_bible) {
            Intent intent = new Intent(ChapterListActivity.this, SelectVersionActivity.class);
            startActivity(intent);
        } /*else if (id == R.id.nav_rewards) {
            Intent intent = new Intent(ChapterListActivity.this,RewardsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_statistics) {
            Intent intent = new Intent(ChapterListActivity.this,StatsActivity.class);
            startActivity(intent);
        }*/ else if (id == R.id.nav_profile) {
            Intent intent = new Intent(ChapterListActivity.this,ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(ChapterListActivity.this, HelpActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(ChapterListActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(ChapterListActivity.this, AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class UpdateStats extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            DBHandler dbHandler = new DBHandler(getApplicationContext());

            for (int i = 0; i < numOfChap; i++) {
                chapterDetails.get(i).settotalVerses(dbHandler.getNumOfVerse(version, bookName, i + 1));
                chapterDetails.get(i).setversesMemorized(dbHandler.getMemorizedVerses(version, bookName, i + 1).size());
                chapterDetails.get(i).setPercentage((chapterDetails.get(i).getversesMemorized() / chapterDetails.get(i).gettotalVerses()) * 100);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
        }
    }

}
