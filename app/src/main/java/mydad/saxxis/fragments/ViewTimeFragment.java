package mydad.saxxis.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mydad.saxxis.R;
import mydad.saxxis.activities.MainActivity;
import mydad.saxxis.adapter.CategoryHomeAdapter;
import mydad.saxxis.adapter.ViewTimeAdapter;
import mydad.saxxis.models.Category;
import mydad.saxxis.models.TimeRequest;
import mydad.saxxis.models.TimeResponse;
import mydad.saxxis.reterofit.RetrofitInstance;
import mydad.saxxis.reterofit.RetrofitService;
import mydad.saxxis.util.ConnectivityReceiver;
import mydad.saxxis.util.RecyclerTouchListener;
import mydad.saxxis.util.SessionManagement;
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

        try {
            if (getdate != null) {
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                dateFormat.format(date);

                if (dateFormat.parse(dateFormat.format(date)).after(dateFormat.parse(getdate + " 20:30"))) {
                    Toast.makeText(getContext(), "Select next day for delivery", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (ParseException ignore) {

        }

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
