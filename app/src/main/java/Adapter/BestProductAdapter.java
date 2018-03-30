package Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.HashMap;
import java.util.List;
import Config.BaseURL;
import Model.BestProducts_model;

import Model.Product_model;
import codecanyon.grocery.BestProductsDetails;
import codecanyon.grocery.MainActivity;
import codecanyon.grocery.ProductDetailsActivity;
import codecanyon.grocery.R;
import util.DatabaseHandler;


public class BestProductAdapter extends RecyclerView.Adapter<BestProductAdapter.MyViewHolder>{

    private List<BestProducts_model> modelList;

    private Context context;
    private DatabaseHandler dbcart;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, cost, quantity, button, tv_contetiy;
        public ImageView image, iv_plus, iv_minus;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            cost = (TextView) view.findViewById(R.id.cost);
            quantity = (TextView) view.findViewById(R.id.quantity);
            image = (ImageView) view.findViewById(R.id.thumbnail);
            button = (TextView) view.findViewById(R.id.addbutton);
            iv_plus = (ImageView) view.findViewById(R.id.iv_subcat_plus);
            iv_minus = (ImageView) view.findViewById(R.id.iv_subcat_minus);
            tv_contetiy = (TextView) view.findViewById(R.id.tv_subcat_contetiy);

            iv_minus.setOnClickListener(this);
            iv_plus.setOnClickListener(this);
            button.setOnClickListener(this);
            image.setOnClickListener(this);

            CardView cardView = (CardView) view.findViewById(R.id.card_view_bestproducts);
            cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();

            if (id == R.id.card_view_bestproducts) {
                Intent intent = new Intent(context, BestProductsDetails.class);
                intent.putExtra("position", position);
                intent.putExtra("selectedProduct", modelList.get(position));
                context.startActivity(intent);
            } else if (id == R.id.thumbnail) {
                showImage(modelList.get(position).getProduct_image());
            } else if (id == R.id.iv_subcat_minus) {

                int qty = 0;
                if (!tv_contetiy.getText().toString().equalsIgnoreCase(""))
                    qty = Integer.valueOf(tv_contetiy.getText().toString());

                if (qty > 0) {
                    qty = qty - 1;
                    tv_contetiy.setText(String.valueOf(qty));
                }

            }else if (id == R.id.iv_subcat_plus) {

                int  qty = Integer.valueOf(tv_contetiy.getText().toString());
                qty = qty + 1;

                tv_contetiy.setText(String.valueOf(qty));

            }
            else if (id == R.id.addbutton) {

                HashMap<String, String> map = new HashMap<>();

                map.put("product_id", modelList.get(position).getProduct_id());
                map.put("category_id", modelList.get(position).getCategory_id());
                map.put("product_image", modelList.get(position).getProduct_image());
                map.put("increament", modelList.get(position).getIncreament());
                map.put("product_name", modelList.get(position).getProduct_name());

                map.put("price", modelList.get(position).getPrice());
                map.put("stock", modelList.get(position).getIn_stock());
                map.put("title", modelList.get(position).getProduct_name());
                map.put("unit", modelList.get(position).getUnit());

                map.put("unit_value", modelList.get(position).getUnit_value());
                if (!tv_contetiy.getText().toString().equalsIgnoreCase("0")) {

                    if (dbcart.isInCart(map.get("product_id"))) {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        button.setText(context.getResources().getString(R.string.tv_pro_add));
                    } else {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        button.setText(context.getResources().getString(R.string.tv_pro_add));
                    }
                }else {
                    dbcart.removeItemFromCart(map.get("product_id"));
                    button.setText(context.getResources().getString(R.string.tv_pro_add));
                }

                ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());

            }
        }
    }
    public BestProductAdapter(List<BestProducts_model> modelList, Context context)  {
        this.modelList = modelList;
        this.context = context;
        dbcart = new DatabaseHandler(context);
    }

    @Override
    public BestProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bestproduct_fragment, parent, false);

        context = parent.getContext();

        return new BestProductAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BestProductAdapter.MyViewHolder holder, int position) {
        BestProducts_model mList = modelList.get(position);

        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL+mList.getProduct_image())
                .placeholder(R.drawable.logonew)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);

        holder.title.setText(mList.getProduct_name());
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

        ImageView iv_image_cancle = (ImageView) dialog.findViewById(R.id.iv_dialog_cancle);
        ImageView iv_image = (ImageView) dialog.findViewById(R.id.iv_dialog_img);

        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + image)
                .centerCrop()
                .placeholder(R.drawable.logonew)
                .crossFade()
                .into(iv_image);

        iv_image_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

}