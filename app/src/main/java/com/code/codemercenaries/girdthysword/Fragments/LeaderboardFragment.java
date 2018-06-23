package com.code.codemercenaries.girdthysword.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.code.codemercenaries.girdthysword.ListAdapters.LCustomAdapter;
import com.code.codemercenaries.girdthysword.Objects.LeaderboardUser;
import com.code.codemercenaries.girdthysword.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardFragment extends Fragment {

    RecyclerView list;
    List<LeaderboardUser> users;
    Activity mActivity;
    RecyclerView.Adapter mAdapter;

    public LeaderboardFragment() {
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
        mActivity = getActivity();
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = view.findViewById(R.id.list);
        Query leaderboard = FirebaseDatabase.getInstance().getReference("leaderboard").orderByChild("versesMemorized").limitToFirst(100);
        leaderboard.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 1;
                users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String UID = snapshot.getKey();
                    Long versesMemorized = -snapshot.child("versesMemorized").getValue(Long.class);
                    String displayName = snapshot.child("displayName").getValue(String.class);
                    String profileURL = snapshot.child("profileURL").getValue(String.class);
                    int level = snapshot.child("level").getValue(int.class);
                    String status = snapshot.child("status").getValue(String.class);

                    users.add(new LeaderboardUser(count, profileURL, displayName, level, status, versesMemorized));
                    count++;
                }
                list.setHasFixedSize(true);
                list.setLayoutManager(new LinearLayoutManager(mActivity));

                mAdapter = new LCustomAdapter(mActivity, users);
                list.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("LeaderboardFragment:", databaseError.toString());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


}
