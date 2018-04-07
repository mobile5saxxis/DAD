package codecanyon.grocery.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import codecanyon.grocery.DADApp;
import codecanyon.grocery.R;
import codecanyon.grocery.models.ForgotPasswordRequest;
import codecanyon.grocery.models.Login;
import codecanyon.grocery.models.LoginRequest;
import codecanyon.grocery.models.LoginResponse;
import codecanyon.grocery.reterofit.RetrofitInstance;
import codecanyon.grocery.reterofit.RetrofitService;
import codecanyon.grocery.util.ConnectivityReceiver;
import codecanyon.grocery.util.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = ForgotActivity.class.getSimpleName();

    private Button btn_continue;
    private EditText et_email;
    private TextView tv_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove tv_title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_forgot);

        et_email = findViewById(R.id.et_login_email);
        tv_email = findViewById(R.id.tv_login_email);
        btn_continue = findViewById(R.id.btnContinue);

        btn_continue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnContinue) {
            attemptForgot();
        }
    }

    private void attemptForgot() {

        tv_email.setText(getResources().getString(R.string.tv_login_email));

        tv_email.setTextColor(getResources().getColor(R.color.dark_gray));

        String getemail = et_email.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(getemail)) {
            tv_email.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_email;
            cancel = true;
        } else if (!isEmailValid(getemail)) {
            tv_email.setText(getResources().getString(R.string.invalide_email_address));
            tv_email.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_email;
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
                makeForgotRequest(getemail);
            }
        }

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeForgotRequest(String email) {
        ForgotPasswordRequest lr = new ForgotPasswordRequest();
        lr.setEmail(email);

        RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
        service.forgotPassword(lr).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse lr = response.body();

                    if (lr.isResponce()) {
                        Toast.makeText(ForgotActivity.this, lr.getError(), Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(ForgotActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();

                    } else {
                        Toast.makeText(ForgotActivity.this, lr.getError(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(ForgotActivity.this, R.string.connection_time_out, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
