package codecanyon.grocery.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import codecanyon.grocery.adapter.CategoryAdapter;

import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.R;
import codecanyon.grocery.models.Category;
import codecanyon.grocery.util.ConnectivityReceiver;
import codecanyon.grocery.util.RecyclerTouchListener;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class CategoryFragment extends Fragment {

    private static String TAG = CategoryFragment.class.getSimpleName();

    private RecyclerView rv_items;
    private boolean isSubcat = false;

    private List<Category> categoryList = new ArrayList<>();
    private CategoryAdapter mAdapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ActionBar actionBar=((MainActivity)context).getSupportActionBar();
        View actionView = actionBar.getCustomView();
        //LinearLayout searchParent = (LinearLayout) actionView.findViewById(R.id.search_parentll);
        //searchParent.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        setHasOptionsMenu(true);

        rv_items= view.findViewById(R.id.rv_subcategory2);
        rv_items.setLayoutManager(new GridLayoutManager(getActivity(),3));
        //setHasOptionsMenu(true);
        //return view;

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.category_name));
        ((MainActivity) getActivity()).updateHeader();

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetCategoryRequest("");
        }
        rv_items.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_items, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String getid = categoryList.get(position).getId();
                String getcat_title = categoryList.get(position).getTitle();

                Bundle args = new Bundle();
                Fragment fm = new ProductFragment();
                args.putString("cat_id", getid);
                args.putString("cat_title", getcat_title);
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_layout, fm)
                        .addToBackStack(null).commit();

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
    private void makeGetCategoryRequest(String parent_id) {

        // Tag used to cancel the request
        String tag_json_obj = "json_category_req";

        isSubcat = false;

        Map<String, String> params = new HashMap<String, String>();
        if (parent_id != null && parent_id != "") {
            params.put("parent", parent_id);
            isSubcat = true;
        }

//        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
//                APIUrls.GET_CATEGORY_URL, params, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());
//
//                try {
//                    Boolean status = response.getBoolean("responce");
//                    if (status) {
//
//                        Gson gson = new Gson();
//                        Type listType = new TypeToken<List<Model.Category>>() {
//                        }.getType();
//
//                        categoryList = gson.fromJson(response.getString("data"), listType);
//
//                        mAdapter = new CategoryAdapter(categoryList);
//                        rv_items.setAdapter(mAdapter);
//                        mAdapter.notifyDataSetChanged();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        // Adding request to request queue
//        DADApp.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}


