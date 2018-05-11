package codecanyon.grocery.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.R;
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

import static codecanyon.grocery.adapter.ProductAdapter.ProductViewHolder.ADD_CLICK_MSG;

/**
 * Created by srikarn on 26-03-2018.
 */

public class OfferAdapter extends CommonRecyclerAdapter<Product> {
    private Context context;
    private DatabaseHandler dbcart;
    private Toast toast;

    public OfferAdapter(Context context) {
        this.context = context;
        dbcart = new DatabaseHandler();
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_best_product, parent, false);

        context = parent.getContext();
        return new OfferViewHolder(itemView);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        OfferViewHolder offerViewHolder = (OfferViewHolder) holder;
        offerViewHolder.bind(position);
    }

    public class OfferViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_title, tv_add, tv_content, tv_discount_price, tv_price, tv_discount, tv_out_of_stock;
        public ImageView iv_image, iv_plus, iv_minus;
        private Spinner spinner_quantity;
        private RelativeLayout rl_content;

        public OfferViewHolder(View view) {
            super(view);

            tv_out_of_stock = view.findViewById(R.id.tv_out_of_stock);
            rl_content = view.findViewById(R.id.rl_content);
            tv_title = view.findViewById(R.id.tv_title);
            iv_image = view.findViewById(R.id.iv_image);
            tv_add = view.findViewById(R.id.tv_add);
            iv_plus = view.findViewById(R.id.iv_subcat_plus);
            iv_minus = view.findViewById(R.id.iv_subcat_minus);
            tv_content = view.findViewById(R.id.tv_subcat_content);
            tv_discount = view.findViewById(R.id.tv_discount);

            iv_minus.setOnClickListener(this);
            iv_plus.setOnClickListener(this);
            tv_add.setOnClickListener(this);
            iv_image.setOnClickListener(this);

            spinner_quantity = view.findViewById(R.id.spinner_qantity);
            tv_discount_price = view.findViewById(R.id.tv_discount_price);
            tv_price = view.findViewById(R.id.tv_price);
            tv_price.setPaintFlags(tv_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            tv_title.setOnClickListener(this);

        }

        private void bind(int position) {
            final Product product = getItem(position);
            final PriceAdapter priceAdapter = new PriceAdapter(context, product.getCustom_fields());
            spinner_quantity.setAdapter(priceAdapter);

            spinner_quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Stock stock = priceAdapter.getItem(position);

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
                        iv_minus.setOnClickListener(null);
                        iv_plus.setOnClickListener(null);
                        tv_out_of_stock.setVisibility(View.VISIBLE);
                    } else {
                        iv_minus.setOnClickListener(OfferViewHolder.this);
                        iv_plus.setOnClickListener(OfferViewHolder.this);
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
                    .load(APIUrls.IMG_PRODUCT_URL + image)
                    .apply(requestOptions)
                    .into(iv_image);

            tv_title.setText(product.getProduct_name());

            if (dbcart.isInCart(product.getProduct_id())) {
                tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                tv_content.setText(dbcart.getCartItemQty(product.getProduct_id()));

                Product p = dbcart.getProduct(product.getProduct_id());

                if (p.getStocks() != null) {
                    List<Stock> stocks = new Gson().fromJson(p.getStocks(), new TypeToken<List<Stock>>() {
                    }.getType());

                    for (int i = 0; i < stocks.size(); i++) {
                        Stock stock1 = stocks.get(i);

                        if (p.getStockId() == stock1.getStockId()) {
                            spinner_quantity.setSelection(i);
                            break;
                        }
                    }
                }

//                rl_content.setVisibility(View.VISIBLE);
            } else {
                tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
//                rl_content.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();
            Product product = getItem(position);

            if (id == R.id.tv_title) {
                String content = tv_content.getText().toString();

                Product product1 = getItem(position);
                String value = new Gson().toJson(product1);
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra(ProductDetailsActivity.PRODUCT, value);
                intent.putExtra(ProductDetailsActivity.CONTENT, content);
                Activity activity = (Activity) context;
                activity.startActivityForResult(intent, ProductDetailsActivity.PRODUCT_DETAIL);
            } else if (id == R.id.iv_image) {
                String image = getItem(position).getProduct_image();

                if (image.contains(",")) {
                    String[] images = image.split(",");

                    if (images.length > 0) {
                        image = images[0];
                    }
                }

                showImage(image);
            } else if (id == R.id.iv_subcat_minus) {

                int qty2 = 0;
                if (!tv_content.getText().toString().equalsIgnoreCase(""))
                    qty2 = Integer.valueOf(tv_content.getText().toString());

                if (qty2 > 0) {
                    qty2 = qty2 - 1;
                    tv_content.setText(String.valueOf(qty2));
                }

                if (qty2 == 0) {
                    tv_add.setText(R.string.tv_pro_add);
                }

                addProduct(product);
            } else if (id == R.id.iv_subcat_plus) {

                int qty = Integer.valueOf(tv_content.getText().toString());
                qty = qty + 1;

                if (qty > product.getQuantity_per_user()) {
                    if (toast != null) {
                        toast.cancel();
                    }

                    toast = Toast.makeText(context, String.format("Only %s items allowed per user for this product", product.getQuantity_per_user()), Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    final Stock stock = (Stock) spinner_quantity.getSelectedItem();
                    final int qty1 = Integer.parseInt(tv_content.getText().toString().trim());

                    if (!TextUtils.isEmpty(stock.getStock()) && Integer.parseInt(stock.getStock()) > qty1) {
                        tv_content.setText(String.valueOf(qty));
                        addProduct(product);
                    } else {
                        if (toast != null) {
                            toast.cancel();
                        }

                        if (stock.getStock().equals("0")) {
                            toast = Toast.makeText(context, R.string.product_out_of_stock, Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            toast = Toast.makeText(context, String.format("Only %s products left", stock.getStock()), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }


            } else if (id == R.id.tv_add) {
                rl_content.setVisibility(View.VISIBLE);

                addProduct(product);
            }
        }

        private synchronized void addProduct(final Product product) {
            final Stock stock = (Stock) spinner_quantity.getSelectedItem();
            final int qty = Integer.parseInt(tv_content.getText().toString().trim());

            if (qty > 0) {
                if (Integer.parseInt(stock.getStock()) >= qty) {

                    if (qty > product.getQuantity_per_user()) {
                        if (toast != null) {
                            toast.cancel();
                        }

                        toast = Toast.makeText(context, String.format(context.getString(R.string.only_items_allowed), String.valueOf(product.getQuantity_per_user())), Toast.LENGTH_SHORT);
                        toast.show();
                    } else {

                        product.setStockId(stock.getStockId());
                        product.setStocks(new Gson().toJson(product.getCustom_fields()));
                        product.setQuantity(qty);

                        tv_content.setText(String.valueOf(qty));
                        dbcart.setCart(product);
                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));

                        ((MainActivity) context).setCartCounter(String.valueOf(dbcart.getCartCount()));
                    }
                } else {
                    if (stock.getStock().equals("0")) {
                        if (toast != null) {
                            toast.cancel();
                        }

                        toast = Toast.makeText(context, R.string.product_out_of_stock, Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        toast = Toast.makeText(context, String.format("Only %s products left", stock.getStock()), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            } else {
                Product p = dbcart.getProduct(product.getProduct_id(), product.getStockId());

                if (p != null) {
                    dbcart.removeItemFromCart(p.getProduct_id(), product.getStockId());
                }

                ((MainActivity) context).setCartCounter(String.valueOf(dbcart.getCartCount()));
            }
        }
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
}
