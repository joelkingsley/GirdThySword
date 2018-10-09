package com.code.codemercenaries.girdthysword.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.code.codemercenaries.girdthysword.Activities.ChapterListScreenActivity;
import com.code.codemercenaries.girdthysword.Database.DBHandler;
import com.code.codemercenaries.girdthysword.ListAdapters.BCustomListAdapter1;
import com.code.codemercenaries.girdthysword.R;

import java.util.ArrayList;
import java.util.List;


public class NewTestamentFragment extends Fragment {


    Activity mActivity;
    ListView list;
    List<String> otBooks;
    List<String> ntBooks;
    String version;

    public NewTestamentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mActivity = getActivity();
        return inflater.inflate(R.layout.fragment_new_testament, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = view.findViewById(R.id.list);

        if (mActivity.getIntent().getExtras() != null) {
            version = mActivity.getIntent().getExtras().getString("EXTRA_VERSION", "en_kjv");
        } else {
            version = "en_kjv";
        }

        DBHandler dbHandler = new DBHandler(mActivity);

        List<String> bookNames = dbHandler.getBookNames(version);

        ntBooks = new ArrayList<>(bookNames.subList(39, 66));

        setupList();
    }

    private void setupList() {
        BCustomListAdapter1 otAdapter = new BCustomListAdapter1(mActivity, R.layout.bible_custom_list1, ntBooks);
        list.setAdapter(otAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mActivity, ChapterListScreenActivity.class);
                intent.putExtra("EXTRA_VERSION", version);
                intent.putExtra("EXTRA_BOOK_NAME", ntBooks.get(i));
                startActivity(intent);
            }
        });
    }
}

