package codecanyon.grocery.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import codecanyon.grocery.activities.ProductDetailsActivity;
import codecanyon.grocery.adapter.OfferListAdapter;
import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.R;
import codecanyon.grocery.models.OffersResponse;
import codecanyon.grocery.models.Product;
import codecanyon.grocery.reterofit.RetrofitInstance;
import codecanyon.grocery.reterofit.RetrofitService;
import codecanyon.grocery.util.ConnectivityReceiver;
import codecanyon.grocery.util.RecyclerTouchListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by srikarn on 26-03-2018.
 */

public class OffersFragment extends Fragment {

    private static String TAG = OffersFragment.class.getSimpleName();
    private RecyclerView rv_items;
    private List<Product> offers_List = new ArrayList<>();
    private OfferListAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offers, container, false);
        setHasOptionsMenu(true);

        rv_items = view.findViewById(R.id.offers_recyclerview);
        rv_items.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        //setHasOptionsMenu(true);
        //return view;

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.offer_name));
        ((MainActivity) getActivity()).updateHeader();

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetOfferRequest();
        }
        rv_items.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_items, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Product product = offers_List.get(position);
                String value = new Gson().toJson(product);
                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                intent.putExtra(ProductDetailsActivity.PRODUCT, value);
                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        return view;
    }


    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeGetOfferRequest() {
        RetrofitService service = RetrofitInstance.createService(RetrofitService.class);

        service.getGetOffers().enqueue(new Callback<OffersResponse>() {
            @Override
            public void onResponse(Call<OffersResponse> call, Response<OffersResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    OffersResponse cr = response.body();

                    offers_List = cr.getData();

                    mAdapter = new OfferListAdapter(offers_List);
                    rv_items.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<OffersResponse> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();

            }
        });
    }

}
