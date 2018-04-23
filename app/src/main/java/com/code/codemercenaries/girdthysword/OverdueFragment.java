package com.code.codemercenaries.girdthysword;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class OverdueFragment extends Fragment {

    Activity mActivity;
    ArrayList<Chunk> chunks;
    ListView overdue;

    private OnFragmentInteractionListener mListener;

    public OverdueFragment() {
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

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar ca = Calendar.getInstance();
        String currDate = df.format(ca.getTime());

        DBHandler dbHandler = new DBHandler(mActivity);
        chunks = (ArrayList<Chunk>) dbHandler.getAllChunksThatAreOverdue(currDate);

        for (Chunk c : chunks) {
            Log.d("OverdueList:", c.toString());
        }

        View rootView = inflater.inflate(R.layout.fragment_overdue, container, false);
        overdue = rootView.findViewById(R.id.overdue_list);
        CustomListAdapter1 overdueAdapter = new CustomListAdapter1(mActivity, R.layout.chunk_custom_list1, chunks);
        TextView defaultText = rootView.findViewById(R.id.defaultText);
        LottieAnimationView animationView = rootView.findViewById(R.id.animation_view);
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

        return rootView;
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
}
