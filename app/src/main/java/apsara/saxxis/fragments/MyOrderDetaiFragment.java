package apsara.saxxis.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import apsara.saxxis.adapter.MyOrderDetailAdapter;
import apsara.saxxis.activities.MainActivity;
import apsara.saxxis.R;
import apsara.saxxis.models.MyOrderDetail;
import apsara.saxxis.models.MyOrderDetailResponse;
import apsara.saxxis.models.MyOrderResponse;
import apsara.saxxis.models.OrderRequest;
import apsara.saxxis.models.RequestResponse;
import apsara.saxxis.reterofit.APIUrls;
import apsara.saxxis.reterofit.RetrofitInstance;
import apsara.saxxis.reterofit.RetrofitService;
import apsara.saxxis.util.ConnectivityReceiver;
import apsara.saxxis.util.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rajesh Dabhi on 30/6/2017.
 */

public class MyOrderDetaiFragment extends Fragment {

    private static String TAG = MyOrderDetaiFragment.class.getSimpleName();

    private RetrofitService service;
    private String sale_id;
    private MyOrderDetailAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_order_detail, container, false);

        service = RetrofitInstance.createService(RetrofitService.class);
        TextView tv_date = view.findViewById(R.id.tv_order_Detail_date);
        TextView tv_time = view.findViewById(R.id.tv_order_Detail_time);
        TextView tv_delivery_charge = view.findViewById(R.id.tv_order_Detail_deli_charge);
        TextView tv_total = view.findViewById(R.id.tv_order_Detail_total);
        Button btn_cancel = view.findViewById(R.id.btn_order_detail_cancle);
        TextView tv_discount_amount = view.findViewById(R.id.tv_discount_amount);

        RecyclerView rv_detail_order = view.findViewById(R.id.rv_order_detail);
        rv_detail_order.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyOrderDetailAdapter();
        rv_detail_order.setAdapter(adapter);

        sale_id = getArguments().getString("sale_id");
        String total_rs = getArguments().getString("total");
        String date = getArguments().getString("date");
        String time = getArguments().getString("time");
        String status = getArguments().getString("status");
        String deli_charge = getArguments().getString("deli_charge");
        String discount_amount = getArguments().getString("discount_amount");

        if (status.equals("0")) {
            btn_cancel.setVisibility(View.VISIBLE);
        } else {
            btn_cancel.setVisibility(View.GONE);
        }

        tv_total.setText(total_rs);
        tv_date.setText(getResources().getString(R.string.date) + date);
        tv_time.setText(getResources().getString(R.string.time) + time);
        tv_delivery_charge.setText(getResources().getString(R.string.delivery_charge) + deli_charge);
        tv_discount_amount.setText(getString(R.string.saving_amount) + discount_amount);
        tv_discount_amount.setVisibility(View.GONE);

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetOrderDetailRequest(sale_id);
        } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog();
            }
        });

        return view;
    }

    // alertdialog for cancle order
    private void showDeleteDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage(getResources().getString(R.string.cancle_order_note));
        alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                SessionManagement sessionManagement = new SessionManagement(getActivity());
                String user_id = sessionManagement.getUserDetails().get(APIUrls.KEY_ID);

                // check internet connection
                if (ConnectivityReceiver.isConnected()) {
                    makeDeleteOrderRequest(sale_id, user_id);
                }

                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    /**
     * Method to make json array request where json response starts wtih
     */
    private void makeGetOrderDetailRequest(String sale_id) {

        OrderRequest or = new OrderRequest();
        or.setSale_id(sale_id);

        service.orderDetail(sale_id).enqueue(new Callback<MyOrderDetailResponse>() {
            @Override
            public void onResponse(Call<MyOrderDetailResponse> call, Response<MyOrderDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.addItems(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<MyOrderDetailResponse> call, Throwable t) {
                Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeDeleteOrderRequest(String sale_id, String user_id) {

        service.deleteOrder(sale_id, user_id).enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RequestResponse rr = response.body();

                    if (rr.isResponce()) {

                        Toast.makeText(getActivity(), rr.getMessage(), Toast.LENGTH_SHORT).show();

                        getActivity().onBackPressed();

                    } else {
                        Toast.makeText(getActivity(), rr.getError(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RequestResponse> call, Throwable t) {
                Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
