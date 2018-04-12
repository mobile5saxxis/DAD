package codecanyon.grocery.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import codecanyon.grocery.adapter.MyOrderDetailAdapter;
import codecanyon.grocery.DADApp;
import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.R;
import codecanyon.grocery.models.MyOrderDetail;
import codecanyon.grocery.models.OrderRequest;
import codecanyon.grocery.models.RequestResponse;
import codecanyon.grocery.reterofit.APIUrls;
import codecanyon.grocery.reterofit.RetrofitInstance;
import codecanyon.grocery.reterofit.RetrofitService;
import codecanyon.grocery.util.ConnectivityReceiver;
import codecanyon.grocery.util.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rajesh Dabhi on 30/6/2017.
 */

public class MyOrderDetaiFragment extends Fragment {

    private static String TAG = MyOrderDetaiFragment.class.getSimpleName();

    private TextView tv_date, tv_time, tv_total, tv_delivery_charge;
    private Button btn_cancle;
    private RecyclerView rv_detail_order;
    private RetrofitService service;

    private String sale_id;

    private List<MyOrderDetail> my_orderDetailList = new ArrayList<>();

    public MyOrderDetaiFragment() {
        // Required empty public constructor
    }

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
        tv_date = view.findViewById(R.id.tv_order_Detail_date);
        tv_time = view.findViewById(R.id.tv_order_Detail_time);
        tv_delivery_charge = view.findViewById(R.id.tv_order_Detail_deli_charge);
        tv_total = view.findViewById(R.id.tv_order_Detail_total);
        btn_cancle = view.findViewById(R.id.btn_order_detail_cancle);
        rv_detail_order = view.findViewById(R.id.rv_order_detail);

        rv_detail_order.setLayoutManager(new LinearLayoutManager(getActivity()));

        sale_id = getArguments().getString("sale_id");
        String total_rs = getArguments().getString("total");
        String date = getArguments().getString("date");
        String time = getArguments().getString("time");
        String status = getArguments().getString("status");
        String deli_charge = getArguments().getString("deli_charge");

        if (status.equals("0")) {
            btn_cancle.setVisibility(View.VISIBLE);
        } else {
            btn_cancle.setVisibility(View.GONE);
        }

        tv_total.setText(total_rs);
        tv_date.setText(getResources().getString(R.string.date) + date);
        tv_time.setText(getResources().getString(R.string.time) + time);
        tv_delivery_charge.setText(getResources().getString(R.string.delivery_charge) + deli_charge);

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetOrderDetailRequest(sale_id);
        } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        btn_cancle.setOnClickListener(new View.OnClickListener() {
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

        service.orderDetail(sale_id).enqueue(new Callback<List<MyOrderDetail>>() {
            @Override
            public void onResponse(Call<List<MyOrderDetail>> call, Response<List<MyOrderDetail>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    my_orderDetailList = response.body();

                    MyOrderDetailAdapter adapter = new MyOrderDetailAdapter(my_orderDetailList);
                    rv_detail_order.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    if (my_orderDetailList.isEmpty()) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MyOrderDetail>> call, Throwable t) {
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
