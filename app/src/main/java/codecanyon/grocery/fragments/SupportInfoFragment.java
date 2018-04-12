package codecanyon.grocery.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import codecanyon.grocery.DADApp;
import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.R;
import codecanyon.grocery.models.SupportInfo;
import codecanyon.grocery.models.SupportInfoResponse;
import codecanyon.grocery.reterofit.RetrofitInstance;
import codecanyon.grocery.reterofit.RetrofitService;
import codecanyon.grocery.util.ConnectivityReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class SupportInfoFragment extends Fragment {

    private static String TAG = SupportInfoFragment.class.getSimpleName();

    private TextView tv_info;

    public SupportInfoFragment() {
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
        View view = inflater.inflate(R.layout.fragment_support_info, container, false);

        tv_info = view.findViewById(R.id.tv_info);

        String geturl = getArguments().getString("url");
        String title = getArguments().getString("tv_subcat_title");

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetInfoRequest(geturl);
        } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        return view;
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeGetInfoRequest(String url) {

        RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
        service.getSupportInfo(url).enqueue(new Callback<SupportInfoResponse>() {
            @Override
            public void onResponse(Call<SupportInfoResponse> call, Response<SupportInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SupportInfoResponse supportInfoResponse = response.body();

                    if (supportInfoResponse.isResponce()) {
                        List<SupportInfo> support_infoList = supportInfoResponse.getData();

                        String desc = support_infoList.get(0).getPg_descri();
                        String title = support_infoList.get(0).getPg_title();

                        tv_info.setText(Html.fromHtml(desc));
                    }
                }
            }

            @Override
            public void onFailure(Call<SupportInfoResponse> call, Throwable t) {

            }
        });
    }

}
