package codecanyon.grocery.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import codecanyon.grocery.R;
import codecanyon.grocery.adapter.BestSellersAdapter;
import codecanyon.grocery.models.BestProductResponse;
import codecanyon.grocery.models.ProductResponse;
import codecanyon.grocery.reterofit.RetrofitInstance;
import codecanyon.grocery.reterofit.RetrofitService;
import codecanyon.grocery.util.ConnectivityReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewArrivalFragment extends Fragment {

    public static final String TYPE = "TYPE";
    public static final String BEST_PRODUCT = "BEST_PRODUCT";
    public static final String NEW_ARRIVAL = "NEW_ARRIVAL";
    private BestSellersAdapter newArrivalAdapter;
    private RetrofitService service;
    private RelativeLayout rl_progress;
    private String type;

    public static NewArrivalFragment newInstance(String type) {
        NewArrivalFragment newArrivalFragment = new NewArrivalFragment();

        Bundle bundle = new Bundle();
        bundle.putString(TYPE, type);
        newArrivalFragment.setArguments(bundle);

        return newArrivalFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            type = getArguments().getString(TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_arrival, container, false);

        service = RetrofitInstance.createService(RetrofitService.class);
        newArrivalAdapter = new BestSellersAdapter(getContext());

        rl_progress = view.findViewById(R.id.rl_progress);
        RecyclerView rv_new_arrivals = view.findViewById(R.id.rv_new_arrivals);
        rv_new_arrivals.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_new_arrivals.setAdapter(newArrivalAdapter);

        if (ConnectivityReceiver.isConnected()) {
            if (type.equals(NEW_ARRIVAL)) {
                makeRequest();
            } else {
                makeGetBestProductsRequest();
            }
        }

        return view;
    }

    private void makeRequest() {
        service.getNewArrivals().enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                ProductResponse pr = response.body();

                newArrivalAdapter.addItems(pr.getData());

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

    private void makeGetBestProductsRequest() {

        service.getBestProducts().enqueue(new Callback<BestProductResponse>() {
            @Override
            public void onResponse(Call<BestProductResponse> call, Response<BestProductResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    BestProductResponse cr = response.body();
                    newArrivalAdapter.addItems(cr.getData());

                    rl_progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<BestProductResponse> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();

            }
        });
    }
}