package Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import Adapter.Dialog_adapter;
import codecanyon.grocery.R;


/**
 * Created by srikarn on 26-03-2018.
 */

public class Dialog_fragment extends DialogFragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

       View rootview = inflater.inflate(R.layout.dialog_sample,container,false);

        tabLayout = (TabLayout) rootview.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) rootview.findViewById(R.id.masterViewPager);

        Dialog_adapter dialog_adapter = new Dialog_adapter(getChildFragmentManager());
        //dialog_adapter.addFragment("about", ProductDetails_fragment.createInstance());


        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
