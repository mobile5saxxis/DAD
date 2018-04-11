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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = LoginActivity.class.getSimpleName();

    private Button btn_continue, btn_register;
    private EditText et_password, et_email;
    private TextView tv_password, tv_email, btn_forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove tv_title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        et_password = findViewById(R.id.et_login_pass);
        et_email = findViewById(R.id.et_login_email);
        tv_password = findViewById(R.id.tv_login_password);
        tv_email = findViewById(R.id.tv_login_email);
        btn_continue = findViewById(R.id.btnContinue);
        btn_register = findViewById(R.id.btnRegister);
        btn_forgot = findViewById(R.id.btnForgot);

        btn_continue.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_forgot.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnContinue) {
            attemptLogin();
        } else if (id == R.id.btnRegister) {
            Intent startRegister = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(startRegister);
        } else if (id == R.id.btnForgot) {
            Intent startRegister = new Intent(LoginActivity.this, ForgotActivity.class);
            startActivity(startRegister);
        }
    }

    private void attemptLogin() {

        tv_email.setText(getResources().getString(R.string.tv_login_email));
        tv_password.setText(getResources().getString(R.string.tv_login_password));

        tv_password.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_email.setTextColor(getResources().getColor(R.color.dark_gray));

        String getpassword = et_password.getText().toString();
        String getemail = et_email.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(getpassword)) {
            tv_password.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_password;
            cancel = true;
        } else if (!isPasswordValid(getpassword)) {
            tv_password.setText(getResources().getString(R.string.password_too_short));
            tv_password.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_password;
            cancel = true;
        }

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
                makeLoginRequest(getemail, getpassword);
            }
        }

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeLoginRequest(String email, final String password) {
        LoginRequest lr = new LoginRequest();
        lr.setUser_email(email);
        lr.setPassword(password);

        RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
        service.login(email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse lr = response.body();

                    if (lr.isResponce()) {
                        Login login = lr.getData();
                        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
                        sessionManagement.createLoginSession(login.getUser_id(), login.getUser_email(), login.getUser_fullname(), login.getUser_phone(), login.getUser_image(), "", "", "", "", password);

                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, lr.getError(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
