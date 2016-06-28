package com.nad.utility.blocker.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import com.nad.utility.blocker.R;
import com.nad.utility.blocker.fragment.CallFragment;
import com.nad.utility.blocker.fragment.GeneralFragment;
import com.nad.utility.blocker.fragment.ContactFragment;
import com.nad.utility.blocker.fragment.SMSFragment;

public class PageFragmentAdapter extends FragmentPagerAdapter {
    private String[] tabTitles;
    private Fragment[] fragments;

    public PageFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);

        tabTitles = context.getResources().getStringArray(R.array.tabTitles);
        fragments = new Fragment[]{new SMSFragment(), new CallFragment(), new ContactFragment(), new GeneralFragment()};
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}