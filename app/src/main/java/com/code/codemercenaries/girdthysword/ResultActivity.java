package com.code.codemercenaries.girdthysword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ResultActivity extends AppCompatActivity {

    //Result Layout
    TextView percent,space,ndor;
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.default_font))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_result);

        DBHandler dbHandler = new DBHandler(this);
        long id = getIntent().getLongExtra("EXTRA_CHUNK_ID",-1);
        Chunk chunk = dbHandler.getChunk(id);
        float totalMatchScore = getIntent().getFloatExtra("EXTRA_TOTAL_MATCH_SCORE",0);
        int numOfReviews = getIntent().getIntExtra("EXTRA_NO_OF_REVIEWS",3);

        percent = (TextView) findViewById(R.id.percentage);
        space = (TextView) findViewById(R.id.space);
        ndor = (TextView) findViewById(R.id.ndor);

        done = (Button) findViewById(R.id.done);

        int percentage = (int)(totalMatchScore/numOfReviews);
        this.percent.setText(Integer.toString(percentage));
        if(percentage>=85){
            int space = chunk.getSpace();
            chunk.setSpace(space*2);
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Calendar ca = Calendar.getInstance();
            ca.add(Calendar.DATE,chunk.getSpace());
            String currDate = df.format(ca.getTime());
            chunk.setNextDateOfReview(currDate);
            DBHandler db = new DBHandler(this);

            db.setChunkToMemorized(chunk);
            db.updateChunk(chunk,true);
            db.updateSiblingChunks(chunk);

            if(db.checkIfMasteredSection(chunk.getSecId()) && chunk.getSeq()!=0){
                db.mergeChunksInSection(chunk.getSecId());
                Section s = db.retSection(chunk.getSecId());
                Toast.makeText(ResultActivity.this, "Merged Section " + s.toString(),
                        Toast.LENGTH_LONG).show();
            }

        }
        else if(percentage>=65){
            int space = chunk.getSpace();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Calendar ca = Calendar.getInstance();
            ca.add(Calendar.DATE,space);
            String currDate = df.format(ca.getTime());
            chunk.setNextDateOfReview(currDate);
            DBHandler db = new DBHandler(this);

            db.updateChunk(chunk, false);

            db.updateSiblingChunks(chunk);
        }
        else{
            int space = chunk.getSpace();
            if(space>1){
                chunk.setSpace(space/2);
            }
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Calendar ca = Calendar.getInstance();
            ca.add(Calendar.DATE,chunk.getSpace());
            String currDate = df.format(ca.getTime());
            chunk.setNextDateOfReview(currDate);
            DBHandler db = new DBHandler(this);
            db.updateChunk(chunk, false);
        }
        this.space.setText(Integer.toString(chunk.getSpace()));
        this.ndor.setText(chunk.getNextDateOfReview());

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
