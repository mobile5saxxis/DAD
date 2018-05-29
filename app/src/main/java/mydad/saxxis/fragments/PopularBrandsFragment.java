package mydad.saxxis.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import mydad.saxxis.R;
import mydad.saxxis.adapter.ProductAdapter;
import mydad.saxxis.models.ProductResponse;
import mydad.saxxis.reterofit.RetrofitInstance;
import mydad.saxxis.reterofit.RetrofitService;
import mydad.saxxis.util.ConnectivityReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularBrandsFragment extends Fragment {

    public static final String POPULAR_BRAND_ID = "NEW_ARRIVAL_ID";
    public static final String POPULAR_BRAND_TITLE = "NEW_ARRIVAL_TITLE";
    private TextView tv_no_of_items;
    private ProductAdapter productAdapter;
    private RetrofitService service;
    private String brandId, brandTitle;
    private RelativeLayout rl_progress;


    public static PopularBrandsFragment newInstance(String brandId, String brandTitle) {
        PopularBrandsFragment productFragment = new PopularBrandsFragment();

        Bundle bundle = new Bundle();
        bundle.putString(POPULAR_BRAND_ID, brandId);
        bundle.putString(POPULAR_BRAND_TITLE, brandTitle);
        productFragment.setArguments(bundle);

        return productFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            brandId = getArguments().getString(POPULAR_BRAND_ID);
            brandTitle = getArguments().getString(POPULAR_BRAND_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        service = RetrofitInstance.createService(RetrofitService.class);
        productAdapter = new ProductAdapter(getContext());

        rl_progress = view.findViewById(R.id.rl_progress);
        RecyclerView rv_popular_brands = view.findViewById(R.id.rv_popular_brands);
        tv_no_of_items = view.findViewById(R.id.tv_no_of_items);
        rv_popular_brands.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_popular_brands.setAdapter(productAdapter);

        if (ConnectivityReceiver.isConnected()) {
            makeGetProductRequest();
        }

        return view;
    }

    private void makeGetProductRequest() {
        service.getPopularBrands(brandId).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                ProductResponse pr = response.body();

                productAdapter.addItems(pr.getData());

                tv_no_of_items.setText(String.format("%s (%s)", brandTitle, pr.getData().size()));

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