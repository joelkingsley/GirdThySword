package com.code.codemercenaries.girdthysword.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.code.codemercenaries.girdthysword.Database.DBHandler;
import com.code.codemercenaries.girdthysword.ListAdapters.CustomListAdapter1;
import com.code.codemercenaries.girdthysword.Objects.Chunk;
import com.code.codemercenaries.girdthysword.R;

import java.util.ArrayList;


public class AllFragment extends Fragment {

    Activity mActivity;
    ArrayList<Chunk> chunks;
    ListView all;

    CustomListAdapter1 allAdapter;
    LottieAnimationView loading;
    LottieAnimationView animationView;
    TextView defaultText;

    public AllFragment() {
        // Required empty public constructor
        Log.d("Fragment:", "All Created");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("Fragment:", "All OnCreateView");
        View rootView = inflater.inflate(R.layout.fragment_all, container, false);

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActivity = getActivity();

        all = view.findViewById(R.id.all_list);
        defaultText = view.findViewById(R.id.defaultText);
        animationView = view.findViewById(R.id.animation_view);

        DBHandler dbHandler = new DBHandler(mActivity);

        chunks = new ArrayList<>();

        chunks = (ArrayList<Chunk>) dbHandler.getAllChunks();

        allAdapter = new CustomListAdapter1(mActivity, R.layout.chunk_custom_list2, chunks);

        Log.d("All Chunks:", chunks.toString());

        if (chunks.size() == 0) {
            defaultText.setVisibility(View.VISIBLE);
            animationView.playAnimation();
        } else {
            defaultText.setVisibility(View.GONE);
            animationView.pauseAnimation();
            animationView.setVisibility(View.GONE);
        }

        all.setAdapter(allAdapter);
    }

    private class UpdateList extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            DBHandler dbHandler = new DBHandler(mActivity);
            chunks = (ArrayList<Chunk>) dbHandler.getAllChunks();

            for (int i = 0; i < chunks.size(); i++) {
                Log.d("All Chunks:", chunks.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            allAdapter.notifyDataSetChanged();
            loading.pauseAnimation();
            loading.setVisibility(View.INVISIBLE);
        }
    }


}
