package codecanyon.grocery.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import codecanyon.grocery.fragments.ProductFragment;
import codecanyon.grocery.models.SubCategory;

public class CategoryPagerAdapter extends FragmentStatePagerAdapter {

    private List<SubCategory> subCategories;
    private List<ProductFragment> productFragments;

    public CategoryPagerAdapter(FragmentManager fM, List<SubCategory> subCategories) {
        super(fM);
        this.subCategories = subCategories;
        productFragments = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        SubCategory subCategory = subCategories.get(position);

        if (position >= productFragments.size()) {
            productFragments.add(ProductFragment.newInstance(subCategory.getId(), subCategory.getTitle()));
        }

        return productFragments.get(position);
    }

    @Override
    public int getCount() {
        return subCategories.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        SubCategory subCategory = subCategories.get(position);

        return subCategory.getTitle();
    }
}
