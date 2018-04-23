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

public class TodayFragment extends Fragment {

    Activity mActivity;
    ArrayList<Chunk> chunks;
    ListView today;

    private OnFragmentInteractionListener mListener;

    public TodayFragment() {
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
        //ca.add(Calendar.DATE,1);
        //String tomDate = df.format(ca.getTime());

        DBHandler dbHandler = new DBHandler(mActivity);
        chunks = (ArrayList<Chunk>) dbHandler.getAllChunksForToday(currDate);

        for (Chunk c : chunks) {
            Log.d("TodayList:", c.toString());
        }

        View rootView = inflater.inflate(R.layout.fragment_today, container, false);
        today = rootView.findViewById(R.id.today_list);
        CustomListAdapter1 todayAdapter = new CustomListAdapter1(mActivity, R.layout.chunk_custom_list1, chunks);
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

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
