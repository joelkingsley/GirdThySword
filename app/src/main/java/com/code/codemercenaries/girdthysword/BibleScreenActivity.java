package com.code.codemercenaries.girdthysword;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.code.codemercenaries.girdthysword.Font.FontHelper;
import com.code.codemercenaries.girdthysword.Fragments.NewTestamentFragment;
import com.code.codemercenaries.girdthysword.Fragments.OldTestamentFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BibleScreenActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    OldTestamentFragment oldTestament;
    NewTestamentFragment newTestament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new FontHelper(this).initialize();

        setContentView(R.layout.activity_bible_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        oldTestament = new OldTestamentFragment();
        newTestament = new NewTestamentFragment();
        adapter.addFragment(oldTestament, "OLD TESTAMENT");
        adapter.addFragment(newTestament, "NEW TESTAMENT");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
