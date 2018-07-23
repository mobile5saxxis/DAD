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

public class ProductsHealthBenefitsFragment  extends Fragment {

    public static final String HEALTH = "HEALTH";
    private String health;

    public static ProductsAboutFragment newInstance(String description) {
        ProductsAboutFragment fragment = new ProductsAboutFragment();
        Bundle args = new Bundle();
        args.putString(HEALTH, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            health = getArguments().getString(HEALTH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_product_health_benifits, container, false);

        TextView tv_health = layout.findViewById(R.id.tv_health);
        tv_health.setText(health);

        return layout;
    }
}
