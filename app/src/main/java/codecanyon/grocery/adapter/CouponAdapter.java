package codecanyon.grocery.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import codecanyon.grocery.R;
import codecanyon.grocery.models.Coupon;

public class CouponAdapter extends CommonRecyclerAdapter<Coupon> {
    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_coupon, parent, false);
        return new CouponViewHolder(view);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        CouponViewHolder couponViewHolder = (CouponViewHolder) holder;
        couponViewHolder.bindData(position);
    }

    private class CouponViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_off_value, tv_min_amount, tv_coupon_title, tv_dates, tv_number_of_times;

        private CouponViewHolder(View view) {
            super(view);
            tv_off_value = view.findViewById(R.id.tv_off_value);
            tv_min_amount = view.findViewById(R.id.tv_min_amount);
            tv_coupon_title = view.findViewById(R.id.tv_coupon_title);
            tv_dates = view.findViewById(R.id.tv_dates);
            tv_number_of_times = view.findViewById(R.id.tv_number_of_times);
        }

        private void bindData(int position) {
            Coupon coupon = getItem(position);

            tv_off_value.setText(coupon.getCoupon_value());
            tv_min_amount.setText(String.format("*Minimum order %s and above", coupon.getMinimum_order_Amount()));
            tv_coupon_title.setText(coupon.getCoupon_title());
            tv_dates.setText(String.format("%s - %s", coupon.getFrom_Date(), coupon.getTo_Date()));
            tv_number_of_times.setText(String.format("%s time(s) per user", coupon.getTimes_Per_user()));
        }
    }
}
