package apsara.saxxis.fragments;

import android.os.Handler;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import apsara.saxxis.activities.LoginActivity;
import apsara.saxxis.adapter.CartAdapter;
import apsara.saxxis.activities.MainActivity;
import apsara.saxxis.R;
import apsara.saxxis.models.Coupon;
import apsara.saxxis.models.CouponAvailableResponse;
import apsara.saxxis.models.CouponResponse;
import apsara.saxxis.models.DeliveryCharge;
import apsara.saxxis.models.LimitCheck;
import apsara.saxxis.models.Product;
import apsara.saxxis.reterofit.APIUrls;
import apsara.saxxis.reterofit.RetrofitInstance;
import apsara.saxxis.reterofit.RetrofitService;
import apsara.saxxis.util.ConnectivityReceiver;
import apsara.saxxis.util.DatabaseHandler;
import apsara.saxxis.util.SessionManagement;
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
    private TextView tv_item, tv_total;
    private ImageView iv_clear_coupon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_cart, container, false);

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
        iv_clear_coupon = view.findViewById(R.id.iv_clear_coupon);
        iv_clear_coupon.setOnClickListener(this);

        String coupon = db.getCouponTitle();

        if (coupon == null) {
            iv_clear_coupon.setVisibility(View.GONE);
        } else {
            iv_clear_coupon.setVisibility(View.VISIBLE);
            et_coupon.setText(coupon);
        }

        updateData();

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.iv_clear_coupon:
                if (getContext() != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Clear Coupon?");
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            iv_clear_coupon.setVisibility(View.GONE);
                            et_coupon.setText("");
                            db.deleteCoupon();
                            updateData();
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(R.string.cancal, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
                break;
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
                    iv_clear_coupon.setVisibility(View.VISIBLE);

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
                    boolean couponFound = false;

                    for (final Coupon coupon : cR.getData()) {
                        if (coupon.getCoupon_title().equals(value)) {

                            Calendar cal = Calendar.getInstance();
                            cal.setTime(new Date());
                            cal.set(Calendar.HOUR_OF_DAY, 0);
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);

                            Date todayDate = cal.getTime();

                            if (todayDate.after(coupon.getTo_Date()) || todayDate.before(coupon.getFrom_Date()) || coupon.getStatus().equals("0")) {
                                Toast.makeText(getContext(), R.string.coupon_expired, Toast.LENGTH_SHORT).show();
                            } else if (Double.parseDouble(db.getDiscountTotalAmount()) < Double.parseDouble(coupon.getMinimum_order_Amount())) {
                                Toast.makeText(getContext(), "Minimum order amount for coupon is " + coupon.getMinimum_order_Amount(), Toast.LENGTH_SHORT).show();
                            } else {
                                service.getCouponAvailability(coupon.getCouponId(), sessionManagement.getUserDetails().get(APIUrls.KEY_ID)).enqueue(new Callback<CouponAvailableResponse>() {
                                    @Override
                                    public void onResponse(Call<CouponAvailableResponse> call, Response<CouponAvailableResponse> response) {

                                        if (response.isSuccessful() && response.body() != null) {
                                            CouponAvailableResponse car = response.body();
                                            if (car.isResponce() && car.getData().getCount() > 0) {
                                                db.addCoupon(coupon);
                                                Toast.makeText(getContext(), R.string.coupon_found, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), String.format("Coupon allowed only %s time for user", car.getData().getTimes_Per_user()), Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getContext(), R.string.unable_to_apply_coupon, Toast.LENGTH_SHORT).show();
                                        }

                                        updateData();
                                    }

                                    @Override
                                    public void onFailure(Call<CouponAvailableResponse> call, Throwable t) {
                                        Toast.makeText(getContext(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            couponFound = true;
                            break;
                        }
                    }

                    if (!couponFound) {
                        db.deleteCoupon();
                        Toast.makeText(getContext(), R.string.coupon_invalid, Toast.LENGTH_SHORT).show();
                    }

                    updateData();
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

    private void makeCouponValidate(final String value) {
        service.getCoupons().enqueue(new Callback<CouponResponse>() {
            @Override
            public void onResponse(Call<CouponResponse> call, Response<CouponResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CouponResponse cR = response.body();

                    for (final Coupon coupon : cR.getData()) {
                        if (coupon.getCoupon_title().equals(value)) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(new Date());
                            cal.set(Calendar.HOUR_OF_DAY, 0);
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);

                            Date todayDate = cal.getTime();

                            if (todayDate.after(coupon.getTo_Date()) || todayDate.before(coupon.getFrom_Date()) || coupon.getStatus().equals("0")) {
                                Toast.makeText(getContext(), R.string.coupon_expired, Toast.LENGTH_SHORT).show();
                            } else if (Double.parseDouble(db.getDiscountTotalAmount()) < Double.parseDouble(coupon.getMinimum_order_Amount())) {
                                Toast.makeText(getContext(), "Minimum order amount for coupon is " + coupon.getMinimum_order_Amount(), Toast.LENGTH_SHORT).show();
                            } else {
                                service.getCouponAvailability(coupon.getCouponId(), sessionManagement.getUserDetails().get(APIUrls.KEY_ID)).enqueue(new Callback<CouponAvailableResponse>() {
                                    @Override
                                    public void onResponse(Call<CouponAvailableResponse> call, Response<CouponAvailableResponse> response) {

                                        if (response.isSuccessful() && response.body() != null) {
                                            CouponAvailableResponse car = response.body();
                                            if (car.isResponce() && car.getData().getCount() > 0) {
                                                db.addCoupon(coupon);
                                            } else {
                                                Toast.makeText(getContext(), String.format("Coupon allowed only %s time for user", car.getData().getTimes_Per_user()), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        updateData();
                                    }

                                    @Override
                                    public void onFailure(Call<CouponAvailableResponse> call, Throwable t) {
                                    }
                                });
                            }

                            break;
                        }
                    }

                    updateData();
                } else {
                    updateData();
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
        if (tv_checkout != null) {
            tv_checkout.setText(String.format("PAY \u20B9 %s", db.getDiscountTotalAmount()));
        }

        if (tv_cart_count != null) {
            tv_cart_count.setText(String.format("%s (%s)", getString(R.string.checkout), db.getCartCount()));
        }

        if (getActivity() != null && !getActivity().isFinishing()) {
            ((MainActivity) getActivity()).setCartCounter("" + db.getCartCount());
        }

        if (tv_total != null) {
            if (db.getCouponAmount().equals("0")) {
                tv_total.setText(String.format("Total: %s", db.getDiscountTotalAmount()));
            } else {
                tv_total.setText(String.format("Total: %s - %s = %s", db.getTotalAmount(), db.getCouponAmount(), db.getDiscountTotalAmount()));
            }
        }

        if (tv_item != null) {
            tv_item.setText(String.format("items: %s", db.getCartCount()));
        }
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
                            service.getDeliveryCharge().enqueue(new Callback<DeliveryCharge>() {
                                @Override
                                public void onResponse(Call<DeliveryCharge> call, Response<DeliveryCharge> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        DeliveryCharge deliveryCharge = response.body();

                                        if (deliveryCharge.isResponce()) {
                                            Bundle args = new Bundle();
                                            args.putString("min_charge", deliveryCharge.getAmount());
                                            args.putString("charge", deliveryCharge.getCharge_rs());
                                            Fragment fm = new DeliveryFragment();
                                            fm.setArguments(args);
                                            FragmentManager fragmentManager = getFragmentManager();
                                            fragmentManager.beginTransaction().replace(R.id.frame_layout, fm)
                                                    .addToBackStack(null).commit();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<DeliveryCharge> call, Throwable t) {
                                    Toast.makeText(getContext(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();
                                }
                            });
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
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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

        updateData();
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
                String value = et_coupon.getText().toString().trim();

                if (sessionManagement.isLoggedIn()) {
                    db.deleteCoupon();

                    if (!value.isEmpty()) {
                        makeCouponValidate(value);
                    }
                }
            }
        }
    };

    public void resetProducts() {
        List<Product> products = db.getCartAll();
        cartAdapter.resetItems();
        cartAdapter.addItems(products);
        updateData();

        String value = et_coupon.getText().toString().trim();

        if (!value.isEmpty()) {
            iv_clear_coupon.setVisibility(View.VISIBLE);

            if (sessionManagement.isLoggedIn()) {
                makeCouponValidate(value);
            }
        }
    }
}
