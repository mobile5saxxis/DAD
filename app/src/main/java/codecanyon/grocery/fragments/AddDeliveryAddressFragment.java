package codecanyon.grocery.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import codecanyon.grocery.R;
import codecanyon.grocery.models.AddDeliveryRequest;
import codecanyon.grocery.models.DeliveryResponse;
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
 * Created by Rajesh Dabhi on 6/7/2017.
 */

public class AddDeliveryAddressFragment extends Fragment implements View.OnClickListener {

    private static String TAG = AddDeliveryAddressFragment.class.getSimpleName();

    private EditText et_phone, et_name, et_pin, et_house;
    private Button btn_update;
    private TextView tv_phone, tv_name, tv_pin, tv_house, tv_socity, btn_socity;
    private RetrofitService service;

    private SessionManagement sessionManagement;

    private boolean isEdit = false;

    private String location_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_delivery_address, container, false);

        sessionManagement = new SessionManagement(getActivity());
        service = RetrofitInstance.createService(RetrofitService.class);

        et_phone = view.findViewById(R.id.et_add_adres_phone);
        et_name = view.findViewById(R.id.et_add_adres_name);
        tv_phone = view.findViewById(R.id.tv_add_adres_phone);
        tv_name = view.findViewById(R.id.tv_add_adres_name);
        tv_pin = view.findViewById(R.id.tv_add_adres_pin);
        et_pin = view.findViewById(R.id.et_add_adres_pin);
        et_house = view.findViewById(R.id.et_add_adres_home);
        tv_house = view.findViewById(R.id.tv_add_adres_home);
        tv_socity = view.findViewById(R.id.tv_add_adres_socity);
        btn_update = view.findViewById(R.id.btn_add_adres_edit);
        btn_socity = view.findViewById(R.id.btn_add_adres_socity);

        String getsocity_name = sessionManagement.getUserDetails().get(APIUrls.KEY_SOCITY_NAME);
        String getsocity_id = sessionManagement.getUserDetails().get(APIUrls.KEY_SOCITY_ID);

        Bundle args = getArguments();

        if (args != null) {
            location_id = getArguments().getString("location_id");
            String get_name = getArguments().getString("name");
            String get_phone = getArguments().getString("mobile");
            String get_pine = getArguments().getString("pincode");
            String get_socity_id = getArguments().getString("socity_id");
            String get_socity_name = getArguments().getString("socity_name");
            String get_house = getArguments().getString("house");

            if (TextUtils.isEmpty(get_name) && get_name == null) {
                isEdit = false;
            } else {
                isEdit = true;

                Toast.makeText(getActivity(), "edit", Toast.LENGTH_SHORT).show();

                et_name.setText(get_name);
                et_phone.setText(get_phone);
                et_pin.setText(get_pine);
                et_house.setText(get_house);
                btn_socity.setText(get_socity_name);

                sessionManagement.updateSocity(get_socity_name, get_socity_id);
            }
        }

        if (!TextUtils.isEmpty(getsocity_name)) {

            btn_socity.setText(getsocity_name);
            sessionManagement.updateSocity(getsocity_name, getsocity_id);
        }

        btn_update.setOnClickListener(this);
        btn_socity.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_add_adres_edit) {
            attemptEditProfile();
        } else if (id == R.id.btn_add_adres_socity) {

            /*String getpincode = et_pin.getText().toString();

            if (!TextUtils.isEmpty(getpincode)) {*/

            Bundle args = new Bundle();
            Fragment fm = new SoCityFragment();
            //args.putString("pincode", getpincode);
            fm.setArguments(args);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout, fm)
                    .addToBackStack(null).commit();
            /*} else {
                Toast.makeText(getActivity(), getResources().getString(R.string.please_enter_pincode), Toast.LENGTH_SHORT).show();
            }*/

        }
    }

    private void attemptEditProfile() {

        tv_phone.setText(getResources().getString(R.string.receiver_mobile_number));
        tv_pin.setText(getResources().getString(R.string.tv_reg_pincode));
        tv_name.setText(getResources().getString(R.string.receiver_name_req));
        tv_house.setText(getResources().getString(R.string.tv_reg_house));
        tv_socity.setText(getResources().getString(R.string.tv_reg_socity));

        tv_name.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_phone.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_pin.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_house.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_socity.setTextColor(getResources().getColor(R.color.dark_gray));

        String getphone = et_phone.getText().toString();
        String getname = et_name.getText().toString();
        String getpin = et_pin.getText().toString();
        String gethouse = et_house.getText().toString();
        String getsocity = sessionManagement.getUserDetails().get(APIUrls.KEY_SOCITY_ID);

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(getphone)) {
            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        } else if (!isPhoneValid(getphone)) {
            tv_phone.setText(getResources().getString(R.string.invalid_phone_number));
            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        }

        if (TextUtils.isEmpty(getname)) {
            tv_name.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_name;
            cancel = true;
        }

        if (TextUtils.isEmpty(getpin)) {
            tv_pin.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_pin;
            cancel = true;
        }

        if (TextUtils.isEmpty(gethouse)) {
            tv_house.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_house;
            cancel = true;
        }

        if (TextUtils.isEmpty(getsocity) && getsocity == null) {
            tv_socity.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = btn_socity;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (focusView != null)
                focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (ConnectivityReceiver.isConnected()) {

                String user_id = sessionManagement.getUserDetails().get(APIUrls.KEY_ID);

                // check internet connection
                if (ConnectivityReceiver.isConnected()) {
                    if (isEdit) {
                        makeEditAddressRequest(location_id, getpin, getsocity, gethouse, getname, getphone);
                    } else {
                        makeAddAddressRequest(user_id, getpin, getsocity, gethouse, getname, getphone);
                    }
                }
            }
        }
    }

    private boolean isPhoneValid(String phoneno) {
        //TODO: Replace this with your own logic
        return phoneno.length() > 9;
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeAddAddressRequest(String user_id, String pincode, String socity_id,
                                       String house_no, String receiver_name, String receiver_mobile) {

        service.addDeliveryList(user_id, pincode, socity_id, house_no, receiver_name, receiver_mobile, location_id)
                .enqueue(new Callback<DeliveryResponse>() {
                    @Override
                    public void onResponse(Call<DeliveryResponse> call, Response<DeliveryResponse> response) {
                        if (response != null && response.body() != null && response.body().isResponce()) {
                            getActivity().onBackPressed();
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DeliveryResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeEditAddressRequest(String location_id, String pincode, String socity_id,
                                        String house_no, String receiver_name, String receiver_mobile) {

        AddDeliveryRequest adr = new AddDeliveryRequest();
        adr.setUser_id(location_id);
        adr.setPincode(pincode);
        adr.setSocity_id(socity_id);
        adr.setHouse_no(house_no);
        adr.setReceiver_name(receiver_name);
        adr.setReceiver_mobile(receiver_mobile);

        service.editDeliveryList(adr).enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                if (response != null && response.body() != null && response.body().isResponce()) {
                    Toast.makeText(getActivity(), response.body().getData(), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<RequestResponse> call, Throwable t) {
                Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
