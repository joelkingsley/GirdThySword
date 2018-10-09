package com.code.codemercenaries.girdthysword.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.code.codemercenaries.girdthysword.Database.DBHandler;
import com.code.codemercenaries.girdthysword.Objects.Version;
import com.code.codemercenaries.girdthysword.R;
import com.code.codemercenaries.girdthysword.StatsDialog;

import java.util.List;

public class StatsFragment extends Fragment {

    ListView versions;
    List<Version> versionsList;

    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);

        versions = rootView.findViewById(R.id.versions);

        DBHandler dbHandler = new DBHandler(getActivity());
        versionsList = dbHandler.getVersions();
        ArrayAdapter<Version> adapter = new ArrayAdapter<Version>(getActivity(), android.R.layout.simple_list_item_1, versionsList);
        this.versions.setAdapter(adapter);
        this.versions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StatsDialog dialog = new StatsDialog(getActivity(), versionsList.get(position).get_id());
                dialog.show();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
