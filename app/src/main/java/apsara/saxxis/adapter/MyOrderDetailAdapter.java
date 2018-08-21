package apsara.saxxis.adapter;

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

import apsara.saxxis.R;
import apsara.saxxis.models.MyOrderDetail;
import apsara.saxxis.reterofit.APIUrls;

/**
 * Created by Rajesh Dabhi on 30/6/2017.
 */

public class MyOrderDetailAdapter extends CommonRecyclerAdapter<MyOrderDetail> {

    private Context context;

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_order_detail, parent, false);
        return new MyOderDetailViewHolder(itemView);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        MyOderDetailViewHolder viewHolder = (MyOderDetailViewHolder) holder;
        viewHolder.bind(position);
    }

    public class MyOderDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title, tv_price, tv_qty;
        private ImageView iv_img;

        private MyOderDetailViewHolder(View view) {
            super(view);
            tv_title = view.findViewById(R.id.tv_order_Detail_title);
            tv_price = view.findViewById(R.id.tv_order_Detail_price);
            tv_qty = view.findViewById(R.id.tv_order_Detail_qty);
            iv_img = view.findViewById(R.id.iv_order_detail_img);
        }

        public void bind(int position) {
            MyOrderDetail myOrderDetail = getItem(position);
            String imageUrl = "";

            if (myOrderDetail.getProduct_image() != null) {
                String[] value = myOrderDetail.getProduct_image().split(",");

                if (value.length > 0) {
                    imageUrl = APIUrls.IMG_PRODUCT_URL + value[0].replace(" ", "%20");
                }
            }

            Glide.with(context)
                    .load(imageUrl)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_placeholder).diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(iv_img);

            tv_title.setText(myOrderDetail.getProduct_name());
            tv_price.setText(myOrderDetail.getFinal_amount());
            tv_qty.setText(myOrderDetail.getQty());
        }
    }
}