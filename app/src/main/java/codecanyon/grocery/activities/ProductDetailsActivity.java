package codecanyon.grocery.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import codecanyon.grocery.R;
import codecanyon.grocery.adapter.PriceAdapter;
import codecanyon.grocery.adapter.SectionPagerAdapter;
import codecanyon.grocery.models.Product;
import codecanyon.grocery.models.Stock;
import codecanyon.grocery.reterofit.APIUrls;
import codecanyon.grocery.util.DatabaseHandler;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener, BaseSliderView.OnSliderClickListener {
    public static final String PRODUCT = "PRODUCT";
    public static final int PRODUCT_DETAIL = 5241;
    private DatabaseHandler dbcart;
    private TextView tv_add, tv_content;
    private Product product;
    private Spinner spinner_stock;
    private boolean isUpdated;
    private LinearLayout ll_add_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        String value = getIntent().getStringExtra(PRODUCT);
        product = new Gson().fromJson(value, Product.class);

        dbcart = new DatabaseHandler();

        ll_add_content = findViewById(R.id.ll_add_content);
        tv_content = findViewById(R.id.tv_content);
        tv_add = findViewById(R.id.tv_add);
        tv_add.setOnClickListener(this);
        findViewById(R.id.iv_plus).setOnClickListener(this);
        findViewById(R.id.iv_minus).setOnClickListener(this);

        TextView tv_title = findViewById(R.id.tv_title);
        final TextView tv_discount_price = findViewById(R.id.tv_discount_price);
        final TextView tv_price = findViewById(R.id.tv_price);
        spinner_stock = findViewById(R.id.spinner_stock);

        SliderLayout sl_product = findViewById(R.id.sl_product);
        sl_product.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sl_product.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sl_product.setCustomAnimation(new DescriptionAnimation());
        sl_product.setDuration(4000);
        sl_product.setOnClickListener(this);

        for (String imageUrl : product.getProduct_image().split(",")) {
            DefaultSliderView textSliderView = new DefaultSliderView(this);

            if (!imageUrl.trim().isEmpty()) {
                textSliderView
                        .image(APIUrls.IMG_PRODUCT_URL + imageUrl.replace(" ", "%20"))
                        .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                        .setOnSliderClickListener(this);
                sl_product.addSlider(textSliderView);
            }
        }

        tv_title.setText(product.getProduct_name());
        tv_price.setPaintFlags(tv_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        final PriceAdapter priceAdapter = new PriceAdapter(this, product.getCustom_fields());
        spinner_stock.setAdapter(priceAdapter);

        spinner_stock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Stock stock = priceAdapter.getItem(position);
                tv_discount_price.setText(String.format("\u20B9 %s", stock.getStrikeprice()));
                tv_price.setText(String.format("(\u20B9 %s)", stock.getPrice_val()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (dbcart.isInCart(product.getProduct_id())) {
            ll_add_content.setVisibility(View.VISIBLE);
            tv_add.setText(R.string.tv_pro_update);
            tv_content.setText(dbcart.getCartItemQty(product.getProduct_id()));

            Product p = dbcart.getProduct(product.getProduct_id());

            if (p.getStocks() != null) {
                List<Stock> stocks = new Gson().fromJson(p.getStocks(), new TypeToken<List<Stock>>() {
                }.getType());

                for (int i = 0; i < stocks.size(); i++) {
                    Stock stock = stocks.get(i);

                    if (p.getStockId() == stock.getStockId()) {
                        spinner_stock.setSelection(i);
                        break;
                    }
                }
            }
        } else {
            ll_add_content.setVisibility(View.INVISIBLE);
            tv_add.setText(R.string.tv_pro_add);
        }

        TabLayout tab_layout = findViewById(R.id.tab_layout);
        ViewPager view_pager = findViewById(R.id.view_pager);
        tab_layout.setupWithViewPager(view_pager);
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager(), this, product);
        view_pager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.iv_minus:
                int qty2 = 0;
                if (!tv_content.getText().toString().equalsIgnoreCase(""))
                    qty2 = Integer.valueOf(tv_content.getText().toString());

                if (qty2 > 0) {
                    qty2 = qty2 - 1;
                    tv_content.setText(String.valueOf(qty2));
                }
                break;
            case R.id.iv_plus:
                int qty = Integer.valueOf(tv_content.getText().toString());
                qty = qty + 1;

                tv_content.setText(String.valueOf(qty));
                break;
            case R.id.tv_add:
                Stock stock = (Stock) spinner_stock.getSelectedItem();
                int quantity = Integer.parseInt(tv_content.getText().toString().trim());
                ll_add_content.setVisibility(View.VISIBLE);

                if (quantity > 0) {
                    product.setStockId(stock.getStockId());
                    product.setStocks(new Gson().toJson(product.getCustom_fields()));
                    product.setQuantity(quantity);

                    dbcart.setCart(product);
                    tv_add.setText(R.string.tv_pro_update);
                    isUpdated = true;
                } else {
                    Product p = dbcart.getProduct(product.getProduct_id());

                    if (p != null) {
                        dbcart.removeItemFromCart(p.getId());
                        if (quantity == 0) {
                            ll_add_content.setVisibility(View.INVISIBLE);
                            tv_add.setText(R.string.tv_pro_add);
                        }
                    }
                }

                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.CART_UPDATED, isUpdated);
        setResult(PRODUCT_DETAIL, intent);
        finish();
    }

    private void showImage(String image) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.product_image_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        ImageView iv_image_cancle = dialog.findViewById(R.id.iv_dialog_cancle);
        ImageView iv_image = dialog.findViewById(R.id.iv_dialog_img);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_logonew)
                .error(R.drawable.ic_logonew)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(this)
                .load(image)
                .apply(requestOptions)
                .into(iv_image);

        iv_image_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        showImage(slider.getUrl());
    }
}
