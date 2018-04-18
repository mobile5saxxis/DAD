package codecanyon.grocery.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import codecanyon.grocery.activities.ProductDetailsActivity;
import codecanyon.grocery.adapter.OfferListAdapter;
import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.R;
import codecanyon.grocery.adapter.ProductAdapter;
import codecanyon.grocery.models.OffersResponse;
import codecanyon.grocery.models.Product;
import codecanyon.grocery.models.ProductResponse;
import codecanyon.grocery.reterofit.RetrofitInstance;
import codecanyon.grocery.reterofit.RetrofitService;
import codecanyon.grocery.util.ConnectivityReceiver;
import codecanyon.grocery.util.RecyclerTouchListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by srikarn on 26-03-2018.
 */

public class OffersFragment extends Fragment {

    private RetrofitService service;
    private TextView tv_no_of_items;
    private ProductAdapter productAdapter;
    private RelativeLayout rl_progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_offers, container, false);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (getContext() != null) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
        });

        service = RetrofitInstance.createService(RetrofitService.class);

        productAdapter = new ProductAdapter(getContext());

        rl_progress = view.findViewById(R.id.rl_progress);
        RecyclerView rv_offers = view.findViewById(R.id.rv_offers);
        tv_no_of_items = view.findViewById(R.id.tv_no_of_items);
        rv_offers.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_offers.setAdapter(productAdapter);

        if (ConnectivityReceiver.isConnected()) {
            makeGetOfferRequest();
        }

        return view;
    }


    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeGetOfferRequest() {

        service.getGetOffers().enqueue(new Callback<OffersResponse>() {
            @Override
            public void onResponse(Call<OffersResponse> call, Response<OffersResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    OffersResponse or = response.body();

                    productAdapter.addItems(or.getData());

                    tv_no_of_items.setText(String.format("%s (%s)", getString(R.string.offer_name), or.getData().size()));

                    if (getContext() != null) {
                        if (or.getData().isEmpty()) {
                            Toast.makeText(getContext(), R.string.no_rcord_found, Toast.LENGTH_SHORT).show();
                        }
                    }

                    rl_progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<OffersResponse> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();

            }
        });
    }

}
