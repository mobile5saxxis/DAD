package Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Config.BaseURL;
import Model.Product_model;
import codecanyon.grocery.MainActivity;
import codecanyon.grocery.ProductDetailsActivity;
import codecanyon.grocery.R;
import util.DatabaseHandler;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class Product_adapter extends RecyclerView.Adapter<Product_adapter.MyViewHolder>
        implements Filterable {


    private static String TAG = Product_adapter.class.getSimpleName();


    private List<Product_model> modelList;
    private List<Product_model> mFilteredList;
    private Context context;
    private DatabaseHandler dbcart;
    String [] list_product;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_title, tv_price, tv_total, tv_contetiy, tv_add;
        public ImageView iv_logo, iv_plus, iv_minus, iv_remove;
        public Spinner iv_spinner;

        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_subcat_title);
            tv_price = (TextView) view.findViewById(R.id.tv_subcat_price);
            tv_total = (TextView) view.findViewById(R.id.tv_subcat_total);
            tv_contetiy = (TextView) view.findViewById(R.id.tv_subcat_contetiy);
            tv_add = (TextView) view.findViewById(R.id.tv_subcat_add);
            iv_logo = (ImageView) view.findViewById(R.id.iv_subcat_img);
            iv_plus = (ImageView) view.findViewById(R.id.iv_subcat_plus);
            iv_minus = (ImageView) view.findViewById(R.id.iv_subcat_minus);
            iv_remove = (ImageView) view.findViewById(R.id.iv_subcat_remove);
            iv_spinner = (Spinner) view.findViewById(R.id.tv_spinner);

