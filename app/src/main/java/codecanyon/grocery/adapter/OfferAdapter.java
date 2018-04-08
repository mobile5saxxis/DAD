package codecanyon.grocery.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.List;

import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.activities.OffersDetailsActivity;
import codecanyon.grocery.R;
import codecanyon.grocery.models.Offers;
import codecanyon.grocery.reterofit.APIUrls;
import codecanyon.grocery.util.DatabaseHandler;

/**
 * Created by srikarn on 26-03-2018.
 */

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {
    private List<Offers> modelList;
    private Context context;
    private DatabaseHandler dbcart;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_title, cost, quantity, button, tv_contetiy;
        public ImageView image, iv_plus, iv_minus;


        public ViewHolder(View view) {
            super(view);
            tv_title = view.findViewById(R.id.tv_title);
            cost = view.findViewById(R.id.cost);
            quantity = view.findViewById(R.id.quantity);
            image = view.findViewById(R.id.iv_image);
            button = view.findViewById(R.id.tv_add);
            iv_plus = view.findViewById(R.id.iv_subcat_plus);
            iv_minus = view.findViewById(R.id.iv_subcat_minus);
            tv_contetiy = view.findViewById(R.id.tv_subcat_content);

            iv_minus.setOnClickListener(this);
            iv_plus.setOnClickListener(this);
            button.setOnClickListener(this);
            image.setOnClickListener(this);

            CardView cardView = view.findViewById(R.id.card_view_bestproducts);
            cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();

            if (id == R.id.card_view_bestproducts) {
                Intent intent = new Intent(context, OffersDetailsActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("selectedProduct", modelList.get(position));
                context.startActivity(intent);
            } else if (id == R.id.iv_image) {
                showImage(modelList.get(position).getProduct_image());
            } else if (id == R.id.iv_subcat_minus) {

                int qty = 0;
                if (!tv_contetiy.getText().toString().equalsIgnoreCase(""))
                    qty = Integer.valueOf(tv_contetiy.getText().toString());

                if (qty > 0) {
                    qty = qty - 1;
                    tv_contetiy.setText(String.valueOf(qty));
                }

            } else if (id == R.id.iv_subcat_plus) {

                int qty = Integer.valueOf(tv_contetiy.getText().toString());
                qty = qty + 1;

                tv_contetiy.setText(String.valueOf(qty));

            } else if (id == R.id.tv_add) {

                HashMap<String, String> map = new HashMap<>();

                map.put("product_id", modelList.get(position).getProduct_id());
                map.put("category_id", modelList.get(position).getCategory_id());
                map.put("product_image", modelList.get(position).getProduct_image());
                map.put("increament", modelList.get(position).getIncreament());
                map.put("product_name", modelList.get(position).getProduct_name());

                map.put("price", modelList.get(position).getPrice());
                map.put("stock", modelList.get(position).getIn_stock());
                map.put("tv_subcat_title", modelList.get(position).getProduct_name());
                map.put("unit", modelList.get(position).getUnit());

                map.put("unit_value", modelList.get(position).getUnit_value());
                if (!tv_contetiy.getText().toString().equalsIgnoreCase("0")) {

                    if (dbcart.isInCart(map.get("product_id"))) {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        button.setText(context.getResources().getString(R.string.tv_pro_update));
                    } else {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        button.setText(context.getResources().getString(R.string.tv_pro_update));
                    }
                } else {
                    dbcart.removeItemFromCart(map.get("product_id"));
                    button.setText(context.getResources().getString(R.string.tv_pro_add));
                }

                ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());

            }
        }
    }


    public OfferAdapter(List<Offers> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
        dbcart = new DatabaseHandler(context);
    }

    @NonNull
    @Override
    public OfferAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bestproduct_fragment, parent, false);

        context = parent.getContext();
        return new OfferAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferAdapter.ViewHolder holder, int position) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.logonew)
                .error(R.drawable.logonew)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Offers mList = modelList.get(position);
        Glide.with(context)
                .load(APIUrls.IMG_PRODUCT_URL + mList.getProduct_image())
                .apply(requestOptions)
                .into(holder.image);

        holder.tv_title.setText(mList.getProduct_name());
        holder.cost.setText("RS");
        holder.cost.append(" " + mList.getPrice());
        holder.quantity.setText(mList.getUnit_value());
        holder.quantity.append(" " + "unit");

        if (dbcart.isInCart(mList.getProduct_id())) {
            holder.button.setText(context.getResources().getString(R.string.tv_pro_update));
            holder.tv_contetiy.setText(dbcart.getCartItemQty(mList.getProduct_id()));
        } else {
            holder.button.setText(context.getResources().getString(R.string.tv_pro_add));
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
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
                .placeholder(R.drawable.logonew)
                .error(R.drawable.logonew)
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