package mydad.saxxis.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mydad.saxxis.adapter.ProductAdapter;
import mydad.saxxis.activities.MainActivity;
import mydad.saxxis.R;
import mydad.saxxis.models.Category;
import mydad.saxxis.models.ProductResponse;
import mydad.saxxis.reterofit.RetrofitInstance;
import mydad.saxxis.reterofit.RetrofitService;
import mydad.saxxis.util.ConnectivityReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class ProductFragment extends Fragment {

    public static final String SUB_CATEGORY_ID = "CATEGORY_ID";
    public static final String SUB_CATEGORY_TITLE = "CATEGORY_TITLE";
    private TextView tv_no_of_items;
    private ProductAdapter productAdapter;
    private RetrofitService service;
    private String subCategoryId;
    private RelativeLayout rl_progress;
    private TextView tv_sort;

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
            String categoryTitle = getArguments().getString(SUB_CATEGORY_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        service = RetrofitInstance.createService(RetrofitService.class);
        productAdapter = new ProductAdapter(getContext());

        tv_no_of_items = view.findViewById(R.id.tv_no_of_items);
        tv_sort = view.findViewById(R.id.tv_sort);

        rl_progress = view.findViewById(R.id.rl_progress);
        RecyclerView rv_category = view.findViewById(R.id.rv_popular_brands);
        rv_category.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_category.setAdapter(productAdapter);

        if (ConnectivityReceiver.isConnected()) {
            makeGetProductRequest("");
        }

        view.findViewById(R.id.ll_sort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int highToLow = 1245;
                final int lowToHigh = 1246;
                final int aToZ = 1247;

                PopupMenu menu = new PopupMenu(getContext(), v);
                menu.getMenu().add(0, highToLow, 0, "Price High to Low");
                menu.getMenu().add(0, lowToHigh, 0, "Price Low to High");
                menu.getMenu().add(0, aToZ, 0, "Order By A to Z");
                menu.show();

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case highToLow:
                                rl_progress.setVisibility(View.VISIBLE);
                                productAdapter.resetItems();
                                makeGetProductRequest("htol");
                                tv_sort.setText(R.string.price_high);
                                return true;
                            case lowToHigh:
                                rl_progress.setVisibility(View.VISIBLE);
                                productAdapter.resetItems();
                                makeGetProductRequest("ltoh");
                                tv_sort.setText(R.string.price_low);
                                return true;
                            case aToZ:
                                rl_progress.setVisibility(View.VISIBLE);
                                productAdapter.resetItems();
                                makeGetProductRequest("alpha");
                                tv_sort.setText(R.string.a_to_z);
                                return true;
                        }

                        return false;
                    }
                });
            }
        });

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


    private void makeGetProductRequest(String sortBy) {
        service.getProducts(subCategoryId, sortBy).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    ProductResponse pr = response.body();

                    if (getActivity() != null && !getActivity().isFinishing()) {
                        productAdapter.addItems(pr.getData());

                        tv_no_of_items.setText(String.format(getString(R.string.no_of_items), String.valueOf(pr.getData().size())));

                        rl_progress.setVisibility(View.GONE);
                    }
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

    public void resetProducts() {
        rl_progress.setVisibility(View.VISIBLE);
        productAdapter.resetItems();
        makeGetProductRequest("");
    }
}
