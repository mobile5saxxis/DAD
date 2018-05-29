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
import mydad.saxxis.activities.MainActivity;
import mydad.saxxis.adapter.ProductAdapter;
import mydad.saxxis.models.CouponResponse;
import mydad.saxxis.models.ProductResponse;
import mydad.saxxis.reterofit.RetrofitInstance;
import mydad.saxxis.reterofit.RetrofitService;
import mydad.saxxis.util.ConnectivityReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SliderProductFragment extends Fragment {

    private static final String SLIDER_ID = "SLIDER_ID";
    private static final String SLIDER_TITLE = "SLIDER_TITLE";
    private ProductAdapter productAdapter;
    private RetrofitService service;
    private String sliderTitle, sliderId;
    private RelativeLayout rl_progress;
    private TextView tv_no_of_items;

    public static SliderProductFragment newInstance(String sliderId, String sliderTitle) {
        SliderProductFragment sPF = new SliderProductFragment();

        Bundle bundle = new Bundle();
        bundle.putString(SLIDER_ID, sliderId);
        bundle.putString(SLIDER_TITLE, sliderTitle);
        sPF.setArguments(bundle);

        return sPF;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            sliderId = getArguments().getString(SLIDER_ID);
            sliderTitle = getArguments().getString(SLIDER_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        rl_progress = view.findViewById(R.id.rl_progress);
        tv_no_of_items = view.findViewById(R.id.tv_no_of_items);

        RecyclerView rv_products = view.findViewById(R.id.rv_popular_brands);
        rv_products.setLayoutManager(new LinearLayoutManager(getContext()));
        productAdapter = new ProductAdapter(getContext());
        rv_products.setAdapter(productAdapter);

        service = RetrofitInstance.createService(RetrofitService.class);

        if (ConnectivityReceiver.isConnected()) {
            makeProductRequest();
        } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        return view;
    }

    private void makeProductRequest() {
        service.getSliderProducts(sliderId).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductResponse cR = response.body();

                    productAdapter.addItems(cR.getData());
                    rl_progress.setVisibility(View.GONE);
                    tv_no_of_items.setText(String.format("%s (%s)", sliderTitle, cR.getData().size()));
                } else {
                    Toast.makeText(getContext(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Toast.makeText(getContext(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
