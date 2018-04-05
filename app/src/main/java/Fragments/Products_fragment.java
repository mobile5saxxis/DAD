/*
package Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.Product_adapter;
import Config.BaseURL;
import Model.Category_model;
import Model.PopularBrands_Model;
import Model.Product_model;
import codecanyon.grocery.AppController;
import codecanyon.grocery.MainActivity;
import codecanyon.grocery.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;

*/
/**
 * Created by srikarn on 02-04-2018.
 *//*


public class Products_fragment extends Fragment {

    private static String TAG = Product_fragment.class.getSimpleName();

    private RecyclerView rv_cat;
    private TabLayout tab_cat;
    private List<PopularBrands_Model> brands_modelList = new ArrayList<>();
    private List<Product_model> brandlist = new ArrayList<>();

    private List<Product_model> product_modelList = new ArrayList<>();
    private Product_adapter adapter_product;

    public Products_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter_product != null) {
            adapter_product.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ActionBar actionBar = ((MainActivity) context).getSupportActionBar();
        View actionView = actionBar.getCustomView();
        //LinearLayout searchParent = (LinearLayout) actionView.findViewById(R.id.search_parentll);
        //searchParent.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        */
/*Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);*//*


        tab_cat = (TabLayout) view.findViewById(R.id.tab_cat);
        rv_cat = (RecyclerView) view.findViewById(R.id.rv_subcategory);
        rv_cat.setLayoutManager(new LinearLayoutManager(getActivity()));
        String getcat_id = getArguments().getString("cat_id");
        String getcat_title = getArguments().getString("cat_title");
        Boolean isCatogery = getArguments().getBoolean("isCategory");

        ((MainActivity) getActivity()).setTitle(getcat_title);

        if (ConnectivityReceiver.isConnected()) {
            makeGetProductRequest(getcat_id, isCatogery);
        }

        return view;
    }



    private void makeGetProductRequest(final String cat_id, final Boolean isCatogery) {

        // Tag used to cancel the request
        String tag_json_obj = "json_product_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("cat_id", cat_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_PRODUCT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();

                        */
/*JSONObject data = response.getJSONObject("data");*//*

                        product_modelList = gson.fromJson(response.getString("data"), listType);

                        if(!isCatogery){
                            for(int i =0 ; i< product_modelList.size(); i++){
                                if(product_modelList.get(i).getBrand() == cat_id){
                                    brandlist.add(product_modelList.get(i));
                                }
                            }
                        }


                        adapter_product = new Product_adapter(brandlist, getActivity());
                        rv_cat.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();

                        if (getActivity() != null) {
                            if (product_modelList.isEmpty()) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                        }

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

*/
