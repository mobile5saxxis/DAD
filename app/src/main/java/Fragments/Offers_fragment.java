package Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.OfferFragment_adapter;
import Adapter.Offer_adapter;
import Config.BaseURL;
import Model.Brands_list_model;
import Model.Offers_model;
import codecanyon.grocery.AppController;
import codecanyon.grocery.MainActivity;
import codecanyon.grocery.OffersDetailsActivity;
import codecanyon.grocery.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.RecyclerTouchListener;

/**
 * Created by srikarn on 26-03-2018.
 */

public class Offers_fragment extends Fragment {

    private static String TAG = Offers_fragment.class.getSimpleName();

    private RecyclerView rv_items;
    private boolean isSubcat = false;

    private List<Offers_model> offers_modelList = new ArrayList<>();
    private OfferFragment_adapter mAdapter;


    public Offers_fragment(){
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
        View view = inflater.inflate(R.layout.fragment_offers, container, false);
        setHasOptionsMenu(true);

        rv_items= (RecyclerView) view.findViewById(R.id.offers_recyclerview);
        rv_items.setLayoutManager(new GridLayoutManager(getActivity(),3));
        //setHasOptionsMenu(true);
        //return view;

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.offer_name));
        ((MainActivity) getActivity()).updateHeader();

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetOfferRequest("");
        }
        rv_items.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_items, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String getid = offers_modelList.get(position).getProduct_id();
                String getcat_title = offers_modelList.get(position).getProduct_name();

                Intent intent = new Intent(getActivity(), OffersDetailsActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("selectedProduct", offers_modelList.get(position));
                getActivity().startActivity(intent);

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
    private void makeGetOfferRequest(String parent_id) {

        // Tag used to cancel the request
        String tag_json_obj = "json_offer_req";

        isSubcat = false;

        Map<String, String> params = new HashMap<String, String>();
        if (parent_id != null && parent_id != "") {
            params.put("parent", parent_id);
            isSubcat = true;
        }

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_OFFERS_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Offers_model>>() {
                        }.getType();

                        offers_modelList = gson.fromJson(response.getString("data"), listType);

                        mAdapter = new OfferFragment_adapter(offers_modelList);
                        rv_items.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
