package codecanyon.grocery.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;

import codecanyon.grocery.R;
import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.activities.ProductDetailsActivity;
import codecanyon.grocery.models.Product;
import codecanyon.grocery.reterofit.APIUrls;
import codecanyon.grocery.util.DatabaseHandler;

import static codecanyon.grocery.util.DatabaseHandler.COLUMN_IMAGE;
import static codecanyon.grocery.util.DatabaseHandler.COLUMN_INCREMENT;
import static codecanyon.grocery.util.DatabaseHandler.COLUMN_NAME;
import static codecanyon.grocery.util.DatabaseHandler.COLUMN_PRICE;
import static codecanyon.grocery.util.DatabaseHandler.COLUMN_STOCK;
import static codecanyon.grocery.util.DatabaseHandler.COLUMN_TITLE;
import static codecanyon.grocery.util.DatabaseHandler.COLUMN_UNIT;
import static codecanyon.grocery.util.DatabaseHandler.COLUMN_UNIT_VALUE;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class ProductAdapter extends CommonRecyclerAdapter<Product> {

    private Context context;
    private DatabaseHandler dbcart;

    public ProductAdapter(Context context) {
        this.context = context;
        dbcart = new DatabaseHandler(context);
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
                .placeholder(R.drawable.logonew)
                .error(R.drawable.logonew)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(APIUrls.IMG_PRODUCT_URL + image.split(",")[0])
                .apply(requestOptions)
                .into(iv_image);

        iv_image_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

//    private void showProductDetail(String image, String title, String description, String detail, final int position, String qty, String price, String quantity) {// showProductDetail(modelList.get(position).getProduct_image(),
//       /* modelList.get(position).getTitle(),
//                modelList.get(position).getProduct_description(),
//                modelList.get(position).getProduct_name(),
//                position, tv_subcat_content.getText().toString());*/
//
//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_product_detail);
//        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        dialog.show();
//
//        ImageView iv_image = dialog.findViewById(R.id.iv_product_detail_img);
//        ImageView iv_minus = dialog.findViewById(R.id.iv_subcat_minus);
//        ImageView iv_plus = dialog.findViewById(R.id.iv_subcat_plus);
//        TextView tv_title = dialog.findViewById(R.id.tv_product_detail_title);
//        TextView tv_name = dialog.findViewById(R.id.product_name);
//        TextView tv_quantity = dialog.findViewById(R.id.quantity);
//        TextView tv_price = dialog.findViewById(R.id.price);
//        TextView tv_detail = dialog.findViewById(R.id.tv_product_detail);
//        final TextView tv_contetiy = dialog.findViewById(R.id.tv_subcat_content);
//        final TextView tv_add = dialog.findViewById(R.id.tv_subcat_add);
//
//        tv_title.setText(title);
//        tv_detail.setText(detail);
//        tv_contetiy.setText(qty);
//        tv_name.setText(detail);
//        tv_quantity.setText(quantity);
//        tv_detail.setText(description);
//        tv_price.setText(price);
//
//        RequestOptions requestOptions = new RequestOptions()
//                .placeholder(R.drawable.logonew)
//                .error(R.drawable.logonew)
//                .diskCacheStrategy(DiskCacheStrategy.ALL);
//
//        Glide.with(context)
//                .load(APIUrls.IMG_PRODUCT_URL + image)
//                .apply(requestOptions)
//                .into(iv_image);
//
//        if (dbcart.isInCart(modelList.get(position).getProduct_id())) {
//            tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
//            tv_contetiy.setText(dbcart.getCartItemQty(modelList.get(position).getProduct_id()));
//        } else {
//            tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
//        }
//
//        tv_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                HashMap<String, String> map = new HashMap<>();
//
//                map.put("product_id", modelList.get(position).getProduct_id());
//                map.put("category_id", modelList.get(position).getCategory_id());
//                map.put("product_image", modelList.get(position).getProduct_image());
//                map.put("increament", modelList.get(position).getIncreament());
//                map.put("product_name", modelList.get(position).getProduct_name());
//
//                map.put("price", modelList.get(position).getPrice());
//                map.put("stock", modelList.get(position).getIn_stock());
//                map.put("tv_subcat_title", modelList.get(position).getTitle());
//                map.put("unit", modelList.get(position).getUnit());
//
//                map.put("unit_value", modelList.get(position).getUnit_value());
//
//                if (!tv_contetiy.getText().toString().equalsIgnoreCase("0")) {
//
//                    if (dbcart.isInCart(map.get("product_id"))) {
//                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
//                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
//                    } else {
//                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
//                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
//                    }
//                } else {
//                    dbcart.removeItemFromCart(map.get("product_id"));
//                    tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
//                }
//
//                Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("product_id")));
//                Double price = Double.parseDouble(map.get("price"));
//
//                ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());
//
//                notifyItemChanged(position);
//
//            }
//        });
//
//        iv_plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int qty = Integer.valueOf(tv_contetiy.getText().toString());
//                qty = qty + 1;
//
//                tv_contetiy.setText(String.valueOf(qty));
//            }
//        });
//
//        iv_minus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int qty = 0;
//                if (!tv_contetiy.getText().toString().equalsIgnoreCase(""))
//                    qty = Integer.valueOf(tv_contetiy.getText().toString());
//
//                if (qty > 0) {
//                    qty = qty - 1;
//                    tv_contetiy.setText(String.valueOf(qty));
//                }
//            }
//        });
//
//    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_subcat_title, tv_subcat_price, tv_subcat_total, tv_subcat_content, tv_subcat_add;
        public ImageView iv_subcat, iv_subcat_plus, iv_subcat_minus, iv_subcat_remove;
        public Spinner spinner_subcat;

        public ProductViewHolder(View view) {
            super(view);
            tv_subcat_title = view.findViewById(R.id.tv_subcat_title);
            tv_subcat_price = view.findViewById(R.id.tv_subcat_price);
            tv_subcat_total = view.findViewById(R.id.tv_subcat_total);
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
                    break;
                case R.id.tv_subcat_add:
                    Product product = getItem(position);

                    HashMap<String, String> map = new HashMap<>();

                    map.put(DatabaseHandler.COLUMN_ID, product.getProduct_id());
                    map.put(DatabaseHandler.COLUMN_CAT_ID, product.getCategory_id());
                    map.put(COLUMN_IMAGE, product.getProduct_image());
                    map.put(COLUMN_INCREMENT, product.getIncreament());
                    map.put(COLUMN_NAME, product.getProduct_name());

                    map.put(COLUMN_PRICE, product.getPrice());
                    map.put(COLUMN_STOCK, product.getIn_stock());
                    map.put(COLUMN_TITLE, product.getTitle());
                    map.put(COLUMN_UNIT, product.getUnit());

                    map.put(COLUMN_UNIT_VALUE, product.getUnit_value());

                    if (!tv_subcat_content.getText().toString().equalsIgnoreCase("0")) {

                        if (dbcart.isInCart(map.get("product_id"))) {
                            dbcart.setCart(map, Float.valueOf(tv_subcat_content.getText().toString()));
                            tv_subcat_add.setText(context.getResources().getString(R.string.tv_pro_update));
                        } else {
                            dbcart.setCart(map, Float.valueOf(tv_subcat_content.getText().toString()));
                            tv_subcat_add.setText(context.getResources().getString(R.string.tv_pro_update));
                        }
                    } else {
                        dbcart.removeItemFromCart(map.get("product_id"));
                        tv_subcat_add.setText(context.getResources().getString(R.string.tv_pro_add));
                    }

                    Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("product_id")));
                    Double price = Double.parseDouble(map.get("price"));

                    tv_subcat_total.setText(String.format("%s", price * items));
                    ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());

                    break;
                case R.id.iv_subcat:
                    Product product1 = getItem(position);

                    showImage(product1.getProduct_image());
                    break;
                case R.id.rl_product:
                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("selectedProduct", getItem(position));
                    intent.putExtra("total", tv_subcat_total.getText().toString());
                    context.startActivity(intent);
                    break;
            }
        }

        public void bindData(int position) {
            final Product product = getItem(position);

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.logonew)
                    .error(R.drawable.logonew)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(context)
                    .load(APIUrls.IMG_PRODUCT_URL + product.getProduct_image())
                    .apply(requestOptions)
                    .into(iv_subcat);

            tv_subcat_title.setText(product.getProduct_name());
            tv_subcat_price.setText(String.format("%s%s %s %s %s", context.getResources().getString(R.string.tv_pro_price), product.getUnit_value(), product.getUnit(), context.getResources().getString(R.string.currency), product.getPrice()));

            if (dbcart.isInCart(product.getProduct_id())) {
                tv_subcat_add.setText(context.getResources().getString(R.string.tv_pro_update));
                tv_subcat_content.setText(dbcart.getCartItemQty(product.getProduct_id()));
            } else {
                tv_subcat_add.setText(context.getResources().getString(R.string.tv_pro_add));
            }

            Double items = Double.parseDouble(dbcart.getInCartItemQty(product.getProduct_id()));
            Double price = Double.parseDouble(product.getPrice());
            Log.v("price", product.getPrice());

            tv_subcat_total.setText(String.format("%s", price * items));
            String drop1 = product.getUnit() + "- Rs " + product.getPrice();
            String drop2 = product.getQuantity() + "- Rs " + product.getPrice_val();
            String drop3 = product.getQuantity() + "- Rs " + product.getPrice_val();

