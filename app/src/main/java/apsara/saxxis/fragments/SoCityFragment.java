package apsara.saxxis.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import apsara.saxxis.adapter.SocityAdapter;
import apsara.saxxis.activities.MainActivity;
import apsara.saxxis.R;
import apsara.saxxis.models.SoCity;
import apsara.saxxis.reterofit.RetrofitInstance;
import apsara.saxxis.reterofit.RetrofitService;
import apsara.saxxis.util.ConnectivityReceiver;
import apsara.saxxis.util.RecyclerTouchListener;
import apsara.saxxis.util.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class SoCityFragment extends Fragment {

    private static String TAG = SoCityFragment.class.getSimpleName();

    private EditText et_search;
    private RecyclerView rv_socity;

    private List<SoCity> soCityList = new ArrayList<>();
    private SocityAdapter adapter;

    public SoCityFragment() {
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
        View view = inflater.inflate(R.layout.fragment_socity, container, false);

        //String getpincode = getArguments().getString("pincode");

        et_search = view.findViewById(R.id.et_socity_search);
        rv_socity = view.findViewById(R.id.rv_socity);
        rv_socity.setLayoutManager(new LinearLayoutManager(getActivity()));

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetSocityRequest();
        } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        rv_socity.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_socity, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                SoCity soCity = adapter.getItem(position);
                String socity_id = soCity.getSocity_id();
                String socity_name = soCity.getSocity_name();

                SessionManagement sessionManagement = new SessionManagement(getActivity());

                sessionManagement.updateSocity(socity_name, socity_id);

                getActivity().onBackPressed();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return view;
    }

    /**
     * Method to make json array request where json response starts wtih
     */
    private void makeGetSocityRequest() {
        RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
        service.getSoCity().enqueue(new Callback<List<SoCity>>() {
            @Override
            public void onResponse(Call<List<SoCity>> call, Response<List<SoCity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    soCityList = response.body();

                    adapter = new SocityAdapter(soCityList);
                    rv_socity.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<SoCity>> call, Throwable t) {
                Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
            }
        });
    }

}