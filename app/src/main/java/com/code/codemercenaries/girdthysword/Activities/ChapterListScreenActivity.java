package com.code.codemercenaries.girdthysword.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.code.codemercenaries.girdthysword.Database.DBHandler;
import com.code.codemercenaries.girdthysword.Fragments.BookFragment;
import com.code.codemercenaries.girdthysword.R;
import com.code.codemercenaries.girdthysword.ViewPagerAdapters.ViewPagerStateAdapter;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChapterListScreenActivity extends AppCompatActivity {

    String version;
    String bookName;
    List<String> bookNames;
    ViewPager viewPager;
    TextView bookTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        bookTitle = findViewById(R.id.bookName);

        viewPager = findViewById(R.id.viewPager);

        if (getIntent().getExtras() != null) {
            version = getIntent().getExtras().getString("EXTRA_VERSION", "en_kjv");
            bookName = getIntent().getExtras().getString("EXTRA_BOOK_NAME", "Genesis");
        } else {
            version = "en_kjv";
            bookName = "Genesis";
        }

        bookTitle.setText(bookName);

        DBHandler dbHandler = new DBHandler(this);

        bookNames = dbHandler.getBookNames(version);

        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerStateAdapter adapter = new ViewPagerStateAdapter(getSupportFragmentManager());

        for (int i = 0; i < bookNames.size(); i++) {
            BookFragment bookFragment = BookFragment.newInstance(version, bookNames.get(i));
            adapter.addFragment(bookFragment, bookName);
        }

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(bookNames.indexOf(bookName));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bookTitle.setText(bookNames.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
