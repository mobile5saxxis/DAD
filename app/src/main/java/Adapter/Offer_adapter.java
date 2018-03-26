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
import Model.Category_model;
import Model.Offers_model;
import codecanyon.grocery.R;

/**
 * Created by srikarn on 26-03-2018.
 */

public class Offer_adapter extends RecyclerView.Adapter<Offer_adapter.ViewHolder> {
    private List<Offers_model> modelList;
    private Context context;

    public Offer_adapter(List<Offers_model> modelList) {
        this.modelList = modelList;
    }
    @NonNull
    @Override
    public Offer_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_categori_rv, parent, false);

        context = parent.getContext();
        return new Offer_adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Offer_adapter.ViewHolder holder, int position) {

        Offers_model mList = modelList.get(position);

        Glide.with(context)
                .load(BaseURL.IMG_CATEGORY_URL+mList.getProduct_image())
                .placeholder(R.drawable.logonew)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);

        holder.title.setText(mList.getProduct_name());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView image;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_caterogi_title);
            image = (ImageView) view.findViewById(R.id.iv_categori);
        }
    }
}
