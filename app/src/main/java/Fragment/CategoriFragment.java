package Fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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
import Model.Product_model;
import codecanyon.grocery.AppController;
import codecanyon.grocery.MainActivity;
import codecanyon.grocery.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class CategoriFragment extends Fragment {

    private static String TAG = CategoriFragment.class.getSimpleName();

    private RecyclerView rv_cat;
    private TabLayout tab_cat;

    private List<Category_model> category_modelList = new ArrayList<>();
    private List<String> cat_menu_id = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categori, container, false);

        tab_cat = (TabLayout) view.findViewById(R.id.tab_cat2);
        rv_cat = (RecyclerView) view.findViewById(R.id.rv_subcategory2);
        rv_cat.setLayoutManager(new LinearLayoutManager(getActivity()));
        //setHasOptionsMenu(true);
        //return view;
        String getcat_id = getArguments().getString("cat_id");
        String getcat_title = getArguments().getString("cat_title");

        ((MainActivity) getActivity()).setTitle(getcat_title);

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetCategoryRequest(getcat_id);
        }

        tab_cat.setVisibility(View.GONE);
        tab_cat.setSelectedTabIndicatorColor(getActivity().getResources().getColor(R.color.white));

        tab_cat.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String getcat_id = cat_menu_id.get(tab.getPosition());

                if (ConnectivityReceiver.isConnected()) {
                    makeGetCategoryRequest(getcat_id);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                /*String getcat_id = cat_menu_id.get(tab.getPosition());
                tab_cat.setSelectedTabIndicatorColor(getActivity().getResources().getColor(R.color.white));

                if (ConnectivityReceiver.isConnected()) {
                    makeGetProductRequest(getcat_id);
                }*/
            }
        });

        return view;
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeGetCategoryRequest(final String parent_id) {

        // Tag used to cancel the request
        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", parent_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_CATEGORY_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Category_model>>() {
                        }.getType();

                        category_modelList = gson.fromJson(response.getString("data"), listType);

                        if (!category_modelList.isEmpty()) {
                            tab_cat.setVisibility(View.VISIBLE);

                            cat_menu_id.clear();
                            for (int i = 0; i < category_modelList.size(); i++) {
                                cat_menu_id.add(category_modelList.get(i).getId());
                                tab_cat.addTab(tab_cat.newTab().setText(category_modelList.get(i).getTitle()));
                            }
                        } else {
                            makeGetCategoryRequest(parent_id);
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


