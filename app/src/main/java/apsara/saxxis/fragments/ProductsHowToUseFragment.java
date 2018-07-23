package apsara.saxxis.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import apsara.saxxis.R;

/**
 * Created by srikarn on 02-04-2018.
 */

public class ProductsHowToUseFragment extends Fragment {

    public static final String HOW_TO_USE = "HOW_TO_USE";
    private String how_to_use;

    public static ProductsHowToUseFragment newInstance(String description) {
        ProductsHowToUseFragment fragment = new ProductsHowToUseFragment();
        Bundle args = new Bundle();
        args.putString(HOW_TO_USE, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            how_to_use = getArguments().getString(HOW_TO_USE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_product_how_to_use, container, false);

        TextView tv_how_to_use = layout.findViewById(R.id.tv_how_to_use);
        tv_how_to_use.setText(how_to_use);

        return layout;
    }
}

