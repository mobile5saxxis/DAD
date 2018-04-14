package codecanyon.grocery.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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


public class BestProductAdapter extends CommonRecyclerAdapter<Product> {

    private Context context;
    private DatabaseHandler dbcart;
    private RetrofitService service;

    public BestProductAdapter(Context context) {
        this.context = context;
        dbcart = new DatabaseHandler();
        service = RetrofitInstance.createService(RetrofitService.class);

    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_best_product, parent, false);

        return new BestProductViewHolder(itemView);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        BestProductViewHolder viewHolder = (BestProductViewHolder) holder;
        viewHolder.bind(position);
    }

    public class BestProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_title, tv_add, tv_subcat_content, tv_discount_price, tv_price;
        private ImageView iv_image, iv_subcat_plus, iv_subcat_minus;
        private Spinner spinner_quantity;

        private BestProductViewHolder(View view) {
            super(view);
            tv_title = view.findViewById(R.id.tv_title);
            iv_image = view.findViewById(R.id.iv_image);
            tv_add = view.findViewById(R.id.tv_add);
            iv_subcat_plus = view.findViewById(R.id.iv_subcat_plus);
            iv_subcat_minus = view.findViewById(R.id.iv_subcat_minus);
            tv_subcat_content = view.findViewById(R.id.tv_subcat_content);

            iv_subcat_minus.setOnClickListener(this);
            iv_subcat_plus.setOnClickListener(this);
            tv_add.setOnClickListener(this);
            iv_image.setOnClickListener(this);

            spinner_quantity = view.findViewById(R.id.spinner_qantity);
            tv_discount_price = view.findViewById(R.id.tv_discount_price);
            tv_price = view.findViewById(R.id.tv_price);
            tv_price.setPaintFlags(tv_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            CardView cardView = view.findViewById(R.id.card_view_bestproducts);
            cardView.setOnClickListener(this);

        }

        public void bind(int position) {
            Product product = getItem(position);

            final PriceAdapter priceAdapter = new PriceAdapter(context, product.getCustom_fields());
            spinner_quantity.setAdapter(priceAdapter);

            spinner_quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Stock stock = priceAdapter.getItem(position);

                    if (TextUtils.isEmpty(stock.getStrikeprice())) {
                        tv_discount_price.setText(String.format("\u20B9 %s", stock.getPrice_val()));
                        tv_price.setVisibility(View.GONE);
                    } else {
                        tv_price.setVisibility(View.VISIBLE);
                        tv_price.setText(String.format("(\u20B9 %s)", stock.getPrice_val()));
                        tv_discount_price.setText(String.format("\u20B9 %s", stock.getStrikeprice()));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .dontAnimate()
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
                    .into(iv_image);

            tv_title.setText(product.getProduct_name());

            if (dbcart.isInCart(product.getProduct_id())) {
                tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                tv_subcat_content.setText(dbcart.getCartItemQty(product.getProduct_id()));

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
            } else {
                tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
            }
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();

            switch (id) {
                case R.id.card_view_bestproducts:
                    Product product1 = getItem(position);
                    String value = new Gson().toJson(product1);
                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    intent.putExtra(ProductDetailsActivity.PRODUCT, value);
                    context.startActivity(intent);
                    break;
                case R.id.iv_image:
                    Product pro = getItem(position);
                    String image = pro.getProduct_image();

                    if (image.contains(",")) {
                        String[] images = image.split(",");

                        if (images.length > 0) {
                            image = images[0];
                        }
                    }

                    showImage(image);
                    break;
                case R.id.iv_subcat_minus:
                    int qty = 0;
                    if (!tv_subcat_content.getText().toString().equalsIgnoreCase(""))
                        qty = Integer.valueOf(tv_subcat_content.getText().toString());

                    if (qty > 0) {
                        qty = qty - 1;
                        tv_subcat_content.setText(String.valueOf(qty));
                    }
                    break;
                case R.id.iv_subcat_plus:
                    int qty2 = Integer.valueOf(tv_subcat_content.getText().toString());
                    qty2 = qty2 + 1;

                    tv_subcat_content.setText(String.valueOf(qty2));

                    if (qty2 == 0) {
                        tv_add.setText(R.string.tv_pro_add);
                    }
                    break;
                case R.id.tv_add:
                    final Product product = getItem(position);

                    final Stock stock = (Stock) spinner_quantity.getSelectedItem();
                    final int qty3 = Integer.parseInt(tv_subcat_content.getText().toString().trim());

                    if (qty3 > 0) {
                        service.getStockAvailability(product.getProduct_id()).enqueue(new Callback<Quantity>() {
                            @Override
                            public void onResponse(Call<Quantity> call, Response<Quantity> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Quantity quantity = response.body();


                                    if (qty3 <= quantity.getQuantity_per_user()) {
                                        product.setStockId(stock.getStockId());
                                        product.setStocks(new Gson().toJson(product.getCustom_fields()));
                                        product.setQuantity(qty3);

                                        dbcart.setCart(product);
                                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                                    }

                                } else {
                                    Toast.makeText(context, R.string.connection_time_out, Toast.LENGTH_SHORT).show();
                                }

                                ((MainActivity) context).setCartCounter(String.valueOf(dbcart.getCartCount()));
                            }

                            @Override
                            public void onFailure(Call<Quantity> call, Throwable t) {
                                Toast.makeText(context, R.string.connection_time_out, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Product p = dbcart.getProduct(product.getProduct_id());

                        if (p != null) {
                            dbcart.removeItemFromCart(p.getId());
                        }

                        ((MainActivity) context).setCartCounter(String.valueOf(dbcart.getCartCount()));
                    }
                    break;
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
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(APIUrls.IMG_PRODUCT_URL + image.replace(" ", "%20"))
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