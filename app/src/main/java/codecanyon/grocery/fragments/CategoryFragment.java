package codecanyon.grocery.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import codecanyon.grocery.adapter.CategoryAdapter;

import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.R;
import codecanyon.grocery.adapter.CategoryHomeAdapter;
import codecanyon.grocery.models.Category;
import codecanyon.grocery.models.CategoryResponse;
import codecanyon.grocery.reterofit.RetrofitInstance;
import codecanyon.grocery.reterofit.RetrofitService;
import codecanyon.grocery.util.ConnectivityReceiver;
import codecanyon.grocery.util.RecyclerTouchListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class CategoryFragment extends Fragment {

    private CategoryHomeAdapter categoryAdapter;
    private RetrofitService service;
    private RelativeLayout rl_loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_category, container, false);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (getContext() != null) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
        });

        service = RetrofitInstance.createService(RetrofitService.class);

        rl_loading = view.findViewById(R.id.rl_loading);
        rl_loading.setVisibility(View.VISIBLE);

        RecyclerView rv_category = view.findViewById(R.id.rv_category);
        rv_category.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        categoryAdapter = new CategoryHomeAdapter(getActivity());
        rv_category.setAdapter(categoryAdapter);

        if (ConnectivityReceiver.isConnected()) {
            makeGetCategoryRequest();
        }

        return view;
    }

    private void makeGetCategoryRequest() {
        service.getCategory().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    CategoryResponse cr = response.body();
                    rl_loading.setVisibility(View.GONE);
                    categoryAdapter.addItems(cr.getData());
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();

            }
        });
    }

}


