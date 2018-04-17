package codecanyon.grocery.fragments;

import android.support.v4.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import codecanyon.grocery.activities.LoginActivity;
import codecanyon.grocery.adapter.CartAdapter;
import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.R;
import codecanyon.grocery.models.Coupon;
import codecanyon.grocery.models.CouponAvailableResponse;
import codecanyon.grocery.models.CouponResponse;
import codecanyon.grocery.models.LimitCheck;
import codecanyon.grocery.models.Product;
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
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class CartFragment extends Fragment implements View.OnClickListener {

    private static String TAG = CartFragment.class.getSimpleName();

    private RecyclerView rv_cart;
    private TextView tv_checkout, tv_cart_clear, tv_cart_count;
    private DatabaseHandler db;
    private CartAdapter cartAdapter;
    private SessionManagement sessionManagement;
    private RetrofitService service;
    private EditText et_coupon;
    private boolean isFound;
    private TextView tv_item, tv_total;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        sessionManagement = new SessionManagement(getActivity());
        sessionManagement.cleardatetime();

        tv_cart_count = view.findViewById(R.id.tv_cart_count);
        tv_cart_clear = view.findViewById(R.id.tv_cart_clear);
        tv_checkout = view.findViewById(R.id.tv_checkout);
        rv_cart = view.findViewById(R.id.rv_cart);
        rv_cart.setLayoutManager(new LinearLayoutManager(getActivity()));

        db = new DatabaseHandler();

        List<Product> products = db.getCartAll();

        cartAdapter = new CartAdapter(getContext());
        rv_cart.setAdapter(cartAdapter);

        cartAdapter.addItems(products);

        et_coupon = view.findViewById(R.id.et_coupon);
        view.findViewById(R.id.tv_add_coupon).setOnClickListener(this);
        service = RetrofitInstance.createService(RetrofitService.class);
        tv_cart_clear.setOnClickListener(this);
        tv_checkout.setOnClickListener(this);

        tv_item = view.findViewById(R.id.tv_item);
        tv_total = view.findViewById(R.id.tv_total);

        updateData();

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.tv_cart_clear:
                showClearDialog();
                break;
            case R.id.tv_checkout:
                if (ConnectivityReceiver.isConnected()) {
                    makeGetLimiterRequest();
                } else {
                    ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
                }
                break;
            case R.id.tv_add_coupon:
                String value = et_coupon.getText().toString().trim();

                if (value.isEmpty()) {
                    Toast.makeText(getContext(), R.string.add_a_coupon_to_apply, Toast.LENGTH_SHORT).show();
                } else {

                    if (sessionManagement.isLoggedIn()) {
                        makeCouponRequest(value);
                    } else {
                        startActivity(new Intent(getContext(), LoginActivity.class));
                    }
                }
                break;
        }
    }


    private void makeCouponRequest(final String value) {
        service.getCoupons().enqueue(new Callback<CouponResponse>() {
            @Override
            public void onResponse(Call<CouponResponse> call, Response<CouponResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CouponResponse cR = response.body();

                    isFound = false;

                    for (final Coupon coupon : cR.getData()) {
                        if (coupon.getCoupon_title().equals(value)) {

                            Date todayDate = new Date();

                            if (todayDate.after(coupon.getFrom_Date()) && todayDate.before(coupon.getTo_Date()) && coupon.getStatus().equals("0")) {
                                Toast.makeText(getContext(), R.string.coupon_expired, Toast.LENGTH_SHORT).show();
                            } else {
                                service.getCouponAvailability(coupon.getCouponId(), sessionManagement.getUserDetails().get(APIUrls.KEY_ID)).enqueue(new Callback<CouponAvailableResponse>() {
                                    @Override
                                    public void onResponse(Call<CouponAvailableResponse> call, Response<CouponAvailableResponse> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            CouponAvailableResponse car = response.body();
                                            if (car.isResponce() && car.getData().getCount() > 0) {
                                                db.addCoupon(coupon);
                                                isFound = true;
                                                et_coupon.setText("");
                                                Toast.makeText(getContext(), R.string.coupon_found, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), String.format("Coupon allowed only %s time for user", car.getData().getTimes_Per_user()), Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getContext(), R.string.unable_to_apply_coupon, Toast.LENGTH_SHORT).show();
                                        }

                                        if (isFound) {
                                            updateData();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<CouponAvailableResponse> call, Throwable t) {
                                        Toast.makeText(getContext(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            break;
                        }
                    }
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

    // update UI
    private void updateData() {
        tv_checkout.setText(String.format("PAY \u20B9 %s", db.getDiscountTotalAmount()));
        tv_cart_count.setText(String.format("%s (%s)", getString(R.string.checkout), db.getCartCount()));
        ((MainActivity) getActivity()).setCartCounter("" + db.getCartCount());

        if (db.getCouponAmount().equals("0")) {
            tv_total.setText(String.format("Total: %s", db.getDiscountTotalAmount()));
        } else {
            tv_total.setText(String.format("Total: %s - %s = %s", db.getTotalAmount(), db.getCouponAmount(), db.getDiscountTotalAmount()));
        }

        tv_item.setText(String.format("items: %s", db.getCartCount()));
    }

    private void showClearDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage("Are you sure!\nyou want to delete all cart product");
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.clearCart();
                List<Product> products = db.getCartAll();
                cartAdapter.resetItems();
                cartAdapter.addItems(products);

                updateData();

                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    /**
     * Method to make json array request where json response starts wtih
     */
    private void makeGetLimiterRequest() {

        service.limitCheck().enqueue(new Callback<List<LimitCheck>>() {
            @Override
            public void onResponse(Call<List<LimitCheck>> call, Response<List<LimitCheck>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Double total_amount = Double.parseDouble(db.getDiscountTotalAmount());

                    boolean isSmall = false;
                    boolean isBig = false;

                    for (LimitCheck limitCheck : response.body()) {

                        if (limitCheck.getId().equals("1")) {
                            if (total_amount < limitCheck.getValue()) {
                                isSmall = true;
                                Toast.makeText(getActivity(), String.format("%s : %s", limitCheck.getTitle(), limitCheck.getValue()), Toast.LENGTH_SHORT).show();
                            }
                        } else if (limitCheck.getId().equals("2")) {
                            if (total_amount > limitCheck.getValue()) {
                                isBig = true;
                                Toast.makeText(getActivity(), String.format("%s : %s", limitCheck.getTitle(), limitCheck.getValue()), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    if (!isSmall && !isBig) {
                        if (sessionManagement.isLoggedIn()) {
                            Bundle args = new Bundle();
                            Fragment fm = new DeliveryFragment();
                            fm.setArguments(args);
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.frame_layout, fm)
                                    .addToBackStack(null).commit();
                        } else {
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            startActivity(i);
                        }
                    }
                } else {
                    Toast.makeText(getContext(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LimitCheck>> call, Throwable t) {
                Toast.makeText(getContext(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();
            }
        });
//
//        JsonArrayRequest req = new JsonArrayRequest(APIUrls.GET_LIMITE_SETTING_URL,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d(TAG, response.toString());
//
//                        Double total_amount = Double.parseDouble(db.getDiscountTotalAmount());
//
//                        try {
//                            // Parsing json array response
//                            // loop through each json object
//
//                            boolean issmall = false;
//                            boolean isbig = false;
//
//                            // arraylist list variable for store data;
//                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
//
//                            for (int i = 0; i < response.length(); i++) {
//
//                                JSONObject jsonObject = (JSONObject) response
//                                        .get(i);
//                                int value;
//
//                                if (jsonObject.getString("id").equals("1")) {
//                                    value = Integer.parseInt(jsonObject.getString("value"));
//
//                                    if (total_amount < value) {
//                                        issmall = true;
//                                        Toast.makeText(getActivity(), "" + jsonObject.getString("tv_subcat_title") + " : " + value, Toast.LENGTH_SHORT).show();
//                                    }
//                                } else if (jsonObject.getString("id").equals("2")) {
//                                    value = Integer.parseInt(jsonObject.getString("value"));
//
//                                    if (total_amount > value) {
//                                        isbig = true;
//                                        Toast.makeText(getActivity(), "" + jsonObject.getString("tv_subcat_title") + " : " + value, Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
//                            }
//
//                            if (!issmall && !isbig) {
//                                if(sessionManagement.isLoggedIn()) {
//                                    Bundle args = new Bundle();
//                                    Fragment fm = new Delivery_fragment();
//                                    fm.setArguments(args);
//                                    FragmentManager fragmentManager = getFragmentManager();
//                                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                                            .addToBackStack(null).commit();
//                                }else {
//                                    //Toast.makeText(getActivity(), "Please login or regiter.\ncontinue", Toast.LENGTH_SHORT).show();
//                                    Intent i = new Intent(getActivity(), LoginActivity.class);
//                                    startActivity(i);
//                                }
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(getActivity(),
//                                    "Error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(getActivity(), "Connection Time out", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        // Adding request to request queue
//        DADApp.getInstance().addToRequestQueue(req);

    }

    @Override
    public void onPause() {
        super.onPause();
        // unregister reciver
        getActivity().unregisterReceiver(mCart);
    }

    @Override
    public void onResume() {
        super.onResume();
        // register reciver
        getActivity().registerReceiver(mCart, new IntentFilter("Grocery_cart"));
    }

    // broadcast reciver for receive data
    private BroadcastReceiver mCart = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");

            if (type.contentEquals("update")) {
                updateData();
            }
        }
    };

}
