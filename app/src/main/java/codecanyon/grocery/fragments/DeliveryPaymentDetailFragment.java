package codecanyon.grocery.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.R;
import codecanyon.grocery.models.AddOrderRequest;
import codecanyon.grocery.models.Product;
import codecanyon.grocery.models.RequestResponse;
import codecanyon.grocery.reterofit.APIUrls;
import codecanyon.grocery.reterofit.RetrofitInstance;
import codecanyon.grocery.reterofit.RetrofitService;
import codecanyon.grocery.util.ConnectivityReceiver;
import codecanyon.grocery.util.DatabaseHandler;
import codecanyon.grocery.util.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class DeliveryPaymentDetailFragment extends Fragment {

    private static String TAG = DeliveryPaymentDetailFragment.class.getSimpleName();

    private TextView tv_timeslot, tv_address, tv_item, tv_total;
    private Button btn_order;

    private String getlocation_id = "";
    private String gettime = "";
    private String getdate = "";
    private String getuser_id = "";
    private int deli_charges;

    private DatabaseHandler db_cart;
    private SessionManagement sessionManagement;

    public DeliveryPaymentDetailFragment() {
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
        View view = inflater.inflate(R.layout.fragment_confirm_order, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.payment_detail));

        db_cart = new DatabaseHandler();
        sessionManagement = new SessionManagement(getActivity());

        tv_timeslot = view.findViewById(R.id.textTimeSlot);
        tv_address = view.findViewById(R.id.txtAddress);
        //tv_item = (TextView) view.findViewById(R.id.textItems);
        //tv_subcat_total = (TextView) view.findViewById(R.id.textPrice);
        tv_total = view.findViewById(R.id.txtTotal);

        btn_order = view.findViewById(R.id.buttonContinue);

        getdate = getArguments().getString("getdate");
        gettime = getArguments().getString("time");
        getlocation_id = getArguments().getString("location_id");
        deli_charges = Integer.parseInt(getArguments().getString("deli_charges"));
        String getaddress = getArguments().getString("address");

        tv_timeslot.setText(String.format("%s %s", getdate, gettime));
        tv_address.setText(getaddress);

        Double total = Double.parseDouble(db_cart.getTotalAmount()) + deli_charges;

        //tv_subcat_total.setText("" + db_cart.getTotalAmount());
        //tv_item.setText("" + db_cart.getCartCount());
        tv_total.setText(String.format("%s%d\n%s%s\n%s%d\n%s%s + %d = %s %s", getResources().getString(R.string.tv_cart_item), db_cart.getCartCount(), getResources().getString(R.string.amount), db_cart.getTotalAmount(), getResources().getString(R.string.delivery_charge), deli_charges, getResources().getString(R.string.total_amount), db_cart.getTotalAmount(), deli_charges, total, getResources().getString(R.string.currency)));

        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check internet connection
                if (ConnectivityReceiver.isConnected()) {
                    attemptOrder();
                } else {
                    ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
                }
            }
        });

        return view;
    }

    private void attemptOrder() {

        List<Product> items = db_cart.getCartAll();

        if (items.size() > 0) {

            String value = new Gson().toJson(items);

            getuser_id = sessionManagement.getUserDetails().get(APIUrls.KEY_ID);

            if (ConnectivityReceiver.isConnected()) {
                makeAddOrderRequest(getdate, gettime, getuser_id, getlocation_id, value);
            }
        }
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeAddOrderRequest(String date, String gettime, String userid,
                                     String location, String value) {

        AddOrderRequest aor = new AddOrderRequest();
        aor.setDate(date);
        aor.setTime(gettime);
        aor.setUser_id(userid);
        aor.setData(location);
        aor.setData(value);

        RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
        service.addOrder(aor).enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                if (response.body() != null & response.isSuccessful()) {
                    RequestResponse requestResponse = response.body();

                    if (requestResponse.isResponce()) {
                        db_cart.clearCart();
                        ((MainActivity) getActivity()).setCartCounter("" + db_cart.getCartCount());

                        Bundle args = new Bundle();
                        Fragment fm = new ThanksFragment();
                        args.putString("msg", requestResponse.getData());
                        fm.setArguments(args);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.frame_layout, fm)
                                .addToBackStack(null).commit();
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
