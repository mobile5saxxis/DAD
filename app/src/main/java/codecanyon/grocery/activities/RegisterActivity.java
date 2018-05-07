package codecanyon.grocery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import codecanyon.grocery.R;
import codecanyon.grocery.models.RequestResponse;
import codecanyon.grocery.reterofit.RetrofitInstance;
import codecanyon.grocery.reterofit.RetrofitService;
import codecanyon.grocery.util.ConnectivityReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static String TAG = RegisterActivity.class.getSimpleName();

    private EditText et_phone, et_name, et_password, et_email;
    private Button btn_register;
    private TextView tv_phone, tv_name, tv_password, tv_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove tv_title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register);

        et_phone = findViewById(R.id.et_reg_phone);
        et_name = findViewById(R.id.et_reg_name);
        et_password = findViewById(R.id.et_reg_password);
        et_email = findViewById(R.id.et_reg_email);
        tv_password = findViewById(R.id.tv_reg_password);
        tv_phone = findViewById(R.id.tv_reg_phone);
        tv_name = findViewById(R.id.tv_reg_name);
        tv_email = findViewById(R.id.tv_reg_email);
        btn_register = findViewById(R.id.btnRegister);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
    }

    private void attemptRegister() {

        tv_phone.setText(getResources().getString(R.string.et_login_phone_hint));
        tv_email.setText(getResources().getString(R.string.tv_login_email));
        tv_name.setText(getResources().getString(R.string.tv_reg_name_hint));
        tv_password.setText(getResources().getString(R.string.tv_login_password));

        tv_name.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_phone.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_password.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_email.setTextColor(getResources().getColor(R.color.dark_gray));

        String getphone = et_phone.getText().toString().trim();
        String getname = et_name.getText().toString().trim();
        String getpassword = et_password.getText().toString().trim();
        String getemail = et_email.getText().toString().trim();

        if (getname.isEmpty()) {
            tv_name.setTextColor(getResources().getColor(R.color.colorPrimary));
            Toast.makeText(this, R.string.name_is_empty, Toast.LENGTH_SHORT).show();
        } else if (getemail.isEmpty()) {
            tv_email.setTextColor(getResources().getColor(R.color.colorPrimary));
            Toast.makeText(this, R.string.email_empty, Toast.LENGTH_SHORT).show();
        } else if (!isEmailValid(getemail)) {
            tv_email.setTextColor(getResources().getColor(R.color.colorPrimary));
            Toast.makeText(this, R.string.enter_valid_email_address, Toast.LENGTH_SHORT).show();
        } else if (getphone.isEmpty()) {
            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            Toast.makeText(this, R.string.phone_number_is_empty, Toast.LENGTH_SHORT).show();
        } else if (!isPhoneValid(getphone)) {
            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            Toast.makeText(this, R.string.enter_valid_phone_number, Toast.LENGTH_SHORT).show();
        } else if (getpassword.isEmpty()) {
            tv_password.setTextColor(getResources().getColor(R.color.colorPrimary));
            Toast.makeText(this, R.string.password_empty, Toast.LENGTH_SHORT).show();
        } else if (!isPasswordValid(getpassword)) {
            tv_password.setTextColor(getResources().getColor(R.color.colorPrimary));
            Toast.makeText(this, R.string.password_too_short, Toast.LENGTH_SHORT).show();
        } else {
            if (ConnectivityReceiver.isConnected()) {
                makeRegisterRequest(getname, getphone, getemail, getpassword);
            }
        }


    }

    private boolean isEmailValid(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isPhoneValid(String phoneno) {
        return (!TextUtils.isEmpty(phoneno) && Patterns.PHONE.matcher(phoneno).matches() && phoneno.length() == 10);
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeRegisterRequest(String name, String mobile, String email, String password) {
        RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
        service.register(name, email, mobile, password).enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    RequestResponse requestResponse = response.body();

                    if (requestResponse.isResponce()) {
                        Toast.makeText(RegisterActivity.this, requestResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        String error = requestResponse.getError();

                        if (error.toLowerCase().contains("email") && error.toLowerCase().contains("unique")) {
                            error = "Email is already registered";
                        }

                        Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<RequestResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, R.string.connection_time_out, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
