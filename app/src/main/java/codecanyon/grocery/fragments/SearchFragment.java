package codecanyon.grocery.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import codecanyon.grocery.R;
import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.adapter.ProductAdapter;
import codecanyon.grocery.models.Product;
import codecanyon.grocery.models.ProductRequest;
import codecanyon.grocery.models.ProductResponse;
import codecanyon.grocery.reterofit.RetrofitInstance;
import codecanyon.grocery.reterofit.RetrofitService;
import codecanyon.grocery.util.ConnectivityReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rajesh Dabhi on 14/7/2017.
 */

public class SearchFragment extends Fragment {

    private static String TAG = SearchFragment.class.getSimpleName();

    private EditText et_search;
    private ProductAdapter adapter_product;
    private String search_text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        et_search = view.findViewById(R.id.et_search);
        et_search.requestFocus();
        final InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        RecyclerView rv_search = view.findViewById(R.id.rv_search);
        rv_search.setLayoutManager(new LinearLayoutManager(getActivity()));

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value = et_search.getText().toString().trim();

                if (!value.isEmpty()) {
                    makeGetProductRequest(value);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String value = et_search.getText().toString().trim();

                    if (!value.isEmpty()) {
                        makeGetProductRequest(value);
                        imgr.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
                    }

                    return true;
                }
                return false;
            }
        });

        adapter_product = new ProductAdapter(getContext());
        rv_search.setAdapter(adapter_product);

        return view;
    }

    /**
     * Method to make json object request where json response starts wtih {
     */
    private void makeGetProductRequest(String search_text) {
        this.search_text = search_text;

        RetrofitService service = RetrofitInstance.createService(RetrofitService.class);

        service.search(search_text).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    adapter_product.resetItems();

                    ProductResponse pr = response.body();

                    if (pr != null) {
                        if (pr.getData().size() == 0) {
                            Toast.makeText(getContext(), "No products found", Toast.LENGTH_SHORT).show();
                        }

                        adapter_product.addItems(pr.getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void resetProducts() {
        if (search_text != null) {
            makeGetProductRequest(search_text);
        }
    }
}
