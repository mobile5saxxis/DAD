package Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import codecanyon.grocery.R;

/**
 * Created by srikarn on 30-03-2018.
 */

public class BestSellers_fragment extends Fragment {

    public static Products_about_fragment newInstance() {
        Products_about_fragment fragment = new Products_about_fragment();
        Bundle args = new Bundle();
        //args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.bestproduct_fragment, container, false);

        return layout;
    }


}