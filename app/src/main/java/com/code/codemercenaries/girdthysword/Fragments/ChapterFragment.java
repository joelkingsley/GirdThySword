package com.code.codemercenaries.girdthysword.Fragments;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.code.codemercenaries.girdthysword.Database.DBHandler;
import com.code.codemercenaries.girdthysword.ListAdapters.BCustomListAdapter3;
import com.code.codemercenaries.girdthysword.R;
import com.code.codemercenaries.girdthysword.ReadableVerse;

import java.util.ArrayList;
import java.util.List;


public class ChapterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "EXTRA_VERSION";
    private static final String ARG_PARAM2 = "EXTRA_BOOK_NAME";
    private static final String ARG_PARAM3 = "EXTRA_CHAP_NUM";
    Activity mActivity;
    int numOfVerse;
    BCustomListAdapter3 bCustomListAdapter3;
    ListView verseList;
    LottieAnimationView lottieAnimationView;
    List<ReadableVerse> verses;
    TextView scrollIndicator;
    DisplayVerses task1;
    UpdateStats task2;
    // TODO: Rename and change types of parameters
    private String version;
    private String bookName;
    private int chapNum;

    public ChapterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @param param3 Parameter 3.
     * @return A new instance of fragment ChapterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChapterFragment newInstance(String param1, String param2, int param3) {
        ChapterFragment fragment = new ChapterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putInt(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            version = getArguments().getString(ARG_PARAM1);
            bookName = getArguments().getString(ARG_PARAM2);
            chapNum = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mActivity = getActivity();
        return inflater.inflate(R.layout.fragment_chapter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scrollIndicator = view.findViewById(R.id.scroll_indicator);
        lottieAnimationView = view.findViewById(R.id.animation_view);
        verseList = view.findViewById(R.id.verseList);
        task1 = new DisplayVerses();
        task2 = new UpdateStats();
        setupList();
    }

    private void setupList() {

        Log.d("ChapterFragment:", "setupList");

        DBHandler dbHandler = new DBHandler(mActivity);
        numOfVerse = dbHandler.getNumOfVerse(version, bookName, chapNum);

        if (chapNum == 1) {
            scrollIndicator.setText(getResources().getString(R.string.start_scroll_indicator));
        } else if (chapNum == numOfVerse) {
            scrollIndicator.setText(getResources().getString(R.string.end_scroll_indicator));
        }

        verses = new ArrayList<>();

        bCustomListAdapter3 = new BCustomListAdapter3(mActivity, R.layout.bible_custom_list3, verses);
        verseList.setAdapter(bCustomListAdapter3);

        lottieAnimationView.playAnimation();
        lottieAnimationView.setVisibility(View.VISIBLE);

        task1.execute();

        verseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String label = verses.get(position).get_book_name() + " " + verses.get(position).get_chap_num() + ":" + verses.get(position).get_verse_num();
                ClipboardManager clipboard = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(label, verses.get(position).get_verse_text());

                clipboard.setPrimaryClip(clip);
                Toast.makeText(mActivity, "Copied to clipboard",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        task1.cancel(true);
        task2.cancel(true);
    }

    private class DisplayVerses extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.d("ChapterFragment:", "DisplayVerses");
            DBHandler dbHandler = new DBHandler(mActivity);

            for (int i = 0; i < numOfVerse; i++) {
                String verse = dbHandler.getVerse(version, bookName, chapNum, i + 1);
                verses.add(new ReadableVerse(bookName, chapNum, i + 1, verse, 0));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            lottieAnimationView.pauseAnimation();
            lottieAnimationView.setVisibility(View.INVISIBLE);
            bCustomListAdapter3.notifyDataSetChanged();
            task2.execute();
        }
    }

    private class UpdateStats extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.d("ChapterFragment:", "UpdateStats");
            DBHandler dbHandler = new DBHandler(mActivity);
            List<Integer> memList = dbHandler.getMemorizedVerses(version, bookName, chapNum);
            List<Integer> addList = dbHandler.getAvailableVersesOfChap(version, bookName, chapNum);

            for (int i = 0; i < numOfVerse; i++) {

                if (memList.contains(i + 1)) {
                    ReadableVerse readableVerse = verses.get(i);
                    readableVerse.set_memory(3);
                    verses.set(i, readableVerse);
                } else if (!addList.contains(i + 1)) {
                    ReadableVerse readableVerse = verses.get(i);
                    readableVerse.set_memory(2);
                    verses.set(i, readableVerse);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("ChapterFragment:", "Verses " + verses.size() + "/" + numOfVerse);
            bCustomListAdapter3.notifyDataSetChanged();
        }
    }
}
