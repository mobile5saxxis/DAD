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

    String mDescription;

    public static ProductsAboutFragment newInstance(String  description) {
        ProductsAboutFragment fragment = new ProductsAboutFragment();
        Bundle args = new Bundle();
        args.putString("description", description);
        //args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){

            mDescription = getArguments().getString("description");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_product_about, container, false);

        TextView about = layout.findViewById(R.id.aboutdescription);
        about.setText(mDescription);

        return layout;
    }



}
