package codecanyon.grocery.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import codecanyon.grocery.R;
import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.activities.ProductDetailsActivity;
import codecanyon.grocery.models.Stock;
import codecanyon.grocery.models.Product;
import codecanyon.grocery.reterofit.APIUrls;
import codecanyon.grocery.util.DatabaseHandler;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class CartAdapter extends CommonRecyclerAdapter<Product> {
    private Context context;
    private DatabaseHandler dbcart;

    public CartAdapter(Context context) {
        this.context = context;
        dbcart = new DatabaseHandler();
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);

        return new ProductHolder(view);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        ProductHolder viewHolder = (ProductHolder) holder;
        viewHolder.bindData(position);
    }

    class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_subcat_title, tv_subcat_price, tv_subcat_content, tv_subcat_add, tv_subcat_discount_price;
        public ImageView iv_subcat, iv_subcat_plus, iv_subcat_minus, iv_subcat_remove;
        public Spinner spinner_subcat;

        private ProductHolder(View view) {
            super(view);

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

            view.findViewById(R.id.rl_product).setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();
            Product product = getItem(position);

            switch (id) {
                case R.id.iv_subcat_plus:
                    int qty = Integer.valueOf(tv_subcat_content.getText().toString());
                    qty = qty + 1;

                    tv_subcat_content.setText(String.valueOf(qty));
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
                        tv_subcat_content.setText(R.string.tv_pro_add);
                    }
                    break;
                case R.id.tv_subcat_add:

                    Stock stock = (Stock) spinner_subcat.getSelectedItem();
                    int quantity = Integer.parseInt(tv_subcat_content.getText().toString().trim());

                    if (quantity > 0) {
                        product.setStockId(stock.getStockId());
                        product.setStocks(new Gson().toJson(product.getCustom_fields()));
                        product.setQuantity(quantity);

                        dbcart.setCart(product);
                        tv_subcat_add.setText(context.getResources().getString(R.string.tv_pro_update));
                    } else {
                        Product p = dbcart.getProduct(product.getProduct_id());

                        if (p != null) {
                            dbcart.removeItemFromCart(p.getId());
                        }
                    }

                    ((MainActivity) context).setCartCounter(String.valueOf(dbcart.getCartCount()));

                    break;
                case R.id.rl_product:
                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    intent.putExtra("position", position);
//                    intent.putExtra("selectedProduct", getItem(position));
//                    intent.putExtra("total", tv_subcat_total.getText().toString());
                    context.startActivity(intent);
                    break;
            }
        }

        public void bindData(int position) {
            final Product product = getItem(position);
            List<Stock> stocks = new Gson().fromJson(product.getStocks(), new TypeToken<List<Stock>>() {
            }.getType());

            final PriceAdapter priceAdapter = new PriceAdapter(context, stocks);
            spinner_subcat.setAdapter(priceAdapter);

            spinner_subcat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Stock stock = priceAdapter.getItem(position);
                    tv_subcat_discount_price.setText(String.format("\u20B9 %s", stock.getStrikeprice()));
                    tv_subcat_price.setText(String.format("(\u20B9 %s)", stock.getPrice_val()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            for (int i = 0; i < stocks.size(); i++) {
                Stock stock1 = stocks.get(i);

                if (product.getStockId() == stock1.getStockId()) {
                    spinner_subcat.setSelection(i);
                    break;
                }
            }

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_logonew)
                    .error(R.drawable.ic_logonew)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(context)
                    .load(APIUrls.IMG_PRODUCT_URL + product.getProduct_image())
                    .apply(requestOptions)
                    .into(iv_subcat);

            tv_subcat_title.setText(product.getProduct_name());

            if (dbcart.isInCart(product.getProduct_id())) {
                tv_subcat_add.setText(context.getResources().getString(R.string.tv_pro_update));
                tv_subcat_content.setText(dbcart.getCartItemQty(product.getProduct_id()));
            } else {
                tv_subcat_add.setText(context.getResources().getString(R.string.tv_pro_add));
            }
        }
    }

    private void updateintent() {
        Intent updates = new Intent("Grocery_cart");
        updates.putExtra("type", "update");
        context.sendBroadcast(updates);
    }

}

