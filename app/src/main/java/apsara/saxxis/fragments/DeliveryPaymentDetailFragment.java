package apsara.saxxis.fragments;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ebs.android.sdk.Config;
import com.ebs.android.sdk.EBSPayment;
import com.ebs.android.sdk.PaymentRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import apsara.saxxis.activities.MainActivity;
import apsara.saxxis.R;
import apsara.saxxis.models.Coupon;
import apsara.saxxis.models.Product;
import apsara.saxxis.models.RequestResponse;
import apsara.saxxis.models.Stock;
import apsara.saxxis.reterofit.APIUrls;
import apsara.saxxis.reterofit.RetrofitService;
import apsara.saxxis.util.ConnectivityReceiver;
import apsara.saxxis.util.DatabaseHandler;
import apsara.saxxis.util.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static apsara.saxxis.reterofit.APIUrls.KEY_EMAIL;
import static apsara.saxxis.reterofit.APIUrls.KEY_MOBILE;
import static apsara.saxxis.reterofit.APIUrls.KEY_NAME;

/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class DeliveryPaymentDetailFragment extends Fragment {

    private static String TAG = DeliveryPaymentDetailFragment.class.getSimpleName();

    private TextView tv_timeslot, tv_address, tv_item, tv_total;

    private String location_id = "";
    private String time = "";
    private String date = "";
    private String address = "";
    private Double totalAmount;
    private String coupon = "0";
    private String couponValue;
    private String mOTP;

    private DatabaseHandler db_cart;
    private SessionManagement sessionManagement;
    private final int ACC_ID = 25376;
    private final String SECRET_KEY = "4aba76a991e26a89b917a16fed4b85a0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm_order, container, false);

        db_cart = new DatabaseHandler();
        sessionManagement = new SessionManagement(getActivity());

        tv_timeslot = view.findViewById(R.id.textTimeSlot);
        tv_address = view.findViewById(R.id.txtAddress);
        //tv_item = (TextView) view.findViewById(R.id.textItems);
        //tv_subcat_total = (TextView) view.findViewById(R.id.textPrice);
        tv_total = view.findViewById(R.id.txtTotal);

        AppCompatButton btn_cod = view.findViewById(R.id.btn_cod);
        AppCompatButton btn_pay_online = view.findViewById(R.id.btn_pay_online);

        date = getArguments().getString("getdate");
        time = getArguments().getString("time");
        location_id = getArguments().getString("location_id");
        int charges = Integer.parseInt(getArguments().getString("deli_charges"));
        address = getArguments().getString("address");
        String min_charge = getArguments().getString("min_charge");

        tv_timeslot.setText(String.format("%s %s", date, time));
        tv_address.setText(address);

        List<Coupon> coupons = Coupon.listAll(Coupon.class);

        if (coupons != null && coupons.size() > 0) {
            Coupon c = coupons.get(0);
            coupon = c.getCouponId();
            couponValue = c.getCoupon_value();
        }

        double amount = Double.parseDouble(db_cart.getDiscountTotalAmount());

        if (amount >= Integer.parseInt(min_charge)) {
            totalAmount = amount;

            if (couponValue == null) {
                tv_total.setText(getResources().getString(R.string.tv_cart_item) + db_cart.getCartCount() + "\n" +
                        getResources().getString(R.string.amount) + db_cart.getDiscountTotalAmount() + "\n" +
                        getResources().getString(R.string.total_amount) +
                        db_cart.getDiscountTotalAmount() + " " + getResources().getString(R.string.currency) + "\n" +
                        getResources().getString(R.string.saving_amount) + db_cart.getSavedAmount());
            } else {
                tv_total.setText(getResources().getString(R.string.tv_cart_item) + db_cart.getCartCount() + "\n" +
                        getResources().getString(R.string.amount) + db_cart.getDiscountTotalAmount() + "\n" +
                        getResources().getString(R.string.coupon_amount) + couponValue + "\n" +
                        getResources().getString(R.string.total_amount) + db_cart.getDiscountTotalAmount() + " " + getResources().getString(R.string.currency) + "\n" +
                        getResources().getString(R.string.saving_amount) + db_cart.getSavedAmount());
            }
        } else {
            totalAmount = amount + charges;

            if (couponValue == null) {
                tv_total.setText(getResources().getString(R.string.tv_cart_item) + db_cart.getCartCount() + "\n" +
                        getResources().getString(R.string.amount) + db_cart.getDiscountTotalAmount() + "\n" +
                        getResources().getString(R.string.delivery_charge) + charges + "\n" +
                        getResources().getString(R.string.total_amount) +
                        db_cart.getDiscountTotalAmount() + " + " + charges + " = " + totalAmount + " " + getResources().getString(R.string.currency) + "\n" +
                        getResources().getString(R.string.saving_amount) + db_cart.getSavedAmount());
            } else {
                tv_total.setText(getResources().getString(R.string.tv_cart_item) + db_cart.getCartCount() + "\n" +
                        getResources().getString(R.string.amount) + db_cart.getDiscountTotalAmount() + "\n" +
                        getResources().getString(R.string.coupon_amount) + couponValue + "\n" +
                        getResources().getString(R.string.delivery_charge) + charges + "\n" +
                        getResources().getString(R.string.total_amount) +
                        db_cart.getDiscountTotalAmount() + " + " + charges + " = " + totalAmount + " " + getResources().getString(R.string.currency) + "\n" +
                        getResources().getString(R.string.saving_amount) + db_cart.getSavedAmount());
            }
        }

        btn_cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check internet connection
                if (ConnectivityReceiver.isConnected()) {
                    attemptOrder(1);
                } else {
                    ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
                }
            }
        });

        btn_pay_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check internet connection
                if (ConnectivityReceiver.isConnected()) {
                    attemptOrder(2);
                } else {
                    ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
                }
            }
        });

        return view;
    }

    private void attemptOrder(int paymentMode) {
        List<Product> items = db_cart.getCartAll();

        if (items.size() > 0) {

            JSONArray passArray = new JSONArray();
            for (Product product : items) {
                JSONObject jObjP = new JSONObject();

                try {
                    jObjP.put("product_id", product.getProduct_id());
                    jObjP.put("qty", product.getQuantity());

                    if (product.getStocks() != null) {
                        List<Stock> stocks = new Gson().fromJson(product.getStocks(), new TypeToken<List<Stock>>() {
                        }.getType());

                        for (Stock stock : stocks) {
                            if (stock.getStockId() == product.getStockId()) {
                                jObjP.put("unit_value", stock.getQuantity());
                                jObjP.put("unit", stock.getStock());

                                if (TextUtils.isEmpty(stock.getStrikeprice())) {
                                    jObjP.put("price", stock.getPrice_val());
                                } else {
                                    jObjP.put("price", stock.getStrikeprice());
                                }

                                jObjP.put("unit_id", stock.getStockId());
                                break;
                            }
                        }
                    }

                    passArray.put(jObjP);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            String user_id = sessionManagement.getUserDetails().get(APIUrls.KEY_ID);

            if (ConnectivityReceiver.isConnected()) {
                makeAddOrderRequest(date, time, user_id, location_id, paymentMode, passArray.toString());
            }
        }
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeAddOrderRequest(final String date, final String gettime, final String userid, final String location, final int paymentMode, final String value) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Placing your order...");
        progressDialog.show();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(APIUrls.BASE_URL)
                .addConverterFactory((GsonConverterFactory.create(gson)));

        builder.build().
                create(RetrofitService.class).
                addOrder(date, gettime, userid, location, value, coupon, paymentMode, mOTP).
                enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        progressDialog.dismiss();

                        if (response.body() != null & response.isSuccessful()) {
                            RequestResponse requestResponse = response.body();

                            if (requestResponse.isResponce()) {

                                if (requestResponse.getOrder_id() == null) {
                                    if (requestResponse.getRequired_otp() != null && requestResponse.getMessage().equalsIgnoreCase("Please Verify Mobile Number With OTP")) {
                                        OTPFragment fragment = new OTPFragment();

                                        fragment.setISetPhoneVerification(new OTPFragment.ISetPhoneVerification() {
                                            @Override
                                            public void onPhoneVerified(String otp) {
                                                mOTP = otp;
                                                makeAddOrderRequest(date, gettime, userid, location, paymentMode, value);
                                            }
                                        });
                                        fragment.show(getChildFragmentManager(), fragment.getTag());
                                    } else {
                                        db_cart.clearCart();
                                        ((MainActivity) getActivity()).setCartCounter(db_cart.getCartCount());
                                        mOTP = null;

                                        Bundle args = new Bundle();
                                        Fragment fm = new ThanksFragment();
                                        args.putString("msg", requestResponse.getData());
                                        fm.setArguments(args);
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.frame_layout, fm)
                                                .addToBackStack(null).commit();
                                    }
                                } else {
                                    SessionManagement sessionManagement = new SessionManagement(getContext());
                                    HashMap<String, String> userDetails = sessionManagement.getUserDetails();

                                    PaymentRequest.getInstance().setTransactionAmount(
                                            String.format("%.2f", totalAmount));

                                    /** Mandatory */

                                    PaymentRequest.getInstance().setAccountId(ACC_ID);
                                    PaymentRequest.getInstance().setSecureKey(SECRET_KEY);

                                    // Reference No
                                    PaymentRequest.getInstance().setReferenceNo(requestResponse.getOrder_id());
                                    /** Mandatory */

                                    // Email Id
                                    //PaymentRequest.getInstance().setBillingEmail("test_tag@testmail.com");

                                    PaymentRequest.getInstance().setBillingEmail(userDetails.get(KEY_EMAIL));
                                    /** Mandatory */

                                    PaymentRequest.getInstance().setFailureid("1");

                                    // PaymentRequest.getInstance().setFailuremessage(getResources().getString(R.string.payment_failure_message));
                                    // System.out.println("FAILURE MESSAGE"+getResources().getString(R.string.payment_failure_message));

                                    /** Mandatory */

                                    // Currency
                                    PaymentRequest.getInstance().setCurrency("INR");
                                    /** Mandatory */

                                    /** Optional */
                                    // Your Reference No or Order Id for this transaction
                                    PaymentRequest.getInstance().setTransactionDescription(requestResponse.getOrder_id());

                                    /** Billing Details */
                                    PaymentRequest.getInstance().setBillingName(userDetails.get(KEY_NAME));
                                    /** Optional */
                                    PaymentRequest.getInstance().setBillingAddress(address);

                                    PaymentRequest.getInstance().setBillingEmail(userDetails.get(KEY_EMAIL));

                                    PaymentRequest.getInstance().setBillingPhone(userDetails.get(KEY_MOBILE));

                                    PaymentRequest.getInstance().setBillingCountry("IND");
                                    /** Optional */
                                    PaymentRequest.getInstance().setBillingCity("Chennai");
                                    /** Optional */
                                    PaymentRequest.getInstance().setBillingPostalCode("600019");
                                    /** Optional */
                                    PaymentRequest.getInstance().setBillingState("Tamilnadu");

                                    PaymentRequest.getInstance().setShippingName(userDetails.get(KEY_NAME));
                                    /** Optional */
                                    PaymentRequest.getInstance().setShippingAddress(address);
                                    /** Optional */
                                    PaymentRequest.getInstance().setShippingCountry("IND");
                                    PaymentRequest.getInstance().setShippingCity("Chennai");
                                    /** Optional */
                                    PaymentRequest.getInstance().setShippingPostalCode("600019");
                                    /** Optional */
                                    PaymentRequest.getInstance().setShippingState("Tamilnadu");

                                    PaymentRequest.getInstance().setShippingEmail(userDetails.get(KEY_EMAIL));
                                    PaymentRequest.getInstance().setShippingPhone(userDetails.get(KEY_MOBILE));

                                    PaymentRequest.getInstance().setLogEnabled("1");

                                    PaymentRequest.getInstance().setHidePaymentOption(false);

                                    /** Optional */
                                    PaymentRequest.getInstance().setHideCashCardOption(false);

                                    /** Optional */
                                    PaymentRequest.getInstance().setHideCreditCardOption(false);

                                    /** Optional */
                                    PaymentRequest.getInstance().setHideDebitCardOption(false);

                                    /** Optional */
                                    PaymentRequest.getInstance().setHideNetBankingOption(false);

                                    /** Optional */
                                    PaymentRequest.getInstance().setHideStoredCardOption(false);

                                    /**
                                     * Initialise parameters for dyanmic values sending from merchant
                                     */

                                    ArrayList<HashMap<String, String>> custom_post_parameters = new ArrayList<HashMap<String, String>>();
                                    HashMap<String, String> hashpostvalues = new HashMap<String, String>();
                                    hashpostvalues.put("account_details", "saving");
                                    hashpostvalues.put("merchant_type", "gold");
                                    custom_post_parameters.add(hashpostvalues);

                                    PaymentRequest.getInstance()
                                            .setCustomPostValues(custom_post_parameters);
                                    /** Optional-Set dyanamic values */

                                    // PaymentRequest.getInstance().setFailuremessage(getResources().getString(R.string.payment_failure_message));

                                    EBSPayment.getInstance().init(getContext(), ACC_ID, SECRET_KEY,
                                            Config.Mode.ENV_LIVE, Config.Encryption.ALGORITHM_SHA512, getString(R.string.host));

                                    Fragment fm = new HomeFragment();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.frame_layout, fm)
                                            .addToBackStack(null).commit();
                                }
                            } else {
                                RequestResponse rr = response.body();

                                Toast.makeText(getActivity(), rr.getError(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RequestResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
