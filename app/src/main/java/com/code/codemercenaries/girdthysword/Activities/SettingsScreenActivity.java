package com.code.codemercenaries.girdthysword.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.code.codemercenaries.girdthysword.Font.FontHelper;
import com.code.codemercenaries.girdthysword.ListAdapters.SCustomListAdapter1;
import com.code.codemercenaries.girdthysword.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingsScreenActivity extends AppCompatActivity {

    ListView settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FontHelper(this).initialize();
        setContentView(R.layout.activity_settings_screen);
        settings = (ListView) findViewById(R.id.settings);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupList();
    }

    private void setupList() {
        SCustomListAdapter1 adapter = new SCustomListAdapter1(this, this, R.layout.settings_custom_list1);
        settings.setAdapter(adapter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
