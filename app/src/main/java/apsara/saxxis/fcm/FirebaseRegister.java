package apsara.saxxis.fcm;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import apsara.saxxis.models.CheckLoginRequest;
import apsara.saxxis.models.RequestResponse;
import apsara.saxxis.reterofit.APIUrls;
import apsara.saxxis.reterofit.RetrofitInstance;
import apsara.saxxis.reterofit.RetrofitService;
import apsara.saxxis.util.ConnectivityReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by subhashsanghani on 12/21/16.
 */

public class FirebaseRegister {

    Activity _context;
    public SharedPreferences settings;
    ConnectivityReceiver cd;

    public FirebaseRegister(Activity context) {
        this._context = context;
        settings = context.getSharedPreferences(APIUrls.PREFS_NAME, 0);
        cd = new ConnectivityReceiver();

    }

    public void RegisterUser(String user_id) {
        // [START subscribe_topics]
        String token = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("grocery");
        // [END subscribe_topics]
        //  mLogTask = new fcmRegisterTask();
        //   mLogTask.execute(user_id,token);
        checkLogin(user_id, token);
    }


    private void checkLogin(String user_id, String token) {
        RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
        service.registerFCM(user_id, token, "android").enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RequestResponse rr = response.body();

                    if (!rr.isResponce()) {
                        Toast.makeText(_context, rr.getError(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RequestResponse> call, Throwable t) {

            }
        });
    }

    public void addNotificationToken() {
        String token = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("grocery");

        RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
        service.addToken(token).enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RequestResponse rr = response.body();

                    if (!rr.isResponce()) {
                        Toast.makeText(_context, rr.getError(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RequestResponse> call, Throwable t) {

            }
        });
    }


}
