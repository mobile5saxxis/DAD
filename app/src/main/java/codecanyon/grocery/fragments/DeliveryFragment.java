package codecanyon.grocery.fragments;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import codecanyon.grocery.R;
import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.adapter.DeliveryAddressAdapter;
import codecanyon.grocery.models.AddressResponse;
import codecanyon.grocery.models.DeliveryAddress;
import codecanyon.grocery.models.DeliveryRequest;
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
 * Created by Rajesh Dabhi on 27/6/2017.
 */

public class DeliveryFragment extends Fragment implements View.OnClickListener {

    private static String TAG = DeliveryFragment.class.getSimpleName();

    private TextView tv_afternoon, tv_morning, tv_total, tv_item, tv_socity;
    private TextView tv_date, tv_time, tv_add_address;
    private EditText et_address;
    private Button btn_checkout;
    private RecyclerView rv_address;

    private DeliveryAddressAdapter adapter;
    private List<DeliveryAddress> delivery_addressList = new ArrayList<>();

    private DatabaseHandler db_cart;

    private SessionManagement sessionManagement;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private String gettime = "";
    private String getdate = "";

    private String deli_charges;

    public DeliveryFragment() {
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
        View view = inflater.inflate(R.layout.fragment_delivery_time, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.delivery));

        tv_date = view.findViewById(R.id.tv_deli_date);
        tv_time = view.findViewById(R.id.tv_deli_fromtime);
        tv_add_address = view.findViewById(R.id.tv_deli_add_address);
        tv_total = view.findViewById(R.id.tv_deli_total);
        tv_item = view.findViewById(R.id.tv_deli_item);
        btn_checkout = view.findViewById(R.id.btn_deli_checkout);
        rv_address = view.findViewById(R.id.rv_deli_address);
        rv_address.setLayoutManager(new LinearLayoutManager(getActivity()));
        //tv_socity = (TextView) view.findViewById(R.id.tv_deli_socity);
        //et_address = (EditText) view.findViewById(R.id.et_deli_address);

        db_cart = new DatabaseHandler();
        tv_total.setText(String.format("%s", db_cart.getTotalAmount()));
        tv_item.setText(String.format("%s", db_cart.getCartCount()));

        // get session user data
        sessionManagement = new SessionManagement(getActivity());
        String getsocity = sessionManagement.getUserDetails().get(APIUrls.KEY_SOCITY_NAME);
        String getaddress = sessionManagement.getUserDetails().get(APIUrls.KEY_HOUSE);

        //tv_socity.setText("Socity Name: " + getsocity);
        //et_address.setText(getaddress);

        tv_date.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        tv_add_address.setOnClickListener(this);
        btn_checkout.setOnClickListener(this);

        String date = sessionManagement.getdatetime().get(APIUrls.KEY_DATE);
        String time = sessionManagement.getdatetime().get(APIUrls.KEY_TIME);

        if (date != null && time != null) {

            getdate = date;
            gettime = time;

            try {
                String inputPattern = "yyyy-MM-dd";
                String outputPattern = "dd-MM-yyyy";
                SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                Date date1 = inputFormat.parse(getdate);
                String str = outputFormat.format(date1);

                tv_date.setText(getResources().getString(R.string.delivery_date) + str);

            } catch (ParseException e) {
                e.printStackTrace();

                tv_date.setText(getResources().getString(R.string.delivery_date) + getdate);
            }

            tv_time.setText(time);
        }

        if (ConnectivityReceiver.isConnected()) {
            String user_id = sessionManagement.getUserDetails().get(APIUrls.KEY_ID);
            makeGetAddressRequest(user_id);
        } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_deli_checkout:
                attemptOrder();
                break;

            case R.id.tv_deli_date:
                getDate();
                break;

            case R.id.tv_deli_fromtime:
                if (TextUtils.isEmpty(getdate)) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.please_select_date), Toast.LENGTH_SHORT).show();
                } else {
                    Bundle args = new Bundle();
                    Fragment fm = new ViewTimeFragment();
                    args.putString("date", getdate);
                    fm.setArguments(args);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_layout, fm)
                            .addToBackStack(null).commit();
                }
                break;

            case R.id.tv_deli_add_address:
                sessionManagement.updateSocity("", "");

                Fragment fm = new AddDeliveryAddressFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_layout, fm)
                        .addToBackStack(null).commit();
                break;
        }
    }

    private void getDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                R.style.datepicker,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        getdate = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                        tv_date.setText(String.format("%s%s", getResources().getString(R.string.delivery_date), getdate));

                        try {
                            String inputPattern = "yyyy-MM-dd";
                            String outputPattern = "dd-MM-yyyy";
                            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.getDefault());
                            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.getDefault());

                            Date date = inputFormat.parse(getdate);
                            String str = outputFormat.format(date);

                            tv_date.setText(String.format("%s%s", getResources().getString(R.string.delivery_date), str));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            tv_date.setText(String.format("%s%s", getResources().getString(R.string.delivery_date), getdate));
                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }

    private void attemptOrder() {

        //String getaddress = et_address.getText().toString();

        String location_id = "";
        String address = "";

        boolean cancel = false;

        if (TextUtils.isEmpty(getdate)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.please_select_date_time), Toast.LENGTH_SHORT).show();
            cancel = true;
        } else if (TextUtils.isEmpty(gettime)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.please_select_date_time), Toast.LENGTH_SHORT).show();
            cancel = true;
        }

        if (!delivery_addressList.isEmpty()) {
            if (adapter.ischeckd()) {
                location_id = adapter.getlocation_id();
                address = adapter.getaddress();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.please_select_address), Toast.LENGTH_SHORT).show();
                cancel = true;
            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.please_add_address), Toast.LENGTH_SHORT).show();
            cancel = true;
        }

        /*if (TextUtils.isEmpty(getaddress)) {
            Toast.makeText(getActivity(), "Please add your address", Toast.LENGTH_SHORT).show();
            cancel = true;
        }*/

        if (!cancel) {
            //Toast.makeText(getActivity(), "date:"+getDate+"Fromtime:"+getfrom_time+"Todate:"+getto_time, Toast.LENGTH_SHORT).show();

            sessionManagement.cleardatetime();

            Bundle args = new Bundle();
            Fragment fm = new DeliveryPaymentDetailFragment();
            args.putString("getdate", getdate);
            args.putString("time", gettime);
            args.putString("location_id", location_id);
            args.putString("address", address);
            args.putString("deli_charges", deli_charges);
            fm.setArguments(args);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout, fm)
                    .addToBackStack(null).commit();

        }
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeGetAddressRequest(String user_id) {
        RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
        service.getDeliveryAddressList(user_id).enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    AddressResponse aR = response.body();

                    if (aR.isResponce()) {
                        delivery_addressList.clear();
                        delivery_addressList = aR.getData();
                        adapter = new DeliveryAddressAdapter(delivery_addressList);
                        adapter.setMode(Attributes.Mode.Single);
                        rv_address.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();
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
        // register reciver
        getActivity().registerReceiver(mCart, new IntentFilter("Grocery_delivery_charge"));
    }

    // broadcast reciver for receive data
    private BroadcastReceiver mCart = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");

            if (type.contentEquals("update")) {
                //updateData();
                deli_charges = intent.getStringExtra("charge");
                //Toast.makeText(getActivity(), deli_charges, Toast.LENGTH_SHORT).show();

                Double total = Double.parseDouble(db_cart.getTotalAmount()) + Integer.parseInt(deli_charges);

                tv_total.setText("" + db_cart.getTotalAmount() + " + " + deli_charges + " = " + total);
            }
        }
    };

}
