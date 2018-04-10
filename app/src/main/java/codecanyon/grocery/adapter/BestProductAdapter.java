package codecanyon.grocery.adapter;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;

import codecanyon.grocery.activities.BestProductsDetailsActivity;
import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.R;
import codecanyon.grocery.models.BestProducts;
import codecanyon.grocery.models.Product;
import codecanyon.grocery.reterofit.APIUrls;
import codecanyon.grocery.util.DatabaseHandler;


public class BestProductAdapter extends CommonRecyclerAdapter<Product> {

    private Context context;
    private DatabaseHandler dbcart;

    public BestProductAdapter(Context context) {
        this.context = context;
        dbcart = new DatabaseHandler();
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bestproduct_fragment, parent, false);

        return new BestProductViewHolder(itemView);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        BestProductViewHolder viewHolder = (BestProductViewHolder) holder;
        viewHolder.bind(position);
    }

    public class BestProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_title, tv_cost, tv_quantity, tv_add, tv_subcat_content;
        public ImageView iv_image, iv_subcat_plus, iv_subcat_minus;


        public BestProductViewHolder(View view) {
            super(view);
            tv_title = view.findViewById(R.id.tv_title);
            tv_cost = view.findViewById(R.id.cost);
            tv_quantity = view.findViewById(R.id.quantity);
            iv_image = view.findViewById(R.id.iv_image);
            tv_add = view.findViewById(R.id.tv_add);
            iv_subcat_plus = view.findViewById(R.id.iv_subcat_plus);
            iv_subcat_minus = view.findViewById(R.id.iv_subcat_minus);
            tv_subcat_content = view.findViewById(R.id.tv_subcat_content);

            iv_subcat_minus.setOnClickListener(this);
            iv_subcat_plus.setOnClickListener(this);
            tv_add.setOnClickListener(this);
            iv_image.setOnClickListener(this);

            CardView cardView = view.findViewById(R.id.card_view_bestproducts);
            cardView.setOnClickListener(this);

        }

        public void bind(int position) {
            Product product = getItem(position);

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_logonew)
                    .error(R.drawable.ic_logonew)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(context)
                    .load(APIUrls.IMG_PRODUCT_URL + product.getProduct_image())
                    .apply(requestOptions)
                    .into(iv_image);

            tv_title.setText(product.getProduct_name());
//            tv_cost.setText("RS " + product.getPrice());
//            tv_quantity.setText(product.getUnit_value());
            tv_quantity.append(" " + "unit");

            if (dbcart.isInCart(product.getProduct_id())) {
                tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                tv_subcat_content.setText(dbcart.getCartItemQty(product.getProduct_id()));
            } else {
                tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
            }
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();

            if (id == R.id.card_view_bestproducts) {
                Intent intent = new Intent(context, BestProductsDetailsActivity.class);
                intent.putExtra("position", position);
//                intent.putExtra("selectedProduct", getItem(position));
                context.startActivity(intent);
            } else if (id == R.id.iv_image) {
                showImage(getItem(position).getProduct_image());
            } else if (id == R.id.iv_subcat_minus) {

                int qty = 0;
                if (!tv_subcat_content.getText().toString().equalsIgnoreCase(""))
                    qty = Integer.valueOf(tv_subcat_content.getText().toString());

                if (qty > 0) {
                    qty = qty - 1;
                    tv_subcat_content.setText(String.valueOf(qty));
                }

            } else if (id == R.id.iv_subcat_plus) {

                int qty = Integer.valueOf(tv_subcat_content.getText().toString());
                qty = qty + 1;

                tv_subcat_content.setText(String.valueOf(qty));

            } else if (id == R.id.tv_add) {
                Product product = getItem(position);

                int qty = Integer.parseInt(tv_subcat_content.getText().toString().trim());

                if (qty > 1) {
                    product.setQuantity(qty);
                    dbcart.setCart(product);
                    tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                }

                ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());

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
                .placeholder(R.drawable.ic_logonew)
                .error(R.drawable.ic_logonew)
                .dontAnimate()
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