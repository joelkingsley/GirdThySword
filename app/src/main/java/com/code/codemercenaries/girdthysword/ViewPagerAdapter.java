package com.code.codemercenaries.girdthysword;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.code.codemercenaries.girdthysword.Fragments.AllFragment;
import com.code.codemercenaries.girdthysword.Fragments.OverdueFragment;
import com.code.codemercenaries.girdthysword.Fragments.TodayFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel Kingsley on 23-04-2018.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("Fragment", "ViewPager getItem " + position);
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new OverdueFragment();
                break;
            case 1:
                fragment = new TodayFragment();
                break;
            case 2:
                fragment = new AllFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }


}
