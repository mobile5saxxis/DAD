package codecanyon.grocery.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import codecanyon.grocery.R;
import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.adapter.ProductAdapter;
import codecanyon.grocery.models.Category;
import codecanyon.grocery.models.ProductResponse;
import codecanyon.grocery.reterofit.RetrofitInstance;
import codecanyon.grocery.reterofit.RetrofitService;
import codecanyon.grocery.util.ConnectivityReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularBrandsFragment extends Fragment {

    public static final String SUB_CATEGORY_ID = "CATEGORY_ID";
    public static final String SUB_CATEGORY_TITLE = "CATEGORY_TITLE";
    private RecyclerView rv_category;
    private TextView tv_no_of_items;
    private List<Category> categoryList = new ArrayList<>();
    private List<String> cat_menu_id = new ArrayList<>();
    private ProductAdapter productAdapter;
    private RetrofitService service;
    private String subCategoryId, categoryTitle;
    private RelativeLayout rl_progress;


    public static ProductFragment newInstance(String categoryId, String categoryTitle) {
        ProductFragment productFragment = new ProductFragment();

        Bundle bundle = new Bundle();
        bundle.putString(SUB_CATEGORY_ID, categoryId);
        bundle.putString(SUB_CATEGORY_TITLE, categoryTitle);
        productFragment.setArguments(bundle);

        return productFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            subCategoryId = getArguments().getString(SUB_CATEGORY_ID);
            categoryTitle = getArguments().getString(SUB_CATEGORY_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        service = RetrofitInstance.createService(RetrofitService.class);
        productAdapter = new ProductAdapter(getContext());

        rl_progress = view.findViewById(R.id.rl_progress);
        rv_category = view.findViewById(R.id.rv_subcategory);
        tv_no_of_items = view.findViewById(R.id.tv_no_of_items);
        rv_category.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_category.setAdapter(productAdapter);

        if (getActivity() != null) {
            ((MainActivity) getActivity()).setTitle(categoryTitle);
        }

        if (ConnectivityReceiver.isConnected()) {
            makeGetProductRequest();
        }

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void makeGetProductRequest() {
        service.getBestProducts(subCategoryId).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                ProductResponse pr = response.body();

                productAdapter.addItems(pr.getData());

                tv_no_of_items.setText(String.valueOf(pr.getData().size()));

                if (getContext() != null) {
                    if (pr.getData().isEmpty()) {
                        Toast.makeText(getContext(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                    }
                }

                rl_progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}