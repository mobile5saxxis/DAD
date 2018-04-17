package codecanyon.grocery.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import codecanyon.grocery.adapter.MyOderAdapter;
import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.R;
import codecanyon.grocery.models.DeliveryRequest;
import codecanyon.grocery.models.MyOrder;
import codecanyon.grocery.models.MyOrderResponse;
import codecanyon.grocery.reterofit.APIUrls;
import codecanyon.grocery.reterofit.RetrofitInstance;
import codecanyon.grocery.reterofit.RetrofitService;
import codecanyon.grocery.util.ConnectivityReceiver;
import codecanyon.grocery.util.RecyclerTouchListener;
import codecanyon.grocery.util.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class MyOrderFragment extends Fragment {

    private static String TAG = MyOrderFragment.class.getSimpleName();

    private RecyclerView rv_myorder;
    private MyOderAdapter adapter;

    public MyOrderFragment() {
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
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);

        // handle the touch event if true
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // check user can press back tv_add or not
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    Fragment fm = new HomeFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_layout, fm)
                            .addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });

        rv_myorder = view.findViewById(R.id.rv_myorder);
        rv_myorder.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyOderAdapter();
        rv_myorder.setAdapter(adapter);

        SessionManagement sessionManagement = new SessionManagement(getActivity());
        String user_id = sessionManagement.getUserDetails().get(APIUrls.KEY_ID);

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetOrderRequest(user_id);
        } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        // recyclerview item click listener
        rv_myorder.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_myorder, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MyOrder myOrder = adapter.getItem(position);
                String sale_id = myOrder.getSale_id();
                String date = myOrder.getOn_date();
                String time = myOrder.getDelivery_time_from() + "-" +
                        myOrder.getDelivery_time_to();
                String total = myOrder.getTotal_amount();
                String status = myOrder.getStatus();
                String deli_charge = myOrder.getDelivery_charge();

                Bundle args = new Bundle();
                Fragment fm = new MyOrderDetaiFragment();
                args.putString("sale_id", sale_id);
                args.putString("date", date);
                args.putString("time", time);
                args.putString("total", total);
                args.putString("status", status);
                args.putString("deli_charge", deli_charge);
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_layout, fm)
                        .addToBackStack(null).commit();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return view;
    }

    /**
     * Method to make json array request where json response starts wtih
     */
    private void makeGetOrderRequest(String userid) {
        RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
        service.getOrder(userid).enqueue(new Callback<MyOrderResponse>() {
            @Override
            public void onResponse(Call<MyOrderResponse> call, Response<MyOrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MyOrderResponse myOrderResponse = response.body();

                    adapter.addItems(myOrderResponse.getData());

                    if (myOrderResponse.getData().isEmpty()) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<MyOrderResponse> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
