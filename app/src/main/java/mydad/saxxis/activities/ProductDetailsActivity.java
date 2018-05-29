package mydad.saxxis.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import mydad.saxxis.R;
import mydad.saxxis.adapter.OfferAdapter;
import mydad.saxxis.adapter.PriceAdapter;
import mydad.saxxis.adapter.SectionPagerAdapter;
import mydad.saxxis.models.Product;
import mydad.saxxis.models.Quantity;
import mydad.saxxis.models.Stock;
import mydad.saxxis.reterofit.APIUrls;
import mydad.saxxis.reterofit.RetrofitInstance;
import mydad.saxxis.reterofit.RetrofitService;
import mydad.saxxis.util.DatabaseHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener, BaseSliderView.OnSliderClickListener {
    public static final String PRODUCT = "PRODUCT";
    public static final String CONTENT = "CONTENT";
    public static final int PRODUCT_DETAIL = 5241;
    private DatabaseHandler dbcart;
    private TextView tv_content;
    private Product product;
    private Spinner spinner_stock;
    private boolean isUpdated;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        String value = getIntent().getStringExtra(PRODUCT);
        String content = getIntent().getStringExtra(CONTENT);
        product = new Gson().fromJson(value, Product.class);

        dbcart = new DatabaseHandler();

        final TextView tv_discount = findViewById(R.id.tv_discount);
        tv_content = findViewById(R.id.tv_content);
        tv_content.setText(content);

        final ImageView iv_plus = findViewById(R.id.iv_plus);
        iv_plus.setOnClickListener(this);

        final ImageView iv_minus = findViewById(R.id.iv_minus);
        iv_minus.setOnClickListener(this);

        final TextView tv_out_of_stock = findViewById(R.id.tv_out_of_stock);
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

        if (dbcart.isInCart(product.getProduct_id())) {
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
            tv_content.setText("0");
        }

        spinner_stock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Stock stock = priceAdapter.getItem(position);
                Product p = dbcart.getProduct(product.getProduct_id(), stock.getStockId());

                if (TextUtils.isEmpty(stock.getStrikeprice())) {
                    tv_discount_price.setText(String.format("\u20B9 %s", stock.getPrice_val()));
                    tv_price.setVisibility(View.GONE);
                    tv_discount.setVisibility(View.GONE);
                } else {
                    float price = Integer.parseInt(stock.getPrice_val());
                    float discountPrice = Integer.parseInt(stock.getStrikeprice());
                    int result = (int) Math.ceil(((price - discountPrice) / price) * 100);

                    tv_discount.setVisibility(View.VISIBLE);
                    tv_discount.setText(result + "%" + "\noff");
                    tv_price.setVisibility(View.VISIBLE);
                    tv_price.setText(String.format("(\u20B9 %s)", stock.getPrice_val()));
                    tv_discount_price.setText(String.format("\u20B9 %s", stock.getStrikeprice()));
                }

                if (stock.getStock().equals("0")) {
                    iv_plus.setOnClickListener(null);
                    iv_minus.setOnClickListener(null);
                    tv_out_of_stock.setVisibility(View.VISIBLE);
                } else {
                    iv_minus.setOnClickListener(ProductDetailsActivity.this);
                    iv_plus.setOnClickListener(ProductDetailsActivity.this);
                    tv_out_of_stock.setVisibility(View.GONE);
                }

                if (p == null) {
                    tv_content.setText("0");
                } else {
                    tv_content.setText(String.valueOf(p.getQuantity()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

                addProduct();
                break;
            case R.id.iv_plus:
                int qty = Integer.valueOf(tv_content.getText().toString());
                qty = qty + 1;

                if (qty > product.getQuantity_per_user()) {
                    if (toast != null) {
                        toast.cancel();
                    }

                    toast = Toast.makeText(this, String.format("Only %s items allowed per user for this product", product.getQuantity_per_user()), Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    final Stock stock = (Stock) spinner_stock.getSelectedItem();
                    final int qty1 = Integer.parseInt(tv_content.getText().toString().trim());

                    if (!TextUtils.isEmpty(stock.getStock()) && Integer.parseInt(stock.getStock()) > qty1) {
                        tv_content.setText(String.valueOf(qty));
                        addProduct();
                    } else {
                        if (toast != null) {
                            toast.cancel();
                        }

                        if (stock.getStock().equals("0")) {
                            toast = Toast.makeText(this, R.string.product_out_of_stock, Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            toast = Toast.makeText(this, String.format("Only %s products left", stock.getStock()), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }
                break;
        }
    }

    private synchronized void addProduct() {
        final Stock stock = (Stock) spinner_stock.getSelectedItem();
        final int qty = Integer.parseInt(tv_content.getText().toString().trim());

        if (qty > 0) {
            if (!TextUtils.isEmpty(stock.getStock()) && Integer.parseInt(stock.getStock()) >= qty) {

                if (qty > product.getQuantity_per_user()) {
                    if (toast != null) {
                        toast.cancel();
                    }

                    toast = Toast.makeText(ProductDetailsActivity.this, String.format("Only %s items allowed per user for this product", product.getQuantity_per_user()), Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                    product.setStockId(stock.getStockId());
                    product.setStocks(new Gson().toJson(product.getCustom_fields()));
                    product.setQuantity(qty);

                    tv_content.setText(String.valueOf(qty));
                    dbcart.setCart(product);
                    isUpdated = true;
                }
            } else {
                if (toast != null) {
                    toast.cancel();
                }

                if (stock.getStock().equals("0")) {
                    toast = Toast.makeText(ProductDetailsActivity.this, R.string.product_out_of_stock, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    toast = Toast.makeText(ProductDetailsActivity.this, String.format("Only %s products left", stock.getStock()), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        } else {
            Product p = dbcart.getProduct(product.getProduct_id(), product.getStockId());

            if (p != null) {
                dbcart.removeItemFromCart(p.getProduct_id(), product.getStockId());
            }

            isUpdated = true;
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
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
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
