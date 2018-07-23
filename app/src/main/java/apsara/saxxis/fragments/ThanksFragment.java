package apsara.saxxis.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import apsara.saxxis.activities.MainActivity;
import apsara.saxxis.R;

/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class ThanksFragment extends Fragment implements View.OnClickListener {

    TextView tv_info;
    Button btn_home, btn_order;

    public ThanksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_thanks, container, false);

        // handle the touch event if true
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // check user can press back tv_add or not
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    Fragment fm = new HomeFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_layout, fm)
                            .addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });

        String data = getArguments().getString("msg");

        tv_info = view.findViewById(R.id.tv_thank_info);
        btn_home = view.findViewById(R.id.btn_thank_home);
        btn_order = view.findViewById(R.id.btn_thank_order);

        tv_info.setText(Html.fromHtml(data));

        btn_home.setOnClickListener(this);
        btn_order.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_thank_home) {

            Fragment fm = new HomeFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout, fm)
                    .addToBackStack(null).commit();
        } else if (id == R.id.btn_thank_order) {

            Fragment fm = new MyOrderFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout, fm)
                    .addToBackStack(null).commit();
        }

    }
}