//            list_product = new String[]{"select tv_quantity", drop1, drop2, drop3};
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
//                    android.R.layout.simple_spinner_item, list_product);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            holder.iv_spinner.setAdapter(adapter);
//            holder.iv_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                    switch (position) {
//                        case 0:
//                            int qty = Integer.valueOf(holder.tv_contetiy.getText().toString());
//                            qty = 0;
//
//                            holder.tv_contetiy.setText(String.valueOf(qty));
//                            break;
//                        case 1:
//
//                            qty = Integer.valueOf(holder.tv_contetiy.getText().toString());
//                            if (qty == 0) {
//                                qty = qty + 1;
//                            } else {
//                                qty = 1;
//                            }
//
//                            holder.tv_contetiy.setText(String.valueOf(qty));
//                            break;
//
//                        case 2:
//                            qty = Integer.valueOf(holder.tv_contetiy.getText().toString());
//                            if (qty == 0) {
//                                qty = qty + 2;
//                            } else {
//                                qty = 2;
//                            }
//
//                            holder.tv_contetiy.setText(String.valueOf(qty));
//                            break;
//
//                        case 3:
//                            qty = Integer.valueOf(holder.tv_contetiy.getText().toString());
//                            if (qty == 0) {
//                                qty = qty + 3;
//                            } else {
//                                qty = 3;
//                            }
//
//                            holder.tv_contetiy.setText(String.valueOf(qty));
//                            break;
//
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
        }
    }
}