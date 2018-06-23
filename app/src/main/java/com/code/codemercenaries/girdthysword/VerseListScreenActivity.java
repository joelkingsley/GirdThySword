package com.code.codemercenaries.girdthysword;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.code.codemercenaries.girdthysword.Database.DBHandler;
import com.code.codemercenaries.girdthysword.Font.FontHelper;
import com.code.codemercenaries.girdthysword.Fragments.ChapterFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class VerseListScreenActivity extends AppCompatActivity {

    String version;
    String bookName;
    int chapNum;
    int numOfChap;
    TextView chapTitle;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new FontHelper(this).initialize();

        setContentView(R.layout.activity_verse_list_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        viewPager = findViewById(R.id.viewPager);
        chapTitle = findViewById(R.id.chapterTitle);

        DBHandler dbHandler = new DBHandler(this);

        if (getIntent().getExtras() != null) {
            bookName = getIntent().getExtras().getString("EXTRA_BOOK_NAME", "Genesis");
            chapNum = getIntent().getExtras().getInt("EXTRA_CHAP_NUM", 1);
            version = getIntent().getExtras().getString("EXTRA_VERSION", "en_kjv");
        } else {
            bookName = "Genesis";
            chapNum = 1;
            version = "en_kjv";
        }
        numOfChap = dbHandler.getNumofChap(version, bookName);

        chapTitle.setText(getResources().getString(R.string.bookname_with_num, bookName, chapNum));

        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerStateAdapter adapter = new ViewPagerStateAdapter(getSupportFragmentManager());

        for (int i = 0; i < numOfChap; i++) {
            ChapterFragment chapterFragment = ChapterFragment.newInstance(version, bookName, i + 1);
            adapter.addFragment(chapterFragment, getResources().getString(R.string.bookname_with_num, bookName, i + 1));
        }

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(chapNum - 1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String text = getResources().getString(R.string.bookname_with_num, bookName, position + 1);
                chapTitle.setText(text);
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
