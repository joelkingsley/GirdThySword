package com.code.codemercenaries.girdthysword;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class VerseListActivity extends AppCompatActivity {

    final String SYSTEM_PREF = "system_pref";
    SharedPreferences systemPreferences;

    TextView cd;
    ListView verseList;

    String bookName;
    int chapNum;
    List<ReadableVerse> verses;

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
        } else if (getSharedPreferences(SETTINGS_PREF, 0).getString("font", getString(R.string.default_font_name)).equals(getString(R.string.coolvetica_font_name))) {
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath(getString(R.string.coolvetica_font))
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );
        }
        setContentView(R.layout.activity_verse_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        cd = (TextView) findViewById(R.id.chapterDesc);
        verseList = (ListView) findViewById(R.id.verseList);
        bookName = getIntent().getExtras().getString("EXTRA_BOOK_NAME");
        chapNum = getIntent().getExtras().getInt("EXTRA_CHAP_NUM");

        verses = new ArrayList<>();

        cd.setText(bookName + " " + chapNum);

        /*systemPreferences = getSharedPreferences(SYSTEM_PREF,0);
        systemPreferences.edit().putString("curr_book_name", bookName).commit();
        systemPreferences.edit().putInt("curr_chap_num", chapNum).commit();*/

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
        //List<ReadableVerse> verses = dbHandler.getChapterWithMemory(bookName,chapNum);
        final List<Integer> memList = dbHandler.getMemorizedVerses(bookName, chapNum);
        final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Loading Data from Firebase Database");

        progressDialog.show();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bible").child(bookName).child("chapters").child(String.valueOf(chapNum - 1)).child("verses");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                long n = snapshot.getChildrenCount();
                for (int i = 0; i < n; i++) {
                    String verse = snapshot.child(String.valueOf(i)).child(String.valueOf(i + 1)).getValue(String.class);
                    if (memList.contains(i + 1))
                        verses.add(new ReadableVerse(bookName, chapNum, i + 1, verse, 2));
                    else
                        verses.add(new ReadableVerse(bookName, chapNum, i + 1, verse, 0));
                }


                BCustomListAdapter3 bCustomListAdapter3 = new BCustomListAdapter3(VerseListActivity.this, R.layout.bible_custom_list3, verses);
                verseList.setAdapter(bCustomListAdapter3);
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
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }

        });
    }

}
