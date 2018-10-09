package com.code.codemercenaries.girdthysword.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.code.codemercenaries.girdthysword.Activities.ReviewActivity;
import com.code.codemercenaries.girdthysword.Database.DBHandler;
import com.code.codemercenaries.girdthysword.ListAdapters.CustomListAdapter1;
import com.code.codemercenaries.girdthysword.Objects.Chunk;
import com.code.codemercenaries.girdthysword.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class OverdueFragment extends Fragment {

    Activity mActivity;
    ArrayList<Chunk> chunks;
    ListView overdue;

    CustomListAdapter1 overdueAdapter;
    LottieAnimationView loading;

    TextView defaultText;
    LottieAnimationView animationView;

    private OnFragmentInteractionListener mListener;

    public OverdueFragment() {
        // Required empty public constructor
        Log.d("Fragment:", "Overdue Created");
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
        // Inflate the layout for this fragment

        Log.d("Fragment:", "Overdue OnCreateView");

        View rootView = inflater.inflate(R.layout.fragment_overdue, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActivity = getActivity();

        chunks = new ArrayList<>();

        /*loading = rootView.findViewById(R.id.loading);
        loading.playAnimation();
        loading.setVisibility(View.VISIBLE);

        new UpdateList().execute();*/

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar ca = Calendar.getInstance();
        String currDate = df.format(ca.getTime());

        DBHandler dbHandler = new DBHandler(mActivity);

        chunks = (ArrayList<Chunk>) dbHandler.getAllChunksThatAreOverdue(currDate);
        for (int i = 0; i < chunks.size(); i++) {
            Log.d("Overdue Chunks:", chunks.toString());
        }

        overdue = view.findViewById(R.id.overdue_list);
        overdueAdapter = new CustomListAdapter1(mActivity, R.layout.chunk_custom_list1, chunks);
        defaultText = view.findViewById(R.id.defaultText);
        animationView = view.findViewById(R.id.animation_view);

        if (chunks.size() == 0) {
            defaultText.setVisibility(View.VISIBLE);
            animationView.playAnimation();
        } else {
            defaultText.setVisibility(View.GONE);
            animationView.pauseAnimation();
            animationView.setVisibility(View.GONE);
        }

        overdue.setAdapter(overdueAdapter);
        overdue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mActivity, ReviewActivity.class);
                String chunkId = chunks.get(i).getId();
                intent.putExtra("EXTRA_CHUNK_ID", chunkId);
                startActivity(intent);
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class UpdateList extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Calendar ca = Calendar.getInstance();
            String currDate = df.format(ca.getTime());

            DBHandler dbHandler = new DBHandler(mActivity);

            chunks = (ArrayList<Chunk>) dbHandler.getAllChunksThatAreOverdue(currDate);
            for (int i = 0; i < chunks.size(); i++) {
                Log.d("Overdue Chunks:", chunks.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            overdueAdapter.notifyDataSetChanged();
            loading.pauseAnimation();
            loading.setVisibility(View.INVISIBLE);
        }
    }
}
