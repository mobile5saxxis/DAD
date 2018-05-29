package mydad.saxxis;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ebs.android.sdk.PaymentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mydad.saxxis.R;
import mydad.saxxis.activities.MainActivity;
import mydad.saxxis.models.RequestResponse;
import mydad.saxxis.reterofit.RetrofitInstance;
import mydad.saxxis.reterofit.RetrofitService;
import mydad.saxxis.util.DatabaseHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mydad.saxxis.activities.ProductDetailsActivity.PRODUCT_DETAIL;

public class PaymentSuccessActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_status;
    private TextView tv_message;
    private AppCompatButton btn_payment_success;
    private AppCompatButton btn_retry;
    private AppCompatButton btn_cancel;
    private String payment_id;
    private LinearLayout ll_tryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.payment);
        }

        ll_tryAgain = findViewById(R.id.ll_tryAgain);
        tv_status = findViewById(R.id.tv_status);
        tv_message = findViewById(R.id.tv_message);
        btn_payment_success = findViewById(R.id.btn_payment_success);
        btn_retry = findViewById(R.id.btn_retry);
        btn_cancel = findViewById(R.id.btn_cancel);

        btn_payment_success.setOnClickListener(this);
        btn_retry.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        payment_id = getIntent().getStringExtra("payment_id");
        getJsonReport();
    }

    private void getJsonReport() {
        String response = payment_id;

        JSONObject jObject;
        try {
            jObject = new JSONObject(response);

            String paymentStatus = jObject.getString("PaymentStatus");

            if (paymentStatus.equalsIgnoreCase("failed")) {
                paymentFailed();
            } else {
                String paymentId = jObject.getString("PaymentId");
                String accountId = jObject.getString("AccountId");
                String merchantRefNo = jObject.getString("MerchantRefNo");
                String amount = jObject.getString("Amount");
                String orderId = jObject.getString("Description");
                String secureHash = jObject.getString("SecureHash");

                RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
                service.saveProductTransaction(paymentId, accountId, merchantRefNo, amount, secureHash, orderId, "1").enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if (response.body() != null && response.isSuccessful()) {

                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<RequestResponse> call, Throwable t) {

                    }
                });

                new DatabaseHandler().clearCart();
                tv_status.setText(R.string.success);
                tv_message.setText(R.string.purchase_has_been_success);
                btn_payment_success.setVisibility(View.VISIBLE);
                ll_tryAgain.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void paymentFailed() {
        tv_status.setText(R.string.failed);
        tv_message.setText(R.string.purchase_has_been_failed_please_try_again);
        ll_tryAgain.setVisibility(View.VISIBLE);
        btn_payment_success.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
            case R.id.btn_payment_success:
                onBackPressed();
                break;
            case R.id.btn_retry:
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                finish();
                startActivity(intent);
                break;
        }
    }
}
