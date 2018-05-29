package mydad.saxxis.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import mydad.saxxis.R;
import mydad.saxxis.fragments.NewArrivalFragment;

public class BestArrivalPager extends FragmentStatePagerAdapter {

    private List<String> titles;
    private SparseArray<Fragment> fragments;

    public BestArrivalPager(FragmentManager fM, Context context) {
        super(fM);
        this.titles = new ArrayList<>();
        titles.add(context.getString(R.string.bestseller));
        titles.add(context.getString(R.string.newarrival));
        fragments = new SparseArray<>();
    }

    @Override
    public Fragment getItem(int position) {

        if (fragments.get(position) == null) {
            String type = NewArrivalFragment.NEW_ARRIVAL;

            if (position == 0) {
                type = NewArrivalFragment.BEST_PRODUCT;
            }

            fragments.put(position, NewArrivalFragment.newInstance(type));
        }

        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = titles.get(position);

        return title;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
