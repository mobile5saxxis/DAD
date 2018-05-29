package mydad.saxxis.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mydad.saxxis.DADApp;
import mydad.saxxis.activities.MainActivity;
import mydad.saxxis.R;
import mydad.saxxis.models.ChangePasswordRequest;
import mydad.saxxis.models.RequestResponse;
import mydad.saxxis.reterofit.APIUrls;
import mydad.saxxis.reterofit.RetrofitInstance;
import mydad.saxxis.reterofit.RetrofitService;
import mydad.saxxis.util.ConnectivityReceiver;
import mydad.saxxis.util.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rajesh Dabhi on 13/7/2017.
 */

public class ChangePasswordFragment extends Fragment {

    private static String TAG = ChangePasswordFragment.class.getSimpleName();

    private TextView tv_new_pass, tv_old_pass, tv_con_pass;
    private EditText et_new_pass, et_old_pass, et_con_pass;
    private Button btn_change_pass;

    private SessionManagement sessionManagement;

    public ChangePasswordFragment() {
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
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        setHasOptionsMenu(true);

        sessionManagement = new SessionManagement(getActivity());

        tv_new_pass = view.findViewById(R.id.tv_change_new_password);
        tv_old_pass = view.findViewById(R.id.tv_change_old_password);
        tv_con_pass = view.findViewById(R.id.tv_change_con_password);
        et_new_pass = view.findViewById(R.id.et_change_new_password);
        et_old_pass = view.findViewById(R.id.et_change_old_password);
        et_con_pass = view.findViewById(R.id.et_change_con_password);
        btn_change_pass = view.findViewById(R.id.btn_change_password);

        btn_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptChangePassword();
            }
        });

        return view;
    }

    private void attemptChangePassword() {

        tv_new_pass.setText(getResources().getString(R.string.new_password));
        tv_old_pass.setText(getResources().getString(R.string.old_password));
        tv_con_pass.setText(getResources().getString(R.string.confirm_password));

        tv_new_pass.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_old_pass.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_con_pass.setTextColor(getResources().getColor(R.color.dark_gray));

        String get_new_pass = et_new_pass.getText().toString();
        String get_old_pass = et_old_pass.getText().toString();
        String get_con_pass = et_con_pass.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(get_new_pass)) {
            tv_new_pass.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = tv_new_pass;
            cancel = true;
        } else if (!isPasswordValid(get_new_pass)) {
            tv_new_pass.setText(getString(R.string.password_too_short));
            tv_new_pass.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = tv_new_pass;
            cancel = true;
        }

        if (TextUtils.isEmpty(get_old_pass)) {
            tv_old_pass.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = tv_old_pass;
            cancel = true;
        } else if (!isPasswordValid(get_old_pass)) {
            tv_old_pass.setText(getString(R.string.password_too_short));
            tv_old_pass.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = tv_old_pass;
            cancel = true;
        }

        if (TextUtils.isEmpty(get_con_pass)) {
            tv_con_pass.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = tv_con_pass;
            cancel = true;
        } else if (!get_con_pass.equals(get_new_pass)) {
            tv_con_pass.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv_con_pass.setText(getResources().getString(R.string.password_not_same));
            focusView = tv_con_pass;
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
                    makeChangePasswordRequest(user_id, get_new_pass, get_old_pass);
                }
            }
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeChangePasswordRequest(String user_id, String new_password,
                                           String current_password) {

        ChangePasswordRequest cpr = new ChangePasswordRequest();
        cpr.setUser_id(user_id);
        cpr.setNew_password(new_password);
        cpr.setCurrent_password(current_password);


        RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
        service.changePassword(cpr).enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RequestResponse requestResponse = response.body();
                    if (requestResponse.isResponce()) {
                        Toast.makeText(getActivity(), "Password Change Sucessfully", Toast.LENGTH_SHORT).show();

                        sessionManagement.logoutSessionwithchangepassword();
                        ((MainActivity) getActivity()).setFinish();
                    } else {
                        Toast.makeText(getActivity(), "'Current password do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RequestResponse> call, Throwable t) {
                Toast.makeText(getActivity(),R.string.connection_time_out, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem cart = menu.findItem(R.id.action_cart);
        cart.setVisible(false);
        MenuItem change_pass = menu.findItem(R.id.action_change_password);
        change_pass.setVisible(false);
        MenuItem search = menu.findItem(R.id.action_search);
        search.setVisible(false);

    }

}
