package com.code.codemercenaries.girdthysword;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.code.codemercenaries.girdthysword.Database.DBHandler;
import com.code.codemercenaries.girdthysword.Font.FontHelper;
import com.code.codemercenaries.girdthysword.ListAdapters.CustomListAdapter2;
import com.code.codemercenaries.girdthysword.Objects.Section;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DeleteSectionActivity extends AppCompatActivity {

    final String INDEX_PREF = "index_pref";
    SharedPreferences indexPreferences;

    ListView sectionList;
    String version;

    List<String> sectionIds;
    List<Section> sections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FontHelper(this).initialize();
        setContentView(R.layout.activity_delete_section);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        version = "en_kjv";

        sectionList = (ListView) findViewById(R.id.sectionList);

        indexPreferences = getSharedPreferences(INDEX_PREF,0);

        setupList();

        final DBHandler dbHandler = new DBHandler(this);

        sectionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(DeleteSectionActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(DeleteSectionActivity.this);
                }
                builder.setTitle("Delete Section")
                        .setMessage("Are you sure you want to delete this section?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                dbHandler.deleteSection(sectionIds.get(i));
                                dbHandler.setMemoryToNotAdded(sections.get(i));
                                //indexPreferences.edit().putBoolean(sections.get(i).get_book_name() + "_" + sections.get(i).get_chap_num(), false).apply();
                                //indexPreferences.edit().putBoolean(sections.get(i).get_book_name(), false).apply();
                                dbHandler.setAvailableBook(sections.get(i).get_version(), sections.get(i).get_book_name());
                                dbHandler.setAvailableChap(sections.get(i).get_version(), sections.get(i).get_book_name(), sections.get(i).get_chap_num());
                                setupList();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void setupList(){
        DBHandler dbHandler = new DBHandler(this);

        sectionIds = dbHandler.retSectionIds();
        sections = new ArrayList<Section>();

        for(int i=0;i<sectionIds.size();i++){
            Section section = dbHandler.retSection(sectionIds.get(i));
            sections.add(section);
        }

        CustomListAdapter2 adapter = new CustomListAdapter2(this,R.layout.section_custom_list1,sections);
        sectionList.setAdapter(adapter);
    }

}
