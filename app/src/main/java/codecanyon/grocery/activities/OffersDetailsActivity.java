package codecanyon.grocery.activities;

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

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import codecanyon.grocery.R;
import codecanyon.grocery.fragments.ProductsAboutFragment;
import codecanyon.grocery.fragments.ProductsHealthBenefitsFragment;
import codecanyon.grocery.fragments.ProductsHowToUseFragment;
import codecanyon.grocery.models.Offers;
import codecanyon.grocery.models.Product;
import codecanyon.grocery.reterofit.APIUrls;
import codecanyon.grocery.util.DatabaseHandler;

/**
 * Created by srikarn on 30-03-2018.
 */

public class OffersDetailsActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private Product product;
    private Context context;
    private DatabaseHandler dbcart;
    private SliderLayout imgSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        mSectionsPagerAdapter = new OffersDetailsActivity.SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.masterViewPager);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = OffersDetailsActivity.this;
        dbcart = new DatabaseHandler();
        Bundle bundle = getIntent().getExtras();
        int position = bundle.getInt("position");
        product = bundle.getParcelable("selectedProduct");
        String getqty = bundle.getString("total");
/*        String getqty = bundle.getString("qty");
        int qty = Integer.parseInt(getqty);*/

        TabLayout tabLayout = findViewById(R.id.tabLayout);



        mSectionsPagerAdapter.addFragment(ProductsAboutFragment.newInstance(product.getProduct_description()),"About");
        mSectionsPagerAdapter.addFragment(ProductsHealthBenefitsFragment.newInstance(product.getProduct_description()),"Health Benefits");
        mSectionsPagerAdapter.addFragment(ProductsHowToUseFragment.newInstance(product.getHow_to_use())," How To Use");
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setCurrentItem(0);

        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).select();

//        showProductDetail(product.getProduct_image(),
//                product.getProduct_name(),
//                product.getProduct_description(),
//                product.getProduct_name(),
//                position ,getqty, product.getPrice(), product.getUnit());

    }




    private void showProductDetail(String image, String title, String description, String detail, final int position, String total, String price, String quantity) {// showProductDetail(product.getProduct_image(),
       /* product.getTitle(),
                product.getProduct_description(),
                product.getProduct_name(),
                position, tv_subcat_content.getText().toString());*/


        //ImageView iv_image = (ImageView) findViewById(R.id.iv_product_detail_img);
        imgSlider = findViewById(R.id.iv_product_detail_img);
        ImageView iv_minus = findViewById(R.id.iv_subcat_minus);
        ImageView iv_plus = findViewById(R.id.iv_subcat_plus);
        TextView tv_title = findViewById(R.id.tv_product_detail_title);
        TextView tv_name = findViewById(R.id.product_name);
        TextView tv_quantity = findViewById(R.id.quantity);
        TextView tv_price = findViewById(R.id.price);
        final TextView tv_detail = findViewById(R.id.tv_product_detail);
        final TextView tv_content = findViewById(R.id.tv_subcat_content);
        final TextView tv_add = findViewById(R.id.tv_subcat_add);


        imgSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        imgSlider.stopAutoCycle();

        String [] imageurls = image.split(",");
        for(int i =0; i< imageurls.length; i++){
            DefaultSliderView textSliderView = new DefaultSliderView(context);
            if(imageurls[i] == ""){
                textSliderView
                        .image(R.drawable.ic_logonew)
                        .setScaleType(BaseSliderView.ScaleType.FitCenterCrop);
                imgSlider.addSlider(textSliderView);
            } else {
                // initialize a SliderLayout
                textSliderView
                        .image(APIUrls.IMG_PRODUCT_URL + imageurls[i])
                        .setScaleType(BaseSliderView.ScaleType.FitCenterCrop);
                imgSlider.addSlider(textSliderView);
            }
        }

        tv_title.setText(title);
        tv_detail.setText(detail);
        //tv_subcat_content.setText(qty);
        tv_name.setText(detail);
        tv_quantity.setText(quantity);
        tv_detail.setText(total);
        tv_price.setText("RS");
        tv_price.append(" " + price);

        /*Glide.with(context)
                .load(APIAPIUrls.IMG_PRODUCT_URL + iv_category)
                .placeholder(R.drawable.logonew)
                .fitCenter()
                .crossFade()
                .into(iv_image);
*/
        if (dbcart.isInCart(product.getProduct_id())) {
            tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
            tv_content.setText(dbcart.getCartItemQty(product.getProduct_id()));

        } else {
            tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
        }

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int qty = Integer.parseInt(tv_content.getText().toString().trim());

                if (qty > 1) {
                    product.setQuantity(qty);
                    dbcart.setCart(product);
                    tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                }

                ((MainActivity) context).setCartCounter(dbcart.getCartCount());


            }
        });

        iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.valueOf(tv_content.getText().toString());
                qty = qty + 1;

                tv_content.setText(String.valueOf(qty));
            }
        });

        iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = 0;
                if (!tv_content.getText().toString().equalsIgnoreCase(""))
                    qty = Integer.valueOf(tv_content.getText().toString());

                if (qty > 0) {
                    qty = qty - 1;
                    tv_content.setText(String.valueOf(qty));
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
