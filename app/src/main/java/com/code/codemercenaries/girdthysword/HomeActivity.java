package com.code.codemercenaries.girdthysword;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.code.codemercenaries.girdthysword.Color.ColorHelper;
import com.code.codemercenaries.girdthysword.Database.DBHandler;
import com.code.codemercenaries.girdthysword.Font.FontHelper;
import com.code.codemercenaries.girdthysword.Fragments.AllFragment;
import com.code.codemercenaries.girdthysword.Fragments.OverdueFragment;
import com.code.codemercenaries.girdthysword.Fragments.TodayFragment;
import com.code.codemercenaries.girdthysword.Objects.Tutorial;
import com.google.firebase.auth.FirebaseAuth;
import com.takusemba.spotlight.OnSpotlightEndedListener;
import com.takusemba.spotlight.OnSpotlightStartedListener;
import com.takusemba.spotlight.SimpleTarget;
import com.takusemba.spotlight.Spotlight;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final String SETTINGS_PREF = "settings_pref";
    final String SYSTEM_PREF = "system_pref";
    CoordinatorLayout coordinatorLayout;

    FloatingActionButton fab;
    FloatingActionButton fab_add;
    FloatingActionButton fab_delete;
    TextView tv_add;
    TextView tv_delete;
    boolean fab_status;

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

    String tabTitle[] = {"OVERDUE", "TODAY", "ALL"};
    ArrayList<Integer> remainingCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        new FontHelper(this).initialize();

        settingsPreferences = getSharedPreferences(SETTINGS_PREF, 0);
        theme = settingsPreferences.getString("theme", "original");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        back = (LinearLayout) findViewById(R.id.back);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

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

        ImageView logo = findViewById(R.id.logo);
        InputStream ims = null;
        try {
            ims = getAssets().open("dark_logo.png");
            Drawable d = Drawable.createFromStream(ims, null);
            logo.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //setupTabHost();
        /*if (getSharedPreferences(SYSTEM_PREF, 0).getBoolean("show_tutorial_nav", true)) {
            startTutorialNav();
        }*/

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
        setupColors();
        setupFabs();

    }

    private void setupColors() {
        /*theme = settingsPreferences.getString("theme", "original");
        if (theme.equals("original")) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            back.setBackgroundColor(getResources().getColor(R.color.colorSword));
            //layTab.setBackgroundColor(getResources().getColor(R.color.colorSword));
        } else if (theme.equals("white")) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            back.setBackgroundColor(getResources().getColor(android.R.color.white));
            //layTab.setBackgroundColor(getResources().getColor(android.R.color.white));
        } else if (theme.equals("red")) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            back.setBackgroundColor(getResources().getColor(R.color.red));
            //layTab.setBackgroundColor(getResources().getColor(android.R.color.white));
        } else if (theme.equals("green")) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            back.setBackgroundColor(getResources().getColor(R.color.backHighlighter));
            //layTab.setBackgroundColor(getResources().getColor(android.R.color.white));
        } else if (theme.equals("light_grey")) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            back.setBackgroundColor(getResources().getColor(R.color.light_grey));
            //layTab.setBackgroundColor(getResources().getColor(android.R.color.white));
        }*/
        new ColorHelper(this).setDefaultTheme(toolbar, back);
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
            Intent intent = new Intent(HomeActivity.this, SelectVersionActivity.class);
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
            Intent intent = new Intent(HomeActivity.this, HomeScreenActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(HomeActivity.this, MainScreenActivity.class);
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
