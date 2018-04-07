package codecanyon.grocery.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import codecanyon.grocery.R;

/**
 * Created by srikarn on 23-03-2018.
 */

public class ProductDetailsFragment extends Fragment {



    public ProductDetailsFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=  inflater.inflate(R.layout.dialog_product_detail, container,false);
        ImageView iv_image = view.findViewById(R.id.iv_product_detail_img);
        ImageView iv_minus = view.findViewById(R.id.iv_subcat_minus);
        ImageView iv_plus = view.findViewById(R.id.iv_subcat_plus);
        TextView tv_title = view.findViewById(R.id.tv_product_detail_title);
        TextView tv_detail = view.findViewById(R.id.tv_product_detail);
        final TextView tv_contetiy = view.findViewById(R.id.tv_subcat_content);
        final TextView tv_add = view.findViewById(R.id.tv_subcat_add);
        return null;
    }
}
