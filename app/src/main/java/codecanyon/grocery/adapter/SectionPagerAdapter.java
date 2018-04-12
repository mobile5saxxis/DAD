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
import codecanyon.grocery.fragments.ProductsAboutFragment;
import codecanyon.grocery.fragments.ProductsHealthBenefitsFragment;
import codecanyon.grocery.fragments.ProductsHowToUseFragment;
import codecanyon.grocery.models.Product;

public class SectionPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> titles;
    private List<String> types;
    private SparseArray<Fragment> fragments;
    private Product product;
    private final String ABOUT = "ABOUT";
    private final String HOW_TO_USE = "HOW_TO_USE";
    private final String HEALTH_BENEFITS = "HEALTH_BENEFITS";

    public SectionPagerAdapter(FragmentManager fM, Context context, Product product) {
        super(fM);
        this.product = product;
        types = new ArrayList<>();
        titles = new ArrayList<>();

        if (!product.getProduct_description().trim().isEmpty()) {
            titles.add(context.getString(R.string.about));
            types.add(ABOUT);
        }

        if (!product.getHow_to_use().trim().isEmpty()) {
            titles.add(context.getString(R.string.how_to_use));
            types.add(HOW_TO_USE);
        }

        if (!product.getSpecifications().trim().isEmpty()) {
            titles.add(context.getString(R.string.health));
            types.add(HEALTH_BENEFITS);
        }

        fragments = new SparseArray<>();
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments.get(position) == null) {
            String type = types.get(position);

            Fragment fragment;

            switch (type) {
                default:
                case ABOUT:
                    fragment = ProductsAboutFragment.newInstance(product.getProduct_description());
                    break;
                case HEALTH_BENEFITS:
                    fragment = ProductsHealthBenefitsFragment.newInstance(product.getSpecifications());
                    break;
                case HOW_TO_USE:
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