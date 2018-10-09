package com.code.codemercenaries.girdthysword.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.code.codemercenaries.girdthysword.Font.FontHelper;
import com.code.codemercenaries.girdthysword.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HelpScreenActivity extends AppCompatActivity {

    ListView questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FontHelper(this).initialize();
        setContentView(R.layout.activity_help_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        questionList = (ListView) findViewById(R.id.questions);

        setupList();

    }

    private void setupList() {

        List<String> questions = new ArrayList<>(Arrays.asList("I'm confused. Where do I get an overview?"
                , "How do I add new sections to memorize?"
                , "How do I know when to review a particular chunk?"
                , "The speech recognition seems to be very inaccurate."
                , "How do I report a bug or request a feature?"));

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, questions);
        questionList.setAdapter(adapter);
        questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HelpScreenActivity.this, AnswerScreenActivity.class);
                intent.putExtra("EXTRA_QUES_ID", position + 1);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
