package apsara.saxxis.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import apsara.saxxis.R;
import apsara.saxxis.adapter.CategoryPagerAdapter;
import apsara.saxxis.models.SubCategoryResponse;
import apsara.saxxis.reterofit.RetrofitInstance;
import apsara.saxxis.reterofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryFragment extends Fragment {

    public static final String CATEGORY_ID = "CATEGORY_ID";
    public static final String CATEGORY_TITLE = "CATEGORY_TITLE";
    private String categoryId;
    private String categoryTitle;
    private RelativeLayout rl_loading;
    private TabLayout tab_layout;
    private ViewPager view_pager;
    private CategoryPagerAdapter categoryPagerAdapter;

    public static SubCategoryFragment newInstance(String categoryId, String categoryTitle) {
        SubCategoryFragment subCategoryFragment = new SubCategoryFragment();

        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY_ID, categoryId);
        bundle.putString(CATEGORY_TITLE, categoryTitle);
        subCategoryFragment.setArguments(bundle);

        return subCategoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            categoryId = getArguments().getString(CATEGORY_ID);
            categoryTitle = getArguments().getString(CATEGORY_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_category, container, false);

        rl_loading = view.findViewById(R.id.rl_loading);
        tab_layout = view.findViewById(R.id.tab_layout);
       // tab_layout.getTabAt(0).setCustomView(R.layout.custom_tab_all);
        view_pager = view.findViewById(R.id.view_pager);
        view_pager.setOffscreenPageLimit(0);


        getProducts();

        return view;
    }

    private void getProducts() {
        RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
        service.getSubCategories(categoryId).enqueue(new Callback<SubCategoryResponse>() {
            @Override
            public void onResponse(Call<SubCategoryResponse> call, Response<SubCategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rl_loading.setVisibility(View.GONE);
                    categoryPagerAdapter = new CategoryPagerAdapter(getChildFragmentManager(), response.body().getData());
                    view_pager.setAdapter(categoryPagerAdapter);

                    if (response.body().getData().size() > 4) {
                        tab_layout.setTabMode(TabLayout.MODE_SCROLLABLE);
                    } else {
                        tab_layout.setTabMode(TabLayout.MODE_FIXED);
                    }

                    tab_layout.setupWithViewPager(view_pager);
                }
            }

            @Override
            public void onFailure(Call<SubCategoryResponse> call, Throwable t) {
                Toast.makeText(getContext(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void resetProducts() {
        if (categoryPagerAdapter != null) {
            ProductFragment productFragment = (ProductFragment) categoryPagerAdapter.getItem(view_pager.getCurrentItem());
            productFragment.resetProducts();
        }
    }
}
