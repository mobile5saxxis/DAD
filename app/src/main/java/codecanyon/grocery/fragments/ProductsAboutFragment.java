package codecanyon.grocery.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import codecanyon.grocery.R;

/**
 * Created by srikarn on 26-03-2018.
 */

public class ProductsAboutFragment extends Fragment {

    public static final String ABOUT = "HEALTH";
    private String about;

    public static ProductsAboutFragment newInstance(String description) {
        ProductsAboutFragment fragment = new ProductsAboutFragment();
        Bundle args = new Bundle();
        args.putString(ABOUT, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            about = getArguments().getString(ABOUT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_product_about, container, false);

        TextView tv_about = layout.findViewById(R.id.tv_about);
        tv_about.setText(about);

        return layout;
    }
}
