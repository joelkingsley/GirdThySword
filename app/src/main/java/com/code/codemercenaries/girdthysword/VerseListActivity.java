package com.code.codemercenaries.girdthysword;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }

        });
    }

}
