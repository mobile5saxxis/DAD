package codecanyon.grocery.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import codecanyon.grocery.R;
import codecanyon.grocery.adapter.ProductAdapter;
import codecanyon.grocery.models.AdResponse;
import codecanyon.grocery.models.OffersResponse;
import codecanyon.grocery.reterofit.RetrofitInstance;
import codecanyon.grocery.reterofit.RetrofitService;
import codecanyon.grocery.util.ConnectivityReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdFragment extends Fragment {

    public static final String AD_ID = "AD_ID";
    private RetrofitService service;
    private RelativeLayout rl_progress;
    private String id;
    private TextView tv_title, tv_description;

    public static AdFragment newInstance(String id) {
        AdFragment adFragment = new AdFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AD_ID, id);
        adFragment.setArguments(bundle);
        return adFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            id = getArguments().getString(AD_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_ad, container, false);

        service = RetrofitInstance.createService(RetrofitService.class);
        rl_progress = view.findViewById(R.id.rl_progress);
        tv_title = view.findViewById(R.id.tv_title);
        tv_description = view.findViewById(R.id.tv_description);

        if (ConnectivityReceiver.isConnected()) {
            makeAdRequest();
        }

        return view;
    }

    private void makeAdRequest() {

        service.getAdResponse(id).enqueue(new Callback<AdResponse>() {
            @Override
            public void onResponse(Call<AdResponse> call, Response<AdResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    AdResponse adResponse = response.body();

                    if (adResponse != null) {
                        tv_title.setText(adResponse.getPg_title());
                        tv_description.setText(Html.fromHtml(adResponse.getPg_descri()));
                    }

                    rl_progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AdResponse> call, Throwable t) {

            }
        });
    }

}