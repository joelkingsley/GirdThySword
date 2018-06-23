package com.code.codemercenaries.girdthysword.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.code.codemercenaries.girdthysword.Database.DBHandler;
import com.code.codemercenaries.girdthysword.ListAdapters.BCustomListAdapter2;
import com.code.codemercenaries.girdthysword.Objects.ChapterDetail;
import com.code.codemercenaries.girdthysword.R;
import com.code.codemercenaries.girdthysword.VerseListScreenActivity;

import java.util.ArrayList;
import java.util.List;


public class BookFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "EXTRA_VERSION";
    private static final String ARG_PARAM2 = "EXTA_BOOK_NAME";

    Activity mActivity;
    List<ChapterDetail> chapterDetails;
    int numOfChap;
    BCustomListAdapter2 adapter;
    ListView chapterList;
    UpdateStats task;
    TextView scrollIndicator;
    // TODO: Rename and change types of parameters
    private String version;
    private String bookName;


    public BookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookFragment newInstance(String param1, String param2) {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            version = getArguments().getString(ARG_PARAM1);
            bookName = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mActivity = getActivity();

        return inflater.inflate(R.layout.fragment_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scrollIndicator = view.findViewById(R.id.scroll_indicator);
        chapterList = view.findViewById(R.id.chapterList);
    }

    @Override
    public void onResume() {
        super.onResume();

        setupList();
    }

    private void setupList() {

        DBHandler dbHandler = new DBHandler(mActivity);

        chapterDetails = new ArrayList<>();

        numOfChap = dbHandler.getNumofChap(version, bookName);

        for (int i = 0; i < numOfChap; i++) {
            chapterDetails.add(new ChapterDetail());
            chapterDetails.get(i).setBookName(bookName);
            chapterDetails.get(i).setChapNum(i + 1);
        }

        task = new UpdateStats();
        task.execute();

        adapter = new BCustomListAdapter2(mActivity, R.layout.bible_custom_list2, chapterDetails);
        chapterList.setAdapter(adapter);

        chapterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mActivity, VerseListScreenActivity.class);
                intent.putExtra("EXTRA_VERSION", version);
                intent.putExtra("EXTRA_BOOK_NAME", bookName);
                intent.putExtra("EXTRA_CHAP_NUM", i + 1);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        task.cancel(true);
    }

    private class UpdateStats extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            DBHandler dbHandler = new DBHandler(mActivity);

            for (int i = 0; i < numOfChap; i++) {
                if (isCancelled()) {
                    break;
                }
                chapterDetails.get(i).settotalVerses(dbHandler.getNumOfVerse(version, bookName, i + 1));
                chapterDetails.get(i).setversesMemorized(dbHandler.getMemorizedVerses(version, bookName, i + 1).size());
                chapterDetails.get(i).setPercentage((chapterDetails.get(i).getversesMemorized() / chapterDetails.get(i).gettotalVerses()) * 100);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
        }
    }
}
