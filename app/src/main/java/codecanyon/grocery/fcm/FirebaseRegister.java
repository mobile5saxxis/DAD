package codecanyon.grocery.fcm;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import codecanyon.grocery.models.CheckLoginRequest;
import codecanyon.grocery.models.RequestResponse;
import codecanyon.grocery.reterofit.APIUrls;
import codecanyon.grocery.reterofit.RetrofitInstance;
import codecanyon.grocery.reterofit.RetrofitService;
import codecanyon.grocery.util.ConnectivityReceiver;
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
        CheckLoginRequest clr = new CheckLoginRequest();
        clr.setUser_id(user_id);
        clr.setToken(token);

        RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
        service.registerFCM(clr).enqueue(new Callback<RequestResponse>() {
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
