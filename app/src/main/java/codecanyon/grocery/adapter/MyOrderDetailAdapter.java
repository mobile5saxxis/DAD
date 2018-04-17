package codecanyon.grocery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import codecanyon.grocery.R;
import codecanyon.grocery.models.MyOrderDetail;
import codecanyon.grocery.reterofit.APIUrls;

/**
 * Created by Rajesh Dabhi on 30/6/2017.
 */

public class MyOrderDetailAdapter extends RecyclerView.Adapter<MyOrderDetailAdapter.MyViewHolder> {

    private List<MyOrderDetail> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, tv_price, tv_qty;
        public ImageView iv_img;

        public MyViewHolder(View view) {
            super(view);
            tv_title = view.findViewById(R.id.tv_order_Detail_title);
            tv_price = view.findViewById(R.id.tv_order_Detail_price);
            tv_qty = view.findViewById(R.id.tv_order_Detail_qty);
            iv_img = view.findViewById(R.id.iv_order_detail_img);
        }
    }

    public MyOrderDetailAdapter(List<MyOrderDetail> modelList) {
        this.modelList = modelList;
    }

    @Override
    public MyOrderDetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_order_detail, parent, false);

        context = parent.getContext();

        return new MyOrderDetailAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyOrderDetailAdapter.MyViewHolder holder, int position) {
        MyOrderDetail mList = modelList.get(position);

        Glide.with(context)
                .load(APIUrls.IMG_PRODUCT_URL + mList.getProduct_image().split(",")[0].replace(" ", "%20"))
                .apply(RequestOptions.placeholderOf(R.drawable.ic_logonew).diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.iv_img);

        holder.tv_title.setText(mList.getProduct_name());
        holder.tv_price.setText(mList.getPrice());
        holder.tv_qty.setText(mList.getQty());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}