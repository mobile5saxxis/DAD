package codecanyon.grocery.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import codecanyon.grocery.R;
import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.activities.ProductDetailsActivity;
import codecanyon.grocery.models.Quantity;
import codecanyon.grocery.models.Stock;
import codecanyon.grocery.models.Product;
import codecanyon.grocery.reterofit.APIUrls;
import codecanyon.grocery.reterofit.RetrofitInstance;
import codecanyon.grocery.reterofit.RetrofitService;
import codecanyon.grocery.util.DatabaseHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class ProductAdapter extends CommonRecyclerAdapter<Product> {

    private Context context;
    private DatabaseHandler dbcart;
    private RetrofitService service;

    public ProductAdapter(Context context) {
        this.context = context;
        dbcart = new DatabaseHandler();
        service = RetrofitInstance.createService(RetrofitService.class);
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);

        context = parent.getContext();

        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        ProductViewHolder vh = (ProductViewHolder) holder;
        vh.bindData(position);
    }

    private void showImage(String image) {

        final Dialog dialog = new Dialog(context);
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

        Glide.with(context)
                .load(APIUrls.IMG_PRODUCT_URL + image)
                .apply(requestOptions)
                .into(iv_image);

        iv_image_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_subcat_title, tv_subcat_price, tv_subcat_content, tv_subcat_add, tv_subcat_discount_price, tv_discount, tv_out_of_stock;
        public ImageView iv_subcat, iv_subcat_plus, iv_subcat_minus, iv_subcat_remove;
        public Spinner spinner_subcat;
        private LinearLayout ll_add_content;
        public static final int ADD_CLICK_MSG = 5546;

        public ProductViewHolder(View view) {
            super(view);

            tv_out_of_stock = view.findViewById(R.id.tv_out_of_stock);
            ll_add_content = view.findViewById(R.id.ll_add_content);
//            ll_add_content.setVisibility(View.INVISIBLE);
            tv_discount = view.findViewById(R.id.tv_discount);
            tv_subcat_title = view.findViewById(R.id.tv_subcat_title);
            tv_subcat_discount_price = view.findViewById(R.id.tv_subcat_discount_price);
            tv_subcat_price = view.findViewById(R.id.tv_subcat_price);
            tv_subcat_price.setPaintFlags(tv_subcat_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tv_subcat_content = view.findViewById(R.id.tv_subcat_content);
            tv_subcat_add = view.findViewById(R.id.tv_subcat_add);
            iv_subcat = view.findViewById(R.id.iv_subcat);
            iv_subcat_plus = view.findViewById(R.id.iv_subcat_plus);
            iv_subcat_minus = view.findViewById(R.id.iv_subcat_minus);
            iv_subcat_remove = view.findViewById(R.id.iv_subcat_remove);
            iv_subcat_remove.setVisibility(View.GONE);
            spinner_subcat = view.findViewById(R.id.spinner_subcat);

            iv_subcat_minus.setOnClickListener(this);
            iv_subcat_plus.setOnClickListener(this);
            tv_subcat_add.setOnClickListener(this);
            iv_subcat.setOnClickListener(this);

            tv_subcat_title.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();
            final Product product = getItem(position);

            switch (id) {
                case R.id.iv_subcat_plus:
                    int qty = Integer.valueOf(tv_subcat_content.getText().toString());
                    qty = qty + 1;

                    if (qty > product.getQuantity_per_user()) {
                        Toast.makeText(context, String.format("Only %s items allowed per user for this product", product.getQuantity_per_user()), Toast.LENGTH_SHORT).show();
                    } else {
                        tv_subcat_content.setText(String.valueOf(qty));
                        addProduct(product);
                    }


                    break;
                case R.id.spinner_subcat:
                    int qty1 = Integer.valueOf(tv_subcat_content.getText().toString());
                    qty1 = qty1 + 1;

                    tv_subcat_content.setText(String.valueOf(qty1));
                    break;
                case R.id.iv_subcat_minus:
                    int qty2 = 0;
                    if (!tv_subcat_content.getText().toString().equalsIgnoreCase(""))
                        qty2 = Integer.valueOf(tv_subcat_content.getText().toString());

                    if (qty2 > 0) {
                        qty2 = qty2 - 1;
                        tv_subcat_content.setText(String.valueOf(qty2));
                    }

                    if (qty2 == 0) {
                        tv_subcat_add.setText(R.string.tv_pro_add);
                    }

                    addProduct(product);
                    break;
                case R.id.tv_subcat_add:

//                    if (ll_add_content.getVisibility() == View.INVISIBLE) {
//                        ll_add_content.setVisibility(View.VISIBLE);
//                    }

                    addProduct(product);

                    break;
                case R.id.iv_subcat:
                    String image = product.getProduct_image();

                    if (image.contains(",")) {
                        String[] images = image.split(",");

                        if (images.length > 0) {
                            image = images[0];
                        }
                    }

                    showImage(image);
                    break;
                case R.id.tv_subcat_title:
                    String content = tv_subcat_content.getText().toString();
                    String value = new Gson().toJson(product);
                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    intent.putExtra(ProductDetailsActivity.PRODUCT, value);
                    intent.putExtra(ProductDetailsActivity.CONTENT, content);
                    Activity activity = (Activity) context;
                    activity.startActivityForResult(intent, ProductDetailsActivity.PRODUCT_DETAIL);
                    break;
            }
        }

        private synchronized void addProduct(final Product product) {
            final Stock stock = (Stock) spinner_subcat.getSelectedItem();
            final int qty = Integer.parseInt(tv_subcat_content.getText().toString().trim());

            if (qty > 0) {
                if (!TextUtils.isEmpty(stock.getStock()) && Integer.parseInt(stock.getStock()) >= qty) {

                    if (qty > product.getQuantity_per_user()) {
                        Toast.makeText(context, String.format("Only %s items allowed per user for this product", product.getQuantity_per_user()), Toast.LENGTH_SHORT).show();
                    } else {

                        product.setStockId(stock.getStockId());
                        product.setStocks(new Gson().toJson(product.getCustom_fields()));
                        product.setQuantity(qty);

                        tv_subcat_content.setText(String.valueOf(qty));
                        dbcart.setCart(product);
                        tv_subcat_add.setText(context.getResources().getString(R.string.tv_pro_update));
                        ((MainActivity) context).setCartCounter(String.valueOf(dbcart.getCartCount()));
                    }
                } else {
                    if (stock.getStock().equals("0")) {
                        Toast.makeText(context, R.string.product_out_of_stock, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, String.format("Only %s products left", stock.getStock()), Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Product p = dbcart.getProduct(product.getProduct_id());

                if (p != null) {
                    dbcart.removeItemFromCart(p.getId());
                }

                ((MainActivity) context).setCartCounter(String.valueOf(dbcart.getCartCount()));
            }
        }

        private void bindData(int position) {
            final Product product = getItem(position);

            final PriceAdapter priceAdapter = new PriceAdapter(context, product.getCustom_fields());
            spinner_subcat.setAdapter(priceAdapter);

            spinner_subcat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Stock stock = priceAdapter.getItem(position);

                    if (TextUtils.isEmpty(stock.getStrikeprice())) {
                        tv_subcat_discount_price.setText(String.format("\u20B9 %s", stock.getPrice_val()));
                        tv_subcat_price.setVisibility(View.GONE);
                        tv_discount.setVisibility(View.GONE);
                    } else {
                        float price = Integer.parseInt(stock.getPrice_val());
                        float discountPrice = Integer.parseInt(stock.getStrikeprice());
                        int result = (int) Math.ceil(((price - discountPrice) / price) * 100);

                        tv_discount.setVisibility(View.VISIBLE);
                        tv_discount.setText(result + "%" + "\noff");
                        tv_subcat_price.setVisibility(View.VISIBLE);
                        tv_subcat_price.setText(String.format("(\u20B9 %s)", stock.getPrice_val()));
                        tv_subcat_discount_price.setText(String.format("\u20B9 %s", stock.getStrikeprice()));
                    }

                    if (stock.getStock().equals("0")) {
                        iv_subcat_plus.setOnClickListener(null);
                        iv_subcat_minus.setOnClickListener(null);
                        tv_out_of_stock.setVisibility(View.VISIBLE);
                    } else {
                        iv_subcat_minus.setOnClickListener(ProductViewHolder.this);
                        iv_subcat_plus.setOnClickListener(ProductViewHolder.this);
                        tv_out_of_stock.setVisibility(View.GONE);
                        addProduct(product);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            String image = product.getProduct_image();

            if (image.contains(",")) {
                String[] images = image.split(",");

                if (images.length > 0) {
                    image = images[0];
                }
            }

            Glide.with(context)
                    .load(APIUrls.IMG_PRODUCT_URL + image.replace(" ", "%20"))
                    .apply(requestOptions)
                    .into(iv_subcat);

            tv_subcat_title.setText(product.getProduct_name());

            if (dbcart.isInCart(product.getProduct_id())) {
//                ll_add_content.setVisibility(View.VISIBLE);
                tv_subcat_add.setText(context.getResources().getString(R.string.tv_pro_update));
                tv_subcat_content.setText(dbcart.getCartItemQty(product.getProduct_id()));

                Product p = dbcart.getProduct(product.getProduct_id());

                if (p.getStocks() != null) {
                    List<Stock> stocks = new Gson().fromJson(p.getStocks(), new TypeToken<List<Stock>>() {
                    }.getType());

                    for (int i = 0; i < stocks.size(); i++) {
                        Stock stock1 = stocks.get(i);

                        if (p.getStockId() == stock1.getStockId()) {
                            spinner_subcat.setSelection(i);
                            break;
                        }
                    }
                }
            } else {
//                ll_add_content.setVisibility(View.INVISIBLE);
                tv_subcat_add.setText(context.getResources().getString(R.string.tv_pro_add));
            }
        }

    }
}