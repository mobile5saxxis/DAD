package apsara.saxxis.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import apsara.saxxis.R;
import apsara.saxxis.activities.MainActivity;
import apsara.saxxis.adapter.CouponAdapter;
import apsara.saxxis.models.CouponResponse;
import apsara.saxxis.models.SupportInfo;
import apsara.saxxis.models.SupportInfoResponse;
import apsara.saxxis.reterofit.RetrofitInstance;
import apsara.saxxis.reterofit.RetrofitService;
import apsara.saxxis.util.ConnectivityReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponFragment extends Fragment {

    private CouponAdapter couponAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coupon, container, false);

        RecyclerView rv_coupons = view.findViewById(R.id.rv_coupons);
        rv_coupons.setLayoutManager(new LinearLayoutManager(getContext()));
        couponAdapter = new CouponAdapter();
        rv_coupons.setAdapter(couponAdapter);

        if (ConnectivityReceiver.isConnected()) {
            makeCouponRequest();
        } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        return view;
    }

    private void makeCouponRequest() {
        RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
        service.getCoupons().enqueue(new Callback<CouponResponse>() {
            @Override
            public void onResponse(Call<CouponResponse> call, Response<CouponResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CouponResponse cR = response.body();

                    couponAdapter.addItems(cR.getData());
                } else {
                    Toast.makeText(getContext(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CouponResponse> call, Throwable t) {
                Toast.makeText(getContext(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();
            }
        });

    }

}