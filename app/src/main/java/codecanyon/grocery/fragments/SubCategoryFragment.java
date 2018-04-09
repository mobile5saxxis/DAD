package codecanyon.grocery.fragments;

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

import codecanyon.grocery.R;
import codecanyon.grocery.adapter.CategoryPagerAdapter;
import codecanyon.grocery.models.SubCategoryResponse;
import codecanyon.grocery.reterofit.RetrofitInstance;
import codecanyon.grocery.reterofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryFragment extends Fragment {

    public static final String CATEGORY_ID = "CATEGORY_ID";
    public static final String CATEGORY_TITLE = "CATEGORY_TITLE";
    private String categoryId;
    private String categoryTitle;

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

        final RelativeLayout rl_loading = view.findViewById(R.id.rl_loading);
        final TabLayout tab_layout = view.findViewById(R.id.tab_layout);
        final ViewPager view_pager = view.findViewById(R.id.view_pager);

        RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
        service.getSubCategories(categoryId).enqueue(new Callback<SubCategoryResponse>() {
            @Override
            public void onResponse(Call<SubCategoryResponse> call, Response<SubCategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rl_loading.setVisibility(View.GONE);
                    CategoryPagerAdapter categoryPagerAdapter = new CategoryPagerAdapter(getChildFragmentManager(), response.body().getData());
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

        return view;
    }
}
