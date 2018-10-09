package com.code.codemercenaries.girdthysword.Activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.code.codemercenaries.girdthysword.Database.DBHandler;
import com.code.codemercenaries.girdthysword.Font.FontHelper;
import com.code.codemercenaries.girdthysword.ListAdapters.BCustomListAdapter3;
import com.code.codemercenaries.girdthysword.R;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class VerseListActivity extends AppCompatActivity {

    final String SYSTEM_PREF = "system_pref";
    SharedPreferences systemPreferences;
    LottieAnimationView lottieAnimationView;

    TextView cd;
    ListView verseList;

    String bookName;
    int chapNum;
    int numOfVerse;
    List<ReadableVerse> verses;
    String version;
    BCustomListAdapter3 bCustomListAdapter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String SETTINGS_PREF = "settings_pref";
        new FontHelper(this).initialize();
        setContentView(R.layout.activity_verse_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        cd = (TextView) findViewById(R.id.chapterDesc);
        verseList = (ListView) findViewById(R.id.verseList);
        bookName = getIntent().getExtras().getString("EXTRA_BOOK_NAME", "Genesis");
        chapNum = getIntent().getExtras().getInt("EXTRA_CHAP_NUM", 1);
        version = getIntent().getExtras().getString("EXTRA_VERSION", "en_kjv");

        verses = new ArrayList<>();

        cd.setText(bookName + " " + chapNum);
        setupList();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setupList(){
        DBHandler dbHandler = new DBHandler(this);
        numOfVerse = dbHandler.getNumOfVerse(version, bookName, chapNum);


        bCustomListAdapter3 = new BCustomListAdapter3(VerseListActivity.this, R.layout.bible_custom_list3, verses);
        verseList.setAdapter(bCustomListAdapter3);


        lottieAnimationView = findViewById(R.id.animation_view);
        lottieAnimationView.playAnimation();
        lottieAnimationView.setVisibility(View.VISIBLE);

        new DisplayVerses().execute();

        verseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String label = verses.get(position).get_book_name() + " " + verses.get(position).get_chap_num() + ":" + verses.get(position).get_verse_num();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(label, verses.get(position).get_verse_text());

                clipboard.setPrimaryClip(clip);
                Toast.makeText(VerseListActivity.this, "Copied to clipboard",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private class DisplayVerses extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            DBHandler dbHandler = new DBHandler(getApplicationContext());

            for (int i = 0; i < numOfVerse; i++) {
                String verse = dbHandler.getVerse(version, bookName, chapNum, i + 1);
                verses.add(new ReadableVerse(bookName, chapNum, i + 1, verse, 0));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            lottieAnimationView.pauseAnimation();
            lottieAnimationView.setVisibility(View.INVISIBLE);
            bCustomListAdapter3.notifyDataSetChanged();
            new UpdateStats().execute();
        }
    }

    private class UpdateStats extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            DBHandler dbHandler = new DBHandler(getApplicationContext());
            List<Integer> memList = dbHandler.getMemorizedVerses(version, bookName, chapNum);
            List<Integer> addList = dbHandler.getAvailableVersesOfChap(version, bookName, chapNum);

            for (int i = 0; i < numOfVerse; i++) {
                if (memList.contains(i + 1)) {
                    ReadableVerse readableVerse = verses.get(i);
                    readableVerse.set_memory(3);
                    verses.set(i, readableVerse);
                } else if (!addList.contains(i + 1)) {
                    ReadableVerse readableVerse = verses.get(i);
                    readableVerse.set_memory(2);
                    verses.set(i, readableVerse);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            bCustomListAdapter3.notifyDataSetChanged();
        }
    }

}