/*
            modelList.get(getAdapterPosition()).getQuantity() + "-" + modelList.get(getAdapterPosition()).getPrice_val()
*/

           /* String drop1= modelList.get(getAdapterPosition()).getQuantity() + "-" + modelList.get(getAdapterPosition()).getPrice_val();
            String drop2= modelList.get(getAdapterPosition()).getQuantity() + "-" + modelList.get(getAdapterPosition()).getPrice_val();
            String drop3= modelList.get(getAdapterPosition()).getQuantity() + "-" + modelList.get(getAdapterPosition()).getPrice_val();
           // String [] list_product ={"1kg", "2Kg", "3kg"};
            String [] list_product ={drop1,drop2, drop3};
            ArrayAdapter<String>adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item,list_product);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            iv_spinner.setAdapter(adapter);*/

            iv_remove.setVisibility(View.GONE);

            iv_minus.setOnClickListener(this);
            iv_plus.setOnClickListener(this);
            tv_add.setOnClickListener(this);
            iv_logo.setOnClickListener(this);

            CardView cardView = (CardView) view.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();


            //int qty = Integer.valueOf(tv_contetiy.getText().toString());
            if (id == R.id.iv_subcat_plus) {

              int  qty = Integer.valueOf(tv_contetiy.getText().toString());
                qty = qty + 1;

                tv_contetiy.setText(String.valueOf(qty));

            }else if(id == R.id.tv_spinner){
                int  qty = Integer.valueOf(tv_contetiy.getText().toString());
                qty = qty + 1;

                tv_contetiy.setText(String.valueOf(qty));

            } else if (id == R.id.iv_subcat_minus) {

              int   qty = 0;
                if (!tv_contetiy.getText().toString().equalsIgnoreCase(""))
                    qty = Integer.valueOf(tv_contetiy.getText().toString());

                if (qty > 0) {
                    qty = qty - 1;
                    tv_contetiy.setText(String.valueOf(qty));
                }

            } else if (id == R.id.tv_subcat_add) {

                HashMap<String, String> map = new HashMap<>();

                map.put("product_id", modelList.get(position).getProduct_id());
                map.put("category_id", modelList.get(position).getCategory_id());
                map.put("product_image", modelList.get(position).getProduct_image());
                map.put("increament", modelList.get(position).getIncreament());
                map.put("product_name", modelList.get(position).getProduct_name());

                map.put("price", modelList.get(position).getPrice());
                map.put("stock", modelList.get(position).getIn_stock());
                map.put("title", modelList.get(position).getTitle());
                map.put("unit", modelList.get(position).getUnit());

                map.put("unit_value", modelList.get(position).getUnit_value());

                if (!tv_contetiy.getText().toString().equalsIgnoreCase("0")) {

                    if (dbcart.isInCart(map.get("product_id"))) {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                    } else {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                    }
                } else {
                    dbcart.removeItemFromCart(map.get("product_id"));
                    tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
                }

                Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("product_id")));
                Double price = Double.parseDouble(map.get("price"));

                tv_total.setText("" + price * items);
                ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());

            } else if (id == R.id.iv_subcat_img) {
                showImage(modelList.get(position).getProduct_image());
            } else if (id == R.id.card_view) {
               /* showProductDetail(modelList.get(position).getProduct_image(),
                        modelList.get(position).getTitle(),
                        modelList.get(position).getProduct_description(),
                        modelList.get(position).getProduct_name(),
                        position, tv_contetiy.getText().toString(), modelList.get(position).getPrice(), modelList.get(position).getUnit());*/

                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("selectedProduct", modelList.get(position));
                intent.putExtra("total",tv_total.getText().toString() );
                context.startActivity(intent);
            }

        }

    }


    public Product_adapter(List<Product_model> modelList, Context context) {
        this.modelList = modelList;
        this.mFilteredList = modelList;
        this.context = context;

        dbcart = new DatabaseHandler(context);
    }

    @Override
    public Product_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_product_rv, parent, false);

        context = parent.getContext();

        return new Product_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Product_adapter.MyViewHolder holder, int position) {
        final Product_model mList = modelList.get(position);

        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + mList.getProduct_image())
                .centerCrop()
                .placeholder(R.drawable.logonew)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.iv_logo);

        holder.tv_title.setText(mList.getProduct_name());
        holder.tv_price.setText(context.getResources().getString(R.string.tv_pro_price) + mList.getUnit_value() + " " +
                mList.getUnit() + " " + context.getResources().getString(R.string.currency) + " " + mList.getPrice());

        if (dbcart.isInCart(mList.getProduct_id())) {
            holder.tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
            holder.tv_contetiy.setText(dbcart.getCartItemQty(mList.getProduct_id()));
        } else {
            holder.tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
        }

        Double items = Double.parseDouble(dbcart.getInCartItemQty(mList.getProduct_id()));
        Double price = Double.parseDouble(mList.getPrice());
        Log.v("price", mList.getPrice());

        holder.tv_total.setText("" + price * items);
        String drop1= mList.getUnit() + "- Rs " + mList.getPrice();
        String drop2= mList.getQuantity()  + "- Rs " + mList.getPrice_val();
        String drop3= mList.getQuantity() + "- Rs " + mList.getPrice_val();
        // String [] list_product ={"1kg", "2Kg", "3kg"};
        list_product = new String[]{"select quantity", drop1, drop2, drop3};
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item,list_product);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.iv_spinner.setAdapter(adapter);
        holder.iv_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
               switch (position){
                   case 0:
                       int  qty = Integer.valueOf(holder.tv_contetiy.getText().toString());
                       qty = 0;

                       holder.tv_contetiy.setText(String.valueOf(qty));
                       break;
                   case 1:

                         qty = Integer.valueOf(holder.tv_contetiy.getText().toString());
                         if(qty == 0) {
                             qty = qty + 1;
                         } else {
                             qty = 1;
                         }

                       holder.tv_contetiy.setText(String.valueOf(qty));
                       break;

                   case 2:
                         qty = Integer.valueOf(holder.tv_contetiy.getText().toString());
                       if(qty == 0) {
                           qty = qty + 2;
                       } else {
                           qty = 2;
                       }

                       holder.tv_contetiy.setText(String.valueOf(qty));
                       break;

                   case 3:
                       qty = Integer.valueOf(holder.tv_contetiy.getText().toString());
                       if(qty == 0) {
                           qty = qty + 3;
                       } else {
                           qty = 3;
                       }

                       holder.tv_contetiy.setText(String.valueOf(qty));
                       break;

               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = modelList;
                } else {

                    ArrayList<Product_model> filteredList = new ArrayList<>();

                    for (Product_model androidVersion : modelList) {

                        if (androidVersion.getProduct_name().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Product_model>) filterResults.values;
                notifyDataSetChanged();

            }
        };
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
                .load(BaseURL.IMG_PRODUCT_URL + image.split(",")[0])
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

    private void showProductDetail(String image, String title, String description, String detail, final int position, String qty, String price, String quantity) {// showProductDetail(modelList.get(position).getProduct_image(),
       /* modelList.get(position).getTitle(),
                modelList.get(position).getProduct_description(),
                modelList.get(position).getProduct_name(),
                position, tv_contetiy.getText().toString());*/

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_product_detail);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        ImageView iv_image = (ImageView) dialog.findViewById(R.id.iv_product_detail_img);
        ImageView iv_minus = (ImageView) dialog.findViewById(R.id.iv_subcat_minus);
        ImageView iv_plus = (ImageView) dialog.findViewById(R.id.iv_subcat_plus);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_product_detail_title);
        TextView tv_name = (TextView) dialog.findViewById(R.id.product_name);
        TextView tv_quantity = (TextView) dialog.findViewById(R.id.quantity);
        TextView tv_price = (TextView) dialog.findViewById(R.id.price);
        TextView tv_detail = (TextView) dialog.findViewById(R.id.tv_product_detail);
        final TextView tv_contetiy = (TextView) dialog.findViewById(R.id.tv_subcat_contetiy);
        final TextView tv_add = (TextView) dialog.findViewById(R.id.tv_subcat_add);

        tv_title.setText(title);
        tv_detail.setText(detail);
        tv_contetiy.setText(qty);
        tv_name.setText(detail);
        tv_quantity.setText(quantity);
        tv_detail.setText(description);
        tv_price.setText(price);

        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + image)
                .centerCrop()
                .placeholder(R.drawable.logonew)
                .crossFade()
                .into(iv_image);

        if (dbcart.isInCart(modelList.get(position).getProduct_id())) {
            tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
            tv_contetiy.setText(dbcart.getCartItemQty(modelList.get(position).getProduct_id()));
        } else {
            tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
        }

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> map = new HashMap<>();

                map.put("product_id", modelList.get(position).getProduct_id());
                map.put("category_id", modelList.get(position).getCategory_id());
                map.put("product_image", modelList.get(position).getProduct_image());
                map.put("increament", modelList.get(position).getIncreament());
                map.put("product_name", modelList.get(position).getProduct_name());

                map.put("price", modelList.get(position).getPrice());
                map.put("stock", modelList.get(position).getIn_stock());
                map.put("title", modelList.get(position).getTitle());
                map.put("unit", modelList.get(position).getUnit());

                map.put("unit_value", modelList.get(position).getUnit_value());

                if (!tv_contetiy.getText().toString().equalsIgnoreCase("0")) {

                    if (dbcart.isInCart(map.get("product_id"))) {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                    } else {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                    }
                } else {
                    dbcart.removeItemFromCart(map.get("product_id"));
                    tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
                }

                Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("product_id")));
                Double price = Double.parseDouble(map.get("price"));

                ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());

                notifyItemChanged(position);

            }
        });

        iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.valueOf(tv_contetiy.getText().toString());
                qty = qty + 1;

                tv_contetiy.setText(String.valueOf(qty));
            }
        });

        iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = 0;
                if (!tv_contetiy.getText().toString().equalsIgnoreCase(""))
                    qty = Integer.valueOf(tv_contetiy.getText().toString());

                if (qty > 0) {
                    qty = qty - 1;
                    tv_contetiy.setText(String.valueOf(qty));
                }
            }
        });

    }


}