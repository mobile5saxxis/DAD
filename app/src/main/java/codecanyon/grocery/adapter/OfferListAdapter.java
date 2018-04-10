package codecanyon.grocery.adapter;

import android.content.Context;
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

import java.util.List;

import codecanyon.grocery.R;
import codecanyon.grocery.models.Offers;
import codecanyon.grocery.models.Product;
import codecanyon.grocery.reterofit.APIUrls;

/**
 * Created by srikarn on 30-03-2018.
 */

public class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.MyViewHolder> {
    private List<Product> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tv_offer_title);
            image = view.findViewById(R.id.iv_offer);
        }
    }

    public OfferListAdapter(List<Product> modelList) {
        this.modelList = modelList;
    }

    @Override
    public OfferListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_offers, parent, false);

        context = parent.getContext();
        return new OfferListAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull OfferListAdapter.MyViewHolder holder, int position) {
        Product mList = modelList.get(position);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_logonew)
                .error(R.drawable.ic_logonew)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(APIUrls.IMG_PRODUCT_URL + mList.getProduct_image())
                .apply(requestOptions)
                .into(holder.image);

        holder.title.setText(mList.getProduct_name());
    }


    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
