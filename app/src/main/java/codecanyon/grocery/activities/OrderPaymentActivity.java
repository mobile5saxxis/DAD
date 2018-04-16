package codecanyon.grocery.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ebs.android.sdk.Config;
import com.ebs.android.sdk.EBSPayment;
import com.ebs.android.sdk.PaymentRequest;

import java.util.ArrayList;
import java.util.HashMap;

import codecanyon.grocery.R;

public class OrderPaymentActivity extends AppCompatActivity {

    public static final String PAYMENT_AMOUNT = "PAYMENT_AMOUNT";
    public static final String ORDER_ID = "ORDER_ID";
    private final int ACC_ID = 27920;
    private final String SECRET_KEY = "db07fb92e05f6657b3e80e286fdba4a5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String amount = getIntent().getStringExtra(PAYMENT_AMOUNT);
        String orderId = getIntent().getStringExtra(ORDER_ID);

        PaymentRequest.getInstance().setReferenceNo(orderId);
        PaymentRequest.getInstance().setTransactionAmount(amount);

        /** Mandatory */

        PaymentRequest.getInstance().setAccountId(ACC_ID);
        PaymentRequest.getInstance().setSecureKey(SECRET_KEY);

        // Reference No
        PaymentRequest.getInstance().setReferenceNo(orderId);
        /** Mandatory */

        // Email Id
        PaymentRequest.getInstance().setBillingEmail("test_tag@testmail.com");
        /** Mandatory */

        /**
         * Set failure id as 1 to display amount and reference number on failed
         * transaction page. set 0 to disable
         */
        PaymentRequest.getInstance().setFailureid("0");
        /** Mandatory */

        // Currency
        PaymentRequest.getInstance().setCurrency("INR");
        /** Mandatory */


        // Your Reference No or Order Id for this transaction
        PaymentRequest.getInstance().setTransactionDescription(
                orderId);

        /** Billing Details */
        PaymentRequest.getInstance().setBillingName("Test_Name");
        /** Optional */
        PaymentRequest.getInstance().setBillingAddress("North Mada Street");
        /** Optional */
        PaymentRequest.getInstance().setBillingCity("Chennai");
        /** Optional */
        PaymentRequest.getInstance().setBillingPostalCode("600019");
        /** Optional */
        PaymentRequest.getInstance().setBillingState("Tamilnadu");
        /** Optional */
        PaymentRequest.getInstance().setBillingCountry("IND");
        // ** Optional */
        PaymentRequest.getInstance().setBillingPhone("01234567890");
        /** Optional */
        /** set custom message for failed transaction */

        PaymentRequest.getInstance().setFailuremessage(
                getResources().getString(R.string.payment_failure_message));
        /** Optional */
        /** Shipping Details */
        PaymentRequest.getInstance().setShippingName("Test_Name");
        /** Optional */
        PaymentRequest.getInstance().setShippingAddress("North Mada Street");
        /** Optional */
        PaymentRequest.getInstance().setShippingCity("Chennai");
        /** Optional */
        PaymentRequest.getInstance().setShippingPostalCode("600019");
        /** Optional */
        PaymentRequest.getInstance().setShippingState("Tamilnadu");
        /** Optional */
        PaymentRequest.getInstance().setShippingCountry("IND");
        /** Optional */
        PaymentRequest.getInstance().setShippingEmail("test@testmail.com");
        /** Optional */
        PaymentRequest.getInstance().setShippingPhone("01234567890");
        /** Optional */
        /* enable log by setting 1 and disable by setting 0 */
        PaymentRequest.getInstance().setLogEnabled("1");

        /**
         * Initialise parameters for dyanmic values sending from merchant custom
         * values from merchant
         */

        ArrayList<HashMap<String, String>> custom_post_parameters = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> hashpostvalues = new HashMap<String, String>();
        hashpostvalues.put("account_details", "saving");
        hashpostvalues.put("merchant_type", "gold");
        custom_post_parameters.add(hashpostvalues);

        PaymentRequest.getInstance()
                .setCustomPostValues(custom_post_parameters);
        EBSPayment.getInstance().init(this, ACC_ID, SECRET_KEY, Config.Mode.ENV_TEST,
                Config.Encryption.ALGORITHM_MD5
                , getString(R.string.host));
    }
}
