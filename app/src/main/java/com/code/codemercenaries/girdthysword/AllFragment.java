package com.code.codemercenaries.girdthysword;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;


public class AllFragment extends Fragment {

    Activity mActivity;
    ArrayList<Chunk> chunks;
    ListView all;

    private OnFragmentInteractionListener mListener;

    public AllFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mActivity = getActivity();

        DBHandler dbHandler = new DBHandler(mActivity);
        chunks = (ArrayList<Chunk>) dbHandler.getAllChunks();

        for (Chunk c : chunks) {
            Log.d("AllList:", c.toString());
        }

        View rootView = inflater.inflate(R.layout.fragment_all, container, false);
        all = rootView.findViewById(R.id.all_list);
        CustomListAdapter1 todayAdapter = new CustomListAdapter1(mActivity, R.layout.chunk_custom_list2, chunks);
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
        all.setAdapter(todayAdapter);

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
