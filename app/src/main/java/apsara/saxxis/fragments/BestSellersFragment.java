package apsara.saxxis.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import apsara.saxxis.R;

/**
 * Created by srikarn on 30-03-2018.
 */

public class BestSellersFragment extends Fragment {

    public static ProductsAboutFragment newInstance() {
        ProductsAboutFragment fragment = new ProductsAboutFragment();
        Bundle args = new Bundle();
        //args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.item_best_product, container, false);

        return layout;
    }


}