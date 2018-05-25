package com.code.codemercenaries.girdthysword.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.code.codemercenaries.girdthysword.Database.DBHandler;
import com.code.codemercenaries.girdthysword.DeleteSectionActivity;
import com.code.codemercenaries.girdthysword.NewSectionActivity;
import com.code.codemercenaries.girdthysword.R;
import com.code.codemercenaries.girdthysword.ViewPagerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    FragmentActivity mActivity;

    ViewPagerAdapter adapter;
    TodayFragment todayFragment;
    OverdueFragment overDueFragment;
    AllFragment allFragment;

    ViewPager viewPager;
    TabLayout tabLayout;

    FloatingActionButton fab, fab_add, fab_delete;
    TextView tv_add, tv_delete;

    String tabTitle[] = {"OVERDUE", "TODAY", "ALL"};
    boolean fab_status;
    ArrayList<Integer> remainingCount;

    public HomeFragment() {
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

        Log.d("Fragment:", "Home onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        fab = rootView.findViewById(R.id.fab);
        fab_add = rootView.findViewById(R.id.fab_add);
        fab_delete = rootView.findViewById(R.id.fab_delete);
        tv_add = rootView.findViewById(R.id.tv_add);
        tv_delete = rootView.findViewById(R.id.tv_delete);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        viewPager = rootView.findViewById(R.id.viewPager);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Fragment:", "Home onResume");
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        setupTabLayout();
        setupTabIcons();
        setupFabs();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Fragment:", "Home onPause");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("Fragment:", "Home onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Fragment:", "Home onDetach");
    }

    private void setupViewPager(ViewPager viewPager) {
        Log.d("Fragment:", "Home setupViewPager");
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        todayFragment = new TodayFragment();
        overDueFragment = new OverdueFragment();
        allFragment = new AllFragment();
        adapter.addFragment(overDueFragment, "OVERDUE");
        adapter.addFragment(todayFragment, "TODAY");
        adapter.addFragment(allFragment, "ALL");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }

    private View prepareTabView(int pos) {
        View view = getLayoutInflater().inflate(R.layout.custom_tab, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_count = (TextView) view.findViewById(R.id.tv_count);
        tv_title.setText(tabTitle[pos]);
        if (remainingCount.get(pos) > 0) {
            tv_count.setVisibility(View.VISIBLE);
            tv_count.setText("" + remainingCount.get(pos));
        } else
            tv_count.setVisibility(View.GONE);


        return view;
    }

    private void setupTabIcons() {
        for (int i = 0; i < tabTitle.length; i++) {
            tabLayout.getTabAt(i).setCustomView(prepareTabView(i));
        }
    }

    private void setupTabLayout() {

        tabLayout.setupWithViewPager(viewPager);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar ca = Calendar.getInstance();
        String currDate = df.format(ca.getTime());

        DBHandler dbHandler = new DBHandler(mActivity);
        remainingCount = new ArrayList<>();
        remainingCount.add(dbHandler.getAllChunksThatAreOverdue(currDate).size());
        remainingCount.add(dbHandler.getAllChunksForToday(currDate).size());
        remainingCount.add(0);
    }

    private void setupFabs() {
        fab_status = false;
        fab_hide();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fab_status == false) {
                    fab_show();
                    fab_status = true;
                } else {
                    fab_hide();
                    fab_status = false;
                }
            }


        });

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, NewSectionActivity.class);
                startActivity(intent);
            }
        });

        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, DeleteSectionActivity.class);
                startActivity(intent);
            }
        });


    }

    private void fab_show() {
        fab_add.show();
        fab_delete.show();
        tv_add.setVisibility(View.VISIBLE);
        tv_delete.setVisibility(View.VISIBLE);
    }

    private void fab_hide() {
        fab_add.hide();
        fab_delete.hide();
        tv_add.setVisibility(View.INVISIBLE);
        tv_delete.setVisibility(View.INVISIBLE);
    }

}
