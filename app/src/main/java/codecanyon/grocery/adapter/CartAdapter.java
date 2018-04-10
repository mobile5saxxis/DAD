package codecanyon.grocery.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import codecanyon.grocery.R;
import codecanyon.grocery.models.Product;
import codecanyon.grocery.reterofit.APIUrls;
import codecanyon.grocery.util.DatabaseHandler;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ProductHolder> {
    private List<Product> list;
    private Activity activity;
    private DatabaseHandler dbHandler;

    public CartAdapter(Activity activity, List<Product> list) {
        this.list = list;
        this.activity = activity;

        dbHandler = new DatabaseHandler();
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);

        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductHolder holder, final int position) {
        final Product product = list.get(position);


        Glide.with(activity)
                .load(APIUrls.IMG_PRODUCT_URL + product.getProduct_image())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_logonew).diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.iv_logo);

        holder.tv_title.setText(product.getProduct_name());
//        holder.tv_price.setText(String.format("%s%s %s %s %s", activity.getResources().getString(R.string.tv_pro_price), product.get("unit_value"), product.get("unit"), activity.getResources().getString(R.string.currency), product.get("price")));
        holder.tv_contetiy.setText(String.valueOf(product.getQuantity()));

//        Double items = Double.parseDouble(dbHandler.getInCartItemQty(product.get("product_id")));
//        Double price = Double.parseDouble(product.get("price"));
//
//        holder.tv_total.setText(String.format("%s", price * items));

        holder.iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = 0;
                if (!holder.tv_contetiy.getText().toString().equalsIgnoreCase(""))
                    qty = Integer.valueOf(holder.tv_contetiy.getText().toString());

                if (qty > 0) {
                    qty = qty - 1;
                    holder.tv_contetiy.setText(String.valueOf(qty));
                }

                if (holder.tv_contetiy.getText().toString().equalsIgnoreCase("0")) {
                    dbHandler.removeItemFromCart(product.getProduct_id());
                    list.remove(position);
                    notifyDataSetChanged();
                    updateintent();
                }
            }
        });

        holder.iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int qty = Integer.valueOf(holder.tv_contetiy.getText().toString());
                qty = qty + 1;

                holder.tv_contetiy.setText(String.valueOf(qty));
            }
        });

        holder.tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                product.setQuantity(Integer.valueOf(holder.tv_contetiy.getText().toString()));
                dbHandler.setCart(product);

//                Double items = Double.parseDouble(dbHandler.getInCartItemQty(product.get("product_id")));
//                Double price = Double.parseDouble(product.get("price"));
//
//                holder.tv_total.setText("" + price * items);
                //holder.tv_subcat_total.setText(activity.getResources().getString(R.string.tv_cart_total) + price * items + " " +activity.getResources().getString(R.string.currency));
                updateintent();
            }
        });

        holder.iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHandler.removeItemFromCart(product.getProduct_id());
                list.remove(position);
                notifyDataSetChanged();

                updateintent();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        private TextView tv_title, tv_price,  tv_contetiy, tv_add,
                tv_unit, tv_unit_value;
        private ImageView iv_logo, iv_plus, iv_minus, iv_remove;

        private ProductHolder(View view) {
            super(view);

            tv_title = view.findViewById(R.id.tv_subcat_title);
            tv_price = view.findViewById(R.id.tv_subcat_price);
            tv_contetiy = view.findViewById(R.id.tv_subcat_content);
            tv_add = view.findViewById(R.id.tv_subcat_add);
            iv_logo = view.findViewById(R.id.iv_subcat);
            iv_plus = view.findViewById(R.id.iv_subcat_plus);
            iv_minus = view.findViewById(R.id.iv_subcat_minus);
            iv_remove = view.findViewById(R.id.iv_subcat_remove);

            tv_add.setText(R.string.tv_pro_update);

        }
    }

    private void updateintent() {
        Intent updates = new Intent("Grocery_cart");
        updates.putExtra("type", "update");
        activity.sendBroadcast(updates);
    }

}

