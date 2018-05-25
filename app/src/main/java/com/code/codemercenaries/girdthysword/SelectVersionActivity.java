package com.code.codemercenaries.girdthysword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.code.codemercenaries.girdthysword.Database.DBHandler;
import com.code.codemercenaries.girdthysword.Font.FontHelper;
import com.code.codemercenaries.girdthysword.Objects.Version;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SelectVersionActivity extends AppCompatActivity {

    ListView versions;
    List<Version> versionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_version);

        new FontHelper(this).initialize();

        DBHandler dbHandler = new DBHandler(this);
        versionList = dbHandler.getVersions();

        versions = findViewById(R.id.versions);
        ArrayAdapter<Version> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, versionList);
        versions.setAdapter(adapter1);

        versions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SelectVersionActivity.this, BibleActivity.class);
                intent.putExtra("EXTRA_VERSION", versionList.get(i).get_id());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
