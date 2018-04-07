package codecanyon.grocery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import codecanyon.grocery.R;
import codecanyon.grocery.models.MyOrder;

/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class MyOderAdapter extends RecyclerView.Adapter<MyOderAdapter.MyViewHolder> {

    private List<MyOrder> modelList;

    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_orderno, tv_status, tv_date, tv_time, tv_price, tv_item;

        public MyViewHolder(View view) {
            super(view);
            tv_orderno = view.findViewById(R.id.tv_order_no);
            tv_status = view.findViewById(R.id.tv_order_status);
            tv_date = view.findViewById(R.id.tv_order_date);
            tv_time = view.findViewById(R.id.tv_order_time);
            tv_price = view.findViewById(R.id.tv_order_price);
            tv_item = view.findViewById(R.id.tv_order_item);
        }
    }

    public MyOderAdapter(List<MyOrder> modelList) {
        this.modelList = modelList;
    }

    @Override
    public MyOderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_order, parent, false);

        context = parent.getContext();

        return new MyOderAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyOderAdapter.MyViewHolder holder, int position) {
        MyOrder mList = modelList.get(position);

        holder.tv_orderno.setText(context.getResources().getString(R.string.order_no) + mList.getSale_id());

        if (mList.getStatus().equals("0")) {
            holder.tv_status.setText(context.getResources().getString(R.string.pending));
        } else if (mList.getStatus().equals("1")) {
            holder.tv_status.setText(context.getResources().getString(R.string.confirm));
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.color_1));
        } else if (mList.getStatus().equals("2")) {
            holder.tv_status.setText(context.getResources().getString(R.string.delivered));
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.color_2));
        } else if (mList.getStatus().equals("3")) {
            holder.tv_status.setText(context.getResources().getString(R.string.cancle));
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.color_3));
        }

        holder.tv_date.setText(String.format("%s%s", context.getResources().getString(R.string.date), mList.getOn_date()));
        holder.tv_time.setText(String.format("%s%s-%s", context.getResources().getString(R.string.time), mList.getDelivery_time_from(), mList.getDelivery_time_to()));
        holder.tv_price.setText(String.format("%s%s", context.getResources().getString(R.string.currency), mList.getTotal_amount()));
        holder.tv_item.setText(String.format("%s%s", context.getResources().getString(R.string.tv_cart_item), mList.getTotal_items()));
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}
