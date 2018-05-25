package com.code.codemercenaries.girdthysword;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.code.codemercenaries.girdthysword.Database.DBHandler;
import com.code.codemercenaries.girdthysword.Font.FontHelper;
import com.code.codemercenaries.girdthysword.Objects.Chunk;
import com.code.codemercenaries.girdthysword.Objects.Section;
import com.code.codemercenaries.girdthysword.Objects.Version;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NewSectionActivity extends AppCompatActivity {

    final String INDEX_PREF = "index_pref";
    final String SETTINGS_PREF = "settings_pref";
    SharedPreferences indexPreferences;
    SharedPreferences settingsPreferences;
    String version;
    int chunkSize = 3;
    int numOfVerse;
    Button submit;

    boolean addedChapter;
    boolean addedBook;
    boolean addedBible;
    DatabaseReference databaseReference;

    List<String> bookItems = new ArrayList<>();
    List<Integer> numOfChap = new ArrayList<>(Arrays.asList(50, 40, 27, 36, 34, 24, 21, 4, 31, 24, 22, 25, 29, 36, 10, 13, 10, 42, 150, 31, 12, 8, 66, 52, 5, 48,
            12, 14, 3, 9, 1, 4, 7, 3, 3, 3, 2, 14, 4, 28, 16, 24, 21, 28, 16, 16, 13, 6, 6, 4, 4, 5, 3, 6, 4, 3, 1, 13, 5, 5, 3, 5, 1, 1, 1, 22));
    List<String> availVersions;
    List<String> availBookNames;
    List<Integer> availChapNums;
    List<Integer> availStartVerses;
    List<Integer> availEndVerses;
    private Spinner spinner1, spinner2, spinner3, spinner4, spinner5;
    private NumberPicker numberPicker1,numberPicker2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FontHelper(this).initialize();
        setContentView(R.layout.activity_new_section);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        DBHandler dbHandler = new DBHandler(this);
        version = "en_kjv";
        bookItems = dbHandler.getBookNames(version);

        settingsPreferences = getSharedPreferences(SETTINGS_PREF, 0);
        indexPreferences = getSharedPreferences(INDEX_PREF,0);
        submit = (Button) findViewById(R.id.button);
        addItemsOnVersionSpinner();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addItemsOnVersionSpinner() {
        DBHandler dbHandler = new DBHandler(this);
        availVersions = new ArrayList<>();

        final List<Version> versions = dbHandler.getVersions();

        for (int i = 0; i < versions.size(); i++) {
            availVersions.add(versions.get(i).get_name() + " (" + versions.get(i).get_lang() + ")");
        }
        spinner5 = findViewById(R.id.spinner5);

        ArrayAdapter<String> StringAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, availVersions);
        StringAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner5.setAdapter(StringAdapter);
        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addItemsOnBookNameSpinner(versions.get(position).get_id());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        StringAdapter.notifyDataSetChanged();
    }

    public void addItemsOnBookNameSpinner(final String version) {
        spinner1 = (Spinner) findViewById(R.id.spinner1);

        DBHandler dbHandler = new DBHandler(this);
        availBookNames = dbHandler.getAvailableBooks(version);

        ArrayAdapter<String> StringAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,availBookNames);
        StringAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(StringAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addItemsOnChapterNumSpinner(version, availBookNames.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        StringAdapter.notifyDataSetChanged();
    }

    public void addItemsOnChapterNumSpinner(final String version, final String bookName) {
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        availChapNums = new ArrayList<Integer>();

        DBHandler dbHandler = new DBHandler(this);
        availChapNums = dbHandler.getAvailableChaptersofBook(version, bookName);

        ArrayAdapter<Integer> IntegerAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,availChapNums);
        IntegerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(IntegerAdapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addItemsOnStartVerseSpinner(version, bookName, availChapNums.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addItemsOnStartVerseSpinner(final String version, final String bookName, final int chapNum) {
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        availStartVerses = new ArrayList<>();
        DBHandler dbHandler = new DBHandler(this);
        numOfVerse = dbHandler.getNumOfVerse(version, bookName, chapNum);
        /*for (int i = 1; i <= numOfVerse; i++) {
            availStartVerses.add(i);
        }*/
        availStartVerses = dbHandler.getAvailableVersesOfChap(version, bookName, chapNum);

        ArrayAdapter<Integer> IntegerAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,availStartVerses);
        IntegerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(IntegerAdapter);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                addItemsOnEndVerseSpinner(version, bookName, chapNum, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView){
            }
        });
        if(availStartVerses.size() == 0){
            addItemsOnEndVerseSpinner(version, bookName, chapNum, 0);
        }
        IntegerAdapter.notifyDataSetChanged();
    }

    public void addItemsOnEndVerseSpinner(String version, String bookName, int chapNum, int startVersePos) {
        spinner4 = (Spinner) findViewById(R.id.spinner4);
        availEndVerses = new ArrayList<>();
        DBHandler dbHandler = new DBHandler(this);
        this.version = version;
        Log.d("addItemsOnEVSpinner","setup");
        for(int i=startVersePos;i<availStartVerses.size();i++){
            if(i==availStartVerses.size()-1){
                availEndVerses.add(availStartVerses.get(i));
            }
            else if(availStartVerses.get(i)+1 == availStartVerses.get(i+1)){
                availEndVerses.add(availStartVerses.get(i));
            }
            else{
                availEndVerses.add(availStartVerses.get(i));
                break;
            }
        }
        ArrayAdapter<Integer> IntegerAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,availEndVerses);
        IntegerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(IntegerAdapter);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
        IntegerAdapter.notifyDataSetChanged();
    }

    public void submit(){
        int secId;
        final String bookName = spinner1.getSelectedItem().toString();
        final int chapNum = Integer.parseInt(spinner2.getSelectedItem().toString());
        int startVerse = Integer.parseInt(spinner3.getSelectedItem().toString());
        int endVerse = Integer.parseInt(spinner4.getSelectedItem().toString());

        addedChapter = false;
        addedBook = false;
        addedBible = false;

        DBHandler dbHandler = new DBHandler(this);

        DatabaseReference sections = FirebaseDatabase.getInstance().getReference("sections").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        String newSectionId = sections.push().getKey();
        Section section = new Section(newSectionId, bookName, chapNum, startVerse, endVerse, version);

        sections.child(newSectionId).setValue(section);
        Log.d("Section:", "Added");

        dbHandler.addSection(section);


        SharedPreferences systemPreferences = getSharedPreferences(SETTINGS_PREF, 0);
        chunkSize = systemPreferences.getInt("chunk_size",3);

        /*final ProgressDialog progressDialog1 = ProgressDialog.show(NewSectionActivity.this, "",
                "Getting Preferences data. Please wait...", true);

        if(isNetworkAvailable()){
            progressDialog1.show();
        }*/

        List<Chunk> chunkList = chunkize(section,chunkSize);

        DatabaseReference chunks = FirebaseDatabase.getInstance().getReference("chunks").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        for(int i=0;i<chunkList.size();i++){
            Log.d("Add Sub Chunk:","SID=" + chunkList.get(i).getSecId() + " " + chunkList.get(i).toString() + " " + chunkList.get(i).getNextDateOfReview());

            String newChunkId = chunks.push().getKey();
            //chunkList.get(i).setId(newChunkId);
            Chunk newChunk = chunkList.get(i);
            newChunk.setId(newChunkId);
            chunkList.set(i, newChunk);

            dbHandler.addChunk(chunkList.get(i));

            chunks.child(newChunkId).setValue(chunkList.get(i));
        }
        Log.d("Chunks:", "Added");

        SharedPreferences indexPreferences = getSharedPreferences(INDEX_PREF,0);

        dbHandler.setMemoryToAdded(section);

        /*final DatabaseReference userBible = FirebaseDatabase.getInstance().getReference("user-bible").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(bookName).child(String.valueOf(chapNum));
        for(int i=section.get_start_verse_num();i<=section.get_end_verse_num();i++){
            userBible.child(String.valueOf(i)).child("memory").setValue(1);
        }*/
        /*Log.d("User Bible Data:","Added");*/
        if (dbHandler.addedChapter(version, bookName, chapNum)) {
            dbHandler.setNotAvailableChap(version, bookName, chapNum);
            if (dbHandler.getAvailableChaptersofBook(version, bookName).size() == 0) {
                dbHandler.setNotAvailableBook(version, bookName);
                Log.d("Submit","Book added");
            }

            Log.d("Submit","Chapter added");
        }

        Toast.makeText(NewSectionActivity.this, "Added " + section.toString() + ":" + newSectionId,
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(NewSectionActivity.this,HomeActivity.class);
        startActivity(intent);
    }

    public List<Chunk> chunkize(Section section,int chunkSize){
        List<Chunk> chunks = new ArrayList<Chunk>();
        int min = section.get_start_verse_num();
        int max = section.get_end_verse_num();
        int seq = 1;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar ca = Calendar.getInstance();
        String currDate = df.format(ca.getTime());

        while(max-min>=chunkSize) {
            Chunk subChunk = new Chunk();
            subChunk.setBookName(section
                    .get_book_name());
            subChunk.setChapNum(section.get_chap_num());
            subChunk.setSeq(seq);
            subChunk.setSpace(1);
            subChunk.setStartVerseNum(min);
            subChunk.setEndVerseNum(min+chunkSize-1);
            subChunk.setSecId(section.get_sec_id());
            if(seq==1){
                subChunk.setNextDateOfReview(currDate);
            }
            else{
                subChunk.setNextDateOfReview("NA");
            }
            subChunk.setMastered(false);
            subChunk.set_version(section.get_version());
            chunks.add(subChunk);
            seq++;
            min = min+chunkSize;
        }
        if(max-min>=0){
            Chunk subChunk = new Chunk();
            subChunk.setBookName(section.get_book_name());
            subChunk.setChapNum(section.get_chap_num());
            subChunk.setSeq(seq);
            subChunk.setSpace(1);
            subChunk.setStartVerseNum(min);
            subChunk.setEndVerseNum(max);
            subChunk.setSecId(section.get_sec_id());
            if(seq==1){
                subChunk.setNextDateOfReview(currDate);
            }
            else{
                subChunk.setNextDateOfReview("NA");
            }
            subChunk.setMastered(false);
            subChunk.set_version(section.get_version());
            chunks.add(subChunk);
            seq++;
            min = min+chunkSize;
        }
        return chunks;
    }
}
