package com.code.codemercenaries.girdthysword;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
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
    long numOfChapters;
    BCustomListAdapter2 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.default_font))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
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

        if(getIntent().getExtras() !=null)
            bookName = getIntent().getExtras().getString("EXTRA_BOOK_NAME");
        else{
            systemPreferences = getSharedPreferences(SYSTEM_PREF,0);
            bookName = systemPreferences.getString("curr_book_name","Genesis");
        }

        /*if(customTitleSupported){
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.activity_chapter_list);
            bt.setText(bookName);
        }*/

        if(bookName!=null){
            bt.setText(bookName);
            Meta meta = new Meta();
            chapterDetails = new ArrayList<>();
            for (int i = 0; i < meta.numOfChap.get(meta.bookItems.indexOf(bookName)); i++) {
                chapterDetails.add(new ChapterDetail());
            }
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
        /*List<String> objects = new ArrayList<String>();

        int n = dbHandler.getNumofChap(bookName);

        for(int i=0;i<n;i++){
            objects.add(bookName);
        }
        */

        /*final ProgressDialog progressDialog = ProgressDialog.show(ChapterListActivity.this, "",
                "Loading User data. Please wait...", true);

        if (isNetworkAvailable()) {
            progressDialog.show();
        }*/

        final ProgressDialog progressDialog1 = ProgressDialog.show(ChapterListActivity.this, "",
                "Loading Static data. Please wait...", true);

        if (isNetworkAvailable()) {
            progressDialog1.show();
        }

        DatabaseReference bible = FirebaseDatabase.getInstance().getReference("bible").child(bookName).child("chapters");
        bible.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    numOfChapters = dataSnapshot.getChildrenCount();
                    for (int i = 0; i < numOfChapters; i++) {
                        //ChapterDetail chapterDetail = new ChapterDetail(bookName,i+1,(int)dataSnapshot.child(String.valueOf(i)).child("verses").getChildrenCount());
                        chapterDetails.get(i).setBookName(bookName);
                        chapterDetails.get(i).setChapNum(i + 1);
                        chapterDetails.get(i).settotalVerses((int) dataSnapshot.child(String.valueOf(i)).child("verses").getChildrenCount());
                        //chapterDetails.add(chapterDetail);
                        Log.d("ChapterDetail:", chapterDetails.get(i).toString());
                    }
                    Log.d("Firebase:", "Static book data updated");
                    progressDialog1.dismiss();
                } else {
                    progressDialog1.dismiss();
                    Toast.makeText(ChapterListActivity.this, "Update is not available", Toast.LENGTH_LONG).show();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Database Error:", databaseError.toString());
                progressDialog1.dismiss();
            }
        });

        /*DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("user-bible").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(bookName);
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (int i = 0; i < chapterDetails.size(); i++) {
                        chapterDetails.get(i).setversesMemorized((int) dataSnapshot.child(String.valueOf(i + 1)).getChildrenCount());
                        chapterDetails.get(i).setPercentage((chapterDetails.get(i).getversesMemorized() / chapterDetails.get(i).gettotalVerses()) * 100);
                    }
                    Log.d("ChapterDetail-User:", "Do the needful");
                } else {
                    //Do nothing
                    Log.d("ChapterDetail-User:", "Do nothing");
                }
                chapList.setAdapter(adapter);
                Log.d("Firebase:", "Dynamic book data updated");
                progressDialog.dismiss();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Database Error:", databaseError.toString());
                progressDialog.dismiss();
            }
        });*/

        for (int i = 0; i < chapterDetails.size(); i++) {
            chapterDetails.get(i).setversesMemorized(dbHandler.getMemorizedVerses(bookName, i + 1).size());
            chapterDetails.get(i).setPercentage((chapterDetails.get(i).getversesMemorized() / chapterDetails.get(i).gettotalVerses()) * 100);
        }

        adapter = new BCustomListAdapter2(ChapterListActivity.this, R.layout.bible_custom_list2, chapterDetails);
        chapList.setAdapter(adapter);

        chapList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ChapterListActivity.this,VerseListActivity.class);
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
            Intent intent = new Intent(ChapterListActivity.this,BibleActivity.class);
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    class Meta {
        public List<String> bookItems = new ArrayList<>(Arrays.asList("Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy", "Joshua",
                "Judges", "Ruth", "1 Samuel", "2 Samuel", "1 Kings", "2 Kings", "1 Chronicles",
                "2 Chronicles", "Ezra", "Nehemiah", "Esther", "Job", "Psalms", "Proverbs", "Ecclesiastes",
                "Song of Solomon", "Isaiah", "Jeremiah", "Lamentations", "Ezekiel", "Daniel", "Hosea",
                "Joel", "Amos", "Obadiah", "Jonah", "Micah", "Nahum", "Habakkuk", "Zephaniah", "Haggai",
                "Zechariah", "Malachi", "Matthew", "Mark", "Luke", "John", "Acts", "Romans",
                "1 Corinthians", "2 Corinthians", "Galatians", "Ephesians", "Philippians", "Colossians",
                "1 Thessalonians", "2 Thessalonians", "1 Timothy", "2 Timothy", "Titus", "Philemon",
                "Hebrews", "James", "1 Peter", "2 Peter", "1 John", "2 John", "3 John", "Jude", "Revelation"));

        public List<Integer> numOfChap = new ArrayList<>(Arrays.asList(50, 40, 27, 36, 34, 24, 21, 4, 31, 24, 22, 25, 29, 36, 10, 13, 10, 42, 150, 31, 12, 8, 66, 52, 5, 48,
                12, 14, 3, 9, 1, 4, 7, 3, 3, 3, 2, 14, 4, 28, 16, 24, 21, 28, 16, 16, 13, 6, 6, 4, 4, 5, 3, 6, 4, 3, 1, 13, 5, 5, 3, 5, 1, 1, 1, 22));
    }
}
