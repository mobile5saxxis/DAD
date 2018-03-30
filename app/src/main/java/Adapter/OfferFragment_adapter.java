package Adapter;

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

import java.util.List;

import Config.BaseURL;
import Model.Offers_model;
import codecanyon.grocery.R;

/**
 * Created by srikarn on 30-03-2018.
 */

public class OfferFragment_adapter extends RecyclerView.Adapter<OfferFragment_adapter.MyViewHolder>{
    private List<Offers_model> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_offer_title);
            image = (ImageView) view.findViewById(R.id.iv_offer);
        }
    }

    public OfferFragment_adapter(List<Offers_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public OfferFragment_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_offers_rv, parent, false);

        context = parent.getContext();
        return new OfferFragment_adapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull OfferFragment_adapter.MyViewHolder holder, int position) {
        Offers_model mList = modelList.get(position);

        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL+mList.getProduct_image())
                .placeholder(R.drawable.logonew)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);

        holder.title.setText(mList.getProduct_name());
    }


    @Override
    public int getItemCount () {
        return modelList.size();
    }
}
