package com.code.codemercenaries.girdthysword;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class ReviewActivity extends AppCompatActivity {

    private static final int REQ_CODE_CONTINUE = 200;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    //General
    Chunk chunk;
    Long id;
    int count=0;
    List<ReadableVerse> readableVerseList = new ArrayList<ReadableVerse>();
    List<StringTokenizer> tokenList = new ArrayList<StringTokenizer>();
    List<String> speakableText = new ArrayList<String>();
    List<String> hintText = new ArrayList<String>();
    int currentVerseIndex;
    float totalMatchScore;
    //Review Layout
    TextView vt;
    TextView st;
    TextView completed;
    TextView heading;
    FloatingActionButton record;
    Button continue_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);


        vt = (TextView) findViewById(R.id.verseText);
        st = (TextView) findViewById(R.id.spokenText);
        completed = (TextView) findViewById(R.id.completed);
        heading = (TextView) findViewById(R.id.heading);

        record = (FloatingActionButton) findViewById(R.id.record);
        continue_btn = (Button) findViewById(R.id.continue_btn);


        DBHandler dbHandler = new DBHandler(this);
        id = getIntent().getLongExtra("EXTRA_CHUNK_ID",-1);
        chunk = dbHandler.getChunk(id);
        for(int i=chunk.getStartVerseNum();i<=chunk.getEndVerseNum();i++){
            ReadableVerse verse = dbHandler.getReadableVerse(chunk.getBookName(),chunk.getChapNum(),i);
            readableVerseList.add(verse);
            tokenList.add( new StringTokenizer(verse.get_verse_text(),":;.?") );
        }
        for(int i=0;i<tokenList.size();i++){
            StringBuilder text = new StringBuilder();
            StringBuilder hint = new StringBuilder();
            while(tokenList.get(i).hasMoreTokens()){
                String token = tokenList.get(i).nextToken();
                text.append(token);
                text.append(" ");

            }
            speakableText.add(text.toString().trim());
            hintText.add(text.toString().trim());
        }
        currentVerseIndex = 0;
        totalMatchScore = 0;
        review();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void review() {
        if(currentVerseIndex<readableVerseList.size()){
            heading.setText(readableVerseList.get(currentVerseIndex).toString());
            vt.setText(speakableText.get(currentVerseIndex));
            st.setText("");
            completed.setText(currentVerseIndex + "/" + readableVerseList.size() + " Completed");
            continue_btn.setVisibility(View.INVISIBLE);
            record.setVisibility(View.VISIBLE);
            record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    vt.setVisibility(View.INVISIBLE);
                    promptSpeechInput();
                }
            });
        }
        else{
            Intent intent = new Intent(ReviewActivity.this,ResultActivity.class);
            intent.putExtra("EXTRA_CHUNK_ID",this.id);
            intent.putExtra("EXTRA_TOTAL_MATCH_SCORE",totalMatchScore);
            intent.putExtra("EXTRA_NO_OF_REVIEWS", readableVerseList.size());
            startActivity(intent);
        }

    }

    private void calculate() {
        int length = vt.getText().length();
        int levDistance = unlimitedCompare(vt.getText().toString().toLowerCase().replaceAll(",",""),st.getText());

        float matchPercentage = 100 - ((float)levDistance/(float)length) *100;
        if(matchPercentage>=85){
            totalMatchScore+= matchPercentage;
            Toast.makeText(ReviewActivity.this, "Awesome!! : " + matchPercentage + "% Accuracy",
                    Toast.LENGTH_LONG).show();
        }
        else if(matchPercentage>=60 && matchPercentage<85){
            totalMatchScore+= matchPercentage;
            Toast.makeText(ReviewActivity.this, "Good Job! : " + matchPercentage + "% Accuracy",
                    Toast.LENGTH_LONG).show();
        }
        else{
            totalMatchScore+= matchPercentage;
            Toast.makeText(ReviewActivity.this, "Try Again : " + matchPercentage + "% Accuracy",
                    Toast.LENGTH_LONG).show();
        }
        continue_btn.setVisibility(View.VISIBLE);
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentVerseIndex++;
                count++;
                review();
            }
        });
    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case REQ_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    st.setText(result.get(0).toLowerCase());
                    calculate();
                    vt.setVisibility(View.VISIBLE);
                }
                break;
            case REQ_CODE_CONTINUE:
                break;
        }
    }

    private int unlimitedCompare(CharSequence left, CharSequence right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        /*
           This implementation use two variable to record the previous cost counts,
           So this implementation use less memory than previous impl.
         */

        int n = left.length(); // length of left
        int m = right.length(); // length of right

        if (n == 0) {
            return m;
        } else if (m == 0) {
            return n;
        }

        if (n > m) {
            // swap the input strings to consume less memory
            final CharSequence tmp = left;
            left = right;
            right = tmp;
            n = m;
            m = right.length();
        }

        int[] p = new int[n + 1];

        // indexes into strings left and right
        int i; // iterates through left
        int j; // iterates through right
        int upper_left;
        int upper;

        char rightJ; // jth character of right
        int cost; // cost

        for (i = 0; i <= n; i++) {
            p[i] = i;
        }

        for (j = 1; j <= m; j++) {
            upper_left = p[0];
            rightJ = right.charAt(j - 1);
            p[0] = j;

            for (i = 1; i <= n; i++) {
                upper = p[i];
                cost = left.charAt(i - 1) == rightJ ? 0 : 1;
                // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
                p[i] = Math.min(Math.min(p[i - 1] + 1, p[i] + 1), upper_left + cost);
                upper_left = upper;
            }
        }

        return p[n];
    }

}
