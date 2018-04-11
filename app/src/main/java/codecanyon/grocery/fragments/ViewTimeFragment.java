package codecanyon.grocery.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import codecanyon.grocery.R;
import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.adapter.CategoryHomeAdapter;
import codecanyon.grocery.adapter.ViewTimeAdapter;
import codecanyon.grocery.models.Category;
import codecanyon.grocery.models.TimeRequest;
import codecanyon.grocery.models.TimeResponse;
import codecanyon.grocery.reterofit.RetrofitInstance;
import codecanyon.grocery.reterofit.RetrofitService;
import codecanyon.grocery.util.ConnectivityReceiver;
import codecanyon.grocery.util.RecyclerTouchListener;
import codecanyon.grocery.util.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rajesh Dabhi on 3/7/2017.
 */

public class ViewTimeFragment extends Fragment {

    private static String TAG = ViewTimeFragment.class.getSimpleName();

    private List<String> time_list = new ArrayList<>();
    private String getdate;
    private SessionManagement sessionManagement;
    private ViewTimeAdapter adapter;

    public ViewTimeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_time_list, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.delivery_time));

        sessionManagement = new SessionManagement(getActivity());

        RecyclerView rv_time = view.findViewById(R.id.rv_times);
        rv_time.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ViewTimeAdapter(time_list);

        rv_time.setAdapter(adapter);
        getdate = getArguments().getString("date");

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetTimeRequest(getdate);
        } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        rv_time.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_time, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String gettime = time_list.get(position);

                sessionManagement.cleardatetime();

                sessionManagement.creatdatetime(getdate, gettime);

                getActivity().onBackPressed();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return view;
    }

    /**
     * Method to make json object request where json response starts wtih {
     */
    private void makeGetTimeRequest(String date) {

        RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
        service.getTime(date).enqueue(new Callback<TimeResponse>() {
            @Override
            public void onResponse(Call<TimeResponse> call, Response<TimeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TimeResponse timeResponse = response.body();

                    if (timeResponse.getResponce()) {
                        time_list.addAll(timeResponse.getTimes());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<TimeResponse> call, Throwable t) {

            }
        });
    }

}
