package codecanyon.grocery;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Config.BaseURL;
import Fragments.Products_about_fragment;
import Model.Product_model;
import util.DatabaseHandler;

public class ProductDetailsActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private Product_model product;
    private Context context;
    private DatabaseHandler dbcart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.masterViewPager);
        mSectionsPagerAdapter.addFragment(Products_about_fragment.newInstance(),"About");
        mSectionsPagerAdapter.addFragment(Products_about_fragment.newInstance(),"Health Benefits");
        mSectionsPagerAdapter.addFragment(Products_about_fragment.newInstance()," How To Use");
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setCurrentItem(0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = ProductDetailsActivity.this;
        dbcart = new DatabaseHandler(context);
        Bundle bundle = getIntent().getExtras();
        int position = bundle.getInt("position");
        product = bundle.getParcelable("selectedProduct");
/*        String getqty = bundle.getString("qty");
        int qty = Integer.parseInt(getqty);*/

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).select();

        showProductDetail(product.getProduct_image(),
                product.getTitle(),
                product.getProduct_description(),
                product.getProduct_name(),
                position , product.getPrice(), product.getUnit());

    }




    private void showProductDetail(String image, String title, String description, String detail, final int position, String price, String quantity) {// showProductDetail(product.getProduct_image(),
       /* product.getTitle(),
                product.getProduct_description(),
                product.getProduct_name(),
                position, tv_contetiy.getText().toString());*/


        ImageView iv_image = (ImageView) findViewById(R.id.iv_product_detail_img);
        ImageView iv_minus = (ImageView) findViewById(R.id.iv_subcat_minus);
        ImageView iv_plus = (ImageView) findViewById(R.id.iv_subcat_plus);
        TextView tv_title = (TextView) findViewById(R.id.tv_product_detail_title);
        TextView tv_name = (TextView) findViewById(R.id.product_name);
        TextView tv_quantity = (TextView) findViewById(R.id.quantity);
        TextView tv_price = (TextView) findViewById(R.id.price);
        TextView tv_detail = (TextView) findViewById(R.id.tv_product_detail);
        final TextView tv_contetiy = (TextView) findViewById(R.id.tv_subcat_contetiy);
        final TextView tv_add = (TextView) findViewById(R.id.tv_subcat_add);

        tv_title.setText(title);
        tv_detail.setText(detail);
        //tv_contetiy.setText(qty);
        tv_name.setText(detail);
        tv_quantity.setText(quantity);
        tv_detail.setText(description);
        tv_price.setText("RS");
        tv_price.append(" " + price);

        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + image)
                .placeholder(R.drawable.logonew)
                .fitCenter()
                .crossFade()
                .into(iv_image);

        if (dbcart.isInCart(product.getProduct_id())) {
            tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
            tv_contetiy.setText(dbcart.getCartItemQty(product.getProduct_id()));

        } else {
            tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
        }

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> map = new HashMap<>();

                map.put("product_id", product.getProduct_id());
                map.put("category_id", product.getCategory_id());
                map.put("product_image", product.getProduct_image());
                map.put("increament", product.getIncreament());
                map.put("product_name", product.getProduct_name());

                map.put("price", product.getPrice());
                map.put("stock", product.getIn_stock());
                map.put("title", product.getTitle());
                map.put("unit", product.getUnit());

                map.put("unit_value", product.getUnit_value());

                if (!tv_contetiy.getText().toString().equalsIgnoreCase("0")) {

                    if (dbcart.isInCart(map.get("product_id"))) {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                    } else {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                    }
                } else {
                    dbcart.removeItemFromCart(map.get("product_id"));
                    tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
                }

                Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("product_id")));
                Double price = Double.parseDouble(map.get("price"));

                //((MainActivity) context).setCartCounter("" + dbcart.getCartCount());


            }
        });

        iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.valueOf(tv_contetiy.getText().toString());
                qty = qty + 1;

                tv_contetiy.setText(String.valueOf(qty));
            }
        });

        iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = 0;
                if (!tv_contetiy.getText().toString().equalsIgnoreCase(""))
                    qty = Integer.valueOf(tv_contetiy.getText().toString());

                if (qty > 0) {
                    qty = qty - 1;
                    tv_contetiy.setText(String.valueOf(qty));
                }
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
                return mFragmentList.get(position);

        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment,String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
