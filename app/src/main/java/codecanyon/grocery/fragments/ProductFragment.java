package codecanyon.grocery.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import codecanyon.grocery.adapter.ProductAdapter;
import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.R;
import codecanyon.grocery.models.Category;
import codecanyon.grocery.models.CategoryResponse;
import codecanyon.grocery.models.Product;
import codecanyon.grocery.models.ProductRequest;
import codecanyon.grocery.models.ProductResponse;
import codecanyon.grocery.reterofit.RetrofitInstance;
import codecanyon.grocery.reterofit.RetrofitService;
import codecanyon.grocery.util.ConnectivityReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class ProductFragment extends Fragment {

    public static final String CATEGORY_ID = "CATEGORY_ID";
    public static final String CATEGORY_TITLE = "CATEGORY_TITLE";
    private RecyclerView rv_category;
    private TextView tv_no_of_items;
    private List<Category> categoryList = new ArrayList<>();
    private List<String> cat_menu_id = new ArrayList<>();
    private ProductAdapter productAdapter;
    private RetrofitService service;
    private String categoryId, categoryTitle;
    private RelativeLayout rl_progress;


    public static ProductFragment newInstance(String categoryId, String categoryTitle) {
        ProductFragment productFragment = new ProductFragment();

        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY_ID, categoryId);
        bundle.putString(CATEGORY_TITLE, categoryTitle);
        productFragment.setArguments(bundle);

        return productFragment;
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
            makeGetCategoryRequest();
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

        ProductRequest pr = new ProductRequest();

        if (!TextUtils.isEmpty(categoryId)) {
            pr.setCat_id(categoryId);
        }

        service.getProducts(pr).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
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
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeGetCategoryRequest() {

        service.getCategories(categoryId).enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    CategoryResponse cr = response.body();

                    categoryList = cr.getData();

                    if (categoryList.isEmpty()) {
                        makeGetProductRequest();
                    }

                    rl_progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem search = menu.findItem(R.id.action_search);
        search.setVisible(false);
        MenuItem check = menu.findItem(R.id.action_change_password);
        check.setVisible(false);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        *//*searchView.setBackgroundColor(getResources().getColor(R.color.white));
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.WHITE);
        searchEditText.setHintTextColor(Color.GRAY);*//*

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                productAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }*/


}
