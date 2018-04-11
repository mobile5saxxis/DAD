package codecanyon.grocery.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import codecanyon.grocery.R;
import codecanyon.grocery.fragments.ProductFragment;
import codecanyon.grocery.fragments.ProductsAboutFragment;
import codecanyon.grocery.fragments.ProductsHealthBenefitsFragment;
import codecanyon.grocery.fragments.ProductsHowToUseFragment;
import codecanyon.grocery.models.Product;
import codecanyon.grocery.models.SubCategory;

public class SectionPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> titles;
    private SparseArray<Fragment> fragments;
    private Product product;

    public SectionPagerAdapter(FragmentManager fM, Context context, Product product) {
        super(fM);
        this.product = product;

        titles = new ArrayList<>();
        titles.add(context.getString(R.string.about));
        titles.add(context.getString(R.string.how_to_use));
        titles.add(context.getString(R.string.health));

        fragments = new SparseArray<>();
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments.get(position) == null) {

            Fragment fragment;

            switch (position) {
                default:
                case 0:
                    fragment = ProductsAboutFragment.newInstance(product.getProduct_description());
                    break;
                case 1:
                    fragment = ProductsHealthBenefitsFragment.newInstance(product.getSpecifications());
                    break;
                case 2:
                    fragment = ProductsHowToUseFragment.newInstance(product.getHow_to_use());
                    break;
            }

            fragments.put(position, fragment);
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
        return titles.get(position);
    }
}