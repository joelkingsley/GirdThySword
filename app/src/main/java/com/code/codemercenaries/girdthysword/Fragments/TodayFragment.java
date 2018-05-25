package com.code.codemercenaries.girdthysword.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.code.codemercenaries.girdthysword.Database.DBHandler;
import com.code.codemercenaries.girdthysword.ListAdapters.CustomListAdapter1;
import com.code.codemercenaries.girdthysword.Objects.Chunk;
import com.code.codemercenaries.girdthysword.R;
import com.code.codemercenaries.girdthysword.ReviewActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TodayFragment extends Fragment {

    Activity mActivity;
    ArrayList<Chunk> chunks;
    ListView today;
    LottieAnimationView loading;

    CustomListAdapter1 todayAdapter;


    public TodayFragment() {
        // Required empty public constructor
        Log.d("Fragment:", "Today Created");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Log.d("Fragment:", "Today OnCreateView");

        View rootView = inflater.inflate(R.layout.fragment_today, container, false);

        /*loading = rootView.findViewById(R.id.loading);
        loading.playAnimation();
        loading.setVisibility(View.VISIBLE);*/

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActivity = getActivity();
        chunks = new ArrayList<>();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar ca = Calendar.getInstance();
        String currDate = df.format(ca.getTime());

        DBHandler dbHandler = new DBHandler(mActivity);
        chunks = (ArrayList<Chunk>) dbHandler.getAllChunksForToday(currDate);

        for (int i = 0; i < chunks.size(); i++) {
            Log.d("Today Chunks:", chunks.toString());
        }

        today = view.findViewById(R.id.today_list);
        todayAdapter = new CustomListAdapter1(mActivity, R.layout.chunk_custom_list1, chunks);


        TextView defaultText = view.findViewById(R.id.defaultText);
        LottieAnimationView animationView = view.findViewById(R.id.animation_view);

        if (chunks.size() == 0) {
            defaultText.setVisibility(View.VISIBLE);
            animationView.playAnimation();
        } else {
            defaultText.setVisibility(View.GONE);
            animationView.pauseAnimation();
            animationView.setVisibility(View.GONE);
        }
        today.setAdapter(todayAdapter);

        today.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mActivity, ReviewActivity.class);
                String chunkId = chunks.get(i).getId();
                intent.putExtra("EXTRA_CHUNK_ID", chunkId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Fragment:", "Today onPause");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("Fragment:", "Today onAttach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Fragment:", "Today onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Fragment:", "Today onDetach");
    }

    private class UpdateList extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Calendar ca = Calendar.getInstance();
            String currDate = df.format(ca.getTime());

            DBHandler dbHandler = new DBHandler(mActivity);
            chunks = (ArrayList<Chunk>) dbHandler.getAllChunksForToday(currDate);

            for (int i = 0; i < chunks.size(); i++) {
                Log.d("Today Chunks:", chunks.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            todayAdapter.notifyDataSetChanged();
            loading.pauseAnimation();
            loading.setVisibility(View.INVISIBLE);
        }
    }
}
