package com.kamizos.bookbinder.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kamizos.bookbinder.Fragment1;
import com.kamizos.bookbinder.Fragment2;
import com.kamizos.bookbinder.Fragment3;
import com.kamizos.bookbinder.Fragment4;
import com.kamizos.bookbinder.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3, R.string.tab_text_4};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        switch (position) {
            case 0:
                Fragment1 frag1 = new Fragment1();
                return frag1;
            case 1:
                Fragment2 frag2 = new Fragment2();
                return frag2;
            case 2:
                Fragment3 frag3 = new Fragment3();
                return frag3;
            case 3:
                Fragment4 frag4 = new Fragment4();
                return frag4;
            default:
                return PlaceholderFragment.newInstance(position);
        }

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 4;
    }
}