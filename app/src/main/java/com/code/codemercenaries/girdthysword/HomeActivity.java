package com.code.codemercenaries.girdthysword;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.takusemba.spotlight.OnSpotlightEndedListener;
import com.takusemba.spotlight.OnSpotlightStartedListener;
import com.takusemba.spotlight.SimpleTarget;
import com.takusemba.spotlight.Spotlight;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final String SETTINGS_PREF = "settings_pref";
    final String SYSTEM_PREF = "system_pref";
    CoordinatorLayout coordinatorLayout;
    TabHost tabHost;
    ListView today;
    ListView overdue;
    ListView tomorrow;
    ListView all;
    FloatingActionButton fab;
    FloatingActionButton fab_add;
    FloatingActionButton fab_delete;
    TextView tv_add;
    TextView tv_delete;
    boolean fab_status;
    //RelativeLayout layTab;
    LinearLayout back;
    Toolbar toolbar;
    SharedPreferences settingsPreferences;
    FirebaseAuth mAuth;
    DrawerLayout drawerLayout;

    ViewPager viewPager;
    TabLayout tabLayout;

    TodayFragment todayFragment;
    OverdueFragment overDueFragment;
    AllFragment allFragment;

    String theme;

    List<Chunk> allChunks;
    List<Chunk> todayChunks;
    List<Chunk> overdueChunks;
    List<Chunk> tomorrowChunks;

    String tabTitle[] = {"OVERDUE", "TODAY", "ALL"};
    ArrayList<Integer> remainingCount;

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
        setContentView(R.layout.activity_home);
        settingsPreferences = getSharedPreferences(SETTINGS_PREF, 0);
        theme = settingsPreferences.getString("theme", "original");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        back = (LinearLayout) findViewById(R.id.back);
        /*layTab = (RelativeLayout) findViewById(R.id.layTab);*/
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        /*
        tabHost = (TabHost) findViewById(R.id.tabhost); // initiate TabHost
        today = (ListView) findViewById(R.id.today_list);
        overdue = (ListView) findViewById(R.id.overdue_list);
        tomorrow = (ListView) findViewById(R.id.tomorrow_list);
        all = (ListView) findViewById(R.id.all_list);*/

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        fab_delete = (FloatingActionButton) findViewById(R.id.fab_delete);
        tv_add = (TextView) findViewById(R.id.tv_add);
        tv_delete = (TextView) findViewById(R.id.tv_delete);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navigationView.getMenu();
        MenuItem tools= menu.findItem(R.id.nav_title_about);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.whiteText), 0, s.length(), 0);
        tools.setTitle(s);

        navigationView.setNavigationItemSelectedListener(this);

        //setupTabHost();
        if (getSharedPreferences(SYSTEM_PREF, 0).getBoolean("show_tutorial_nav", true)) {
            startTutorialNav();
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        todayFragment = new TodayFragment();
        overDueFragment = new OverdueFragment();
        allFragment = new AllFragment();
        adapter.addFragment(overDueFragment, "OVERDUE");
        adapter.addFragment(todayFragment, "TODAY");
        adapter.addFragment(allFragment, "ALL");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAuth = FirebaseAuth.getInstance();

        viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar ca = Calendar.getInstance();
        String currDate = df.format(ca.getTime());

        DBHandler dbHandler = new DBHandler(this);
        remainingCount = new ArrayList<>();
        remainingCount.add(dbHandler.getAllChunksThatAreOverdue(currDate).size());
        remainingCount.add(dbHandler.getAllChunksForToday(currDate).size());
        remainingCount.add(0);

        setupTabIcons();

        try {
            setupColors();
            setupLists();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        setupFabs();

    }

    private Point getCenterPointOfView(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        int x = location[0] + view.getWidth() / 2;
        int y = location[1] + view.getHeight() / 2;
        return new Point(x, y);
    }

    private void setupColors() {
        theme = settingsPreferences.getString("theme", "original");
        if (theme.equals("original")) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            back.setBackgroundColor(getResources().getColor(R.color.colorSword));
            //layTab.setBackgroundColor(getResources().getColor(R.color.colorSword));
        } else if (theme.equals("dark")) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorSword));
            //back.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            //layTab.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else if (theme.equals("white")) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            back.setBackgroundColor(getResources().getColor(android.R.color.white));
            //layTab.setBackgroundColor(getResources().getColor(android.R.color.white));
        }
    }


    private void setupLists() throws ParseException {

        DBHandler dbHandler = new DBHandler(this);

        //dbHandler.deleteAllChunks();
        //dbHandler.deleteAllSections();

        /*dbHandler.addChunk(new Chunk(1,"John",3,16,18, "17/10/2017", 1,1, false));
        dbHandler.addChunk(new Chunk(1,"Romans",4,5,7, "18/10/2017", 1,2,false));
        dbHandler.addChunk(new Chunk(1, "Romans",1,1,3, "10/10/2017", 1,3,false));
        dbHandler.addSection(new Section("John",3,16,18,1));
        dbHandler.addSection(new Section("Romans",4,5,7,2));
        dbHandler.addSection(new Section("Romans",1,1,3,3));*/

        allChunks = new ArrayList<Chunk>();
        todayChunks = new ArrayList<Chunk>();
        overdueChunks = new ArrayList<Chunk>();
        tomorrowChunks = new ArrayList<Chunk>();

        /*DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("chunks").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        allChunks.add(d.getValue(Chunk.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("DatabaseError:",databaseError.toString());
            }
        });*/

        /*allChunks = dbHandler.getAllChunks();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar ca = Calendar.getInstance();
        String currDate = df.format(ca.getTime());

        ca.add(Calendar.DATE,1);
        String tomDate = df.format(ca.getTime());

        for (Chunk c : allChunks) {
            Log.d("Reading:",c.toString());

            if(!c.getNextDateOfReview().equals("NA")) {
                Log.d("CurrDate:",currDate);
                Log.d("DateObj:",c.getNextDateOfReview());
                Date dateObj = df.parse(c.getNextDateOfReview());
                Date currDateObj = df.parse(currDate);
                Date tomDateObj = df.parse(tomDate);
                if(currDateObj.equals(dateObj)){
                    Log.d("Today",c.toString());
                    todayChunks.add(c);
                }
                else if(dateObj.before(currDateObj)){
                    overdueChunks.add(c);
                }
                else if(tomDateObj.equals(dateObj)) {
                    tomorrowChunks.add(c);
                }
            }
        }

        for(Chunk c:todayChunks) {
            Log.d("TodayList:",c.toString());
        }
        CustomListAdapter1 todayAdapter = new CustomListAdapter1(this,R.layout.chunk_custom_list1,todayChunks);
        today.setAdapter(todayAdapter);
        today.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HomeActivity.this,ReviewActivity.class);
                String chunkId = todayChunks.get(i).getId();
                intent.putExtra("EXTRA_CHUNK_ID", chunkId);
                startActivity(intent);
            }
        });

        CustomListAdapter1 overdueAdapter = new CustomListAdapter1(this,R.layout.chunk_custom_list1,overdueChunks);
        overdue.setAdapter(overdueAdapter);
        overdue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HomeActivity.this,ReviewActivity.class);
                intent.putExtra("EXTRA_CHUNK_ID", overdueChunks.get(i).getId());
                startActivity(intent);
            }
        });

        CustomListAdapter1 tomorrowAdapter = new CustomListAdapter1(this,R.layout.chunk_custom_list1,tomorrowChunks);
        tomorrow.setAdapter(tomorrowAdapter);

        CustomListAdapter1 allAdapter = new CustomListAdapter1(this,R.layout.chunk_custom_list2,allChunks);
        all.setAdapter(allAdapter);*/

    }

    private View prepareTabView(int pos) {
        View view = getLayoutInflater().inflate(R.layout.custom_tab, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_count = (TextView) view.findViewById(R.id.tv_count);
        tv_title.setText(tabTitle[pos]);
        if (remainingCount.get(pos) > 0) {
            tv_count.setVisibility(View.VISIBLE);
            tv_count.setText("" + remainingCount.get(pos));
        } else
            tv_count.setVisibility(View.GONE);


        return view;
    }

    private void setupTabIcons() {

        for (int i = 0; i < tabTitle.length; i++) {
            tabLayout.getTabAt(i).setCustomView(prepareTabView(i));
        }


    }

    private void setupFabs() {
        fab_status = false;
        fab_hide();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */
                if(fab_status == false) {
                    fab_show();
                    fab_status = true;
                }
                else {
                    fab_hide();
                    fab_status = false;
                }
            }


        });

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,NewSectionActivity.class);
                startActivity(intent);
            }
        });

        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,DeleteSectionActivity.class);
                startActivity(intent);
            }
        });


    }

    private void fab_show() {
        fab_add.show();
        fab_delete.show();
        tv_add.setVisibility(View.VISIBLE);
        tv_delete.setVisibility(View.VISIBLE);
    }

    private void fab_hide() {
        fab_add.hide();
        fab_delete.hide();
        tv_add.setVisibility(View.INVISIBLE);
        tv_delete.setVisibility(View.INVISIBLE);
    }

    /*private void setupTabHost() {

        TabHost.TabSpec spec; // Reusable TabSpec for each tab
        tabHost.setup();

        spec = tabHost.newTabSpec("Today");
        spec.setIndicator("TODAY");
        spec.setContent(R.id.tab1);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Overdue");
        spec.setIndicator("OVERDUE");
        spec.setContent(R.id.tab2);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Tomorrow");
        spec.setIndicator("TOMORROW");
        spec.setContent(R.id.tab3);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("All");
        spec.setIndicator("ALL");
        spec.setContent(R.id.tab4);
        tabHost.addTab(spec);

        if (theme.equals("original")) {
        } else if (theme.equals("white")) {
        } else if (theme.equals("dark")) {
            for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                tv.setTextColor(getResources().getColor(android.R.color.white));
            }
        }

        //set tab which one you want to open first time 0 or 1 or 2
        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch(tabId){
                    case "Today":
                        break;
                    case "Overdue":
                        break;
                    case "Tomorrow":
                        break;
                    case "All":
                        break;
                    default:
                }
            }
        });
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            fab_hide();
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Press Home Button to exit app!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
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
            // Handle the camera action
        } else if (id == R.id.nav_bible) {
            Intent intent = new Intent(HomeActivity.this,BibleActivity.class);
            startActivity(intent);
        } /*else if (id == R.id.nav_rewards) {
            Intent intent = new Intent(HomeActivity.this,RewardsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_statistics) {
            Intent intent = new Intent(HomeActivity.this,StatsActivity.class);
            startActivity(intent);
        }*/ else if (id == R.id.nav_profile) {
            Intent intent = new Intent(HomeActivity.this,ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(HomeActivity.this, HelpActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
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

    public void startTutorialHome() {
        fab.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                fab.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                final Tutorial tutorial1 = new Tutorial(HomeActivity.this, fab, 200f, "Manage Sections", "Click on this floating button to add and delete sections to memorize");
                final Tutorial tutorial2 = new Tutorial(HomeActivity.this, new Point(100, 300), 800f, "Today List", "Displays the list of chunks that are scheduled for evaluation today");

                final SimpleTarget target1 = tutorial1.generateTarget();
                final SimpleTarget target2 = tutorial2.generateTarget();

                Spotlight.with(HomeActivity.this)
                        .setDuration(600L) // duration of Spotlight emerging and disappearing in ms
                        .setAnimation(new DecelerateInterpolator(2f)) // animation of Spotlight
                        .setTargets(target1, target2) // set targets. see below for more info
                        .setOnSpotlightStartedListener(new OnSpotlightStartedListener() { // callback when Spotlight starts
                            @Override
                            public void onStarted() {
                                //Toast.makeText(HomeActivity.this, "spotlight is started", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setOnSpotlightEndedListener(new OnSpotlightEndedListener() { // callback when Spotlight ends
                            @Override
                            public void onEnded() {
                                //Toast.makeText(HomeActivity.this, "spotlight is ended", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .start(); // start Spotlight
            }
        });
        getSharedPreferences(SYSTEM_PREF, 0).edit().putBoolean("show_tutorial_home", false).apply();
    }

    private void startTutorialNav() {
        drawerLayout.openDrawer(Gravity.LEFT);
        drawerLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                drawerLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                final Tutorial tutorial1 = new Tutorial(HomeActivity.this, new Point(100, 770), 1f, "Home", "The Home screen is the default screen through which you can add, delete or review sections that you want to memorize");
                final Tutorial tutorial2 = new Tutorial(HomeActivity.this, new Point(100, 970), 1f, "Bible", "This is the integrated Bible which also contains information on the verses you've memorized");

                final SimpleTarget target1 = tutorial1.generateTarget();
                final SimpleTarget target2 = tutorial2.generateTarget();

                Spotlight.with(HomeActivity.this)
                        .setDuration(600L) // duration of Spotlight emerging and disappearing in ms
                        .setAnimation(new DecelerateInterpolator(2f)) // animation of Spotlight
                        .setTargets(target1, target2) // set targets. see below for more info
                        .setOnSpotlightStartedListener(new OnSpotlightStartedListener() { // callback when Spotlight starts
                            @Override
                            public void onStarted() {
                                //Toast.makeText(HomeActivity.this, "spotlight is started", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setOnSpotlightEndedListener(new OnSpotlightEndedListener() { // callback when Spotlight ends
                            @Override
                            public void onEnded() {
                                //Toast.makeText(HomeActivity.this, "spotlight is ended", Toast.LENGTH_SHORT).show();
                                drawerLayout.closeDrawer(Gravity.LEFT);
                                if (getSharedPreferences(SYSTEM_PREF, 0).getBoolean("show_tutorial_home", true)) {
                                    startTutorialHome();
                                }
                            }
                        })
                        .start(); // start Spotlight
            }
        });
        getSharedPreferences(SYSTEM_PREF, 0).edit().putBoolean("show_tutorial_nav", false).apply();
    }

}
