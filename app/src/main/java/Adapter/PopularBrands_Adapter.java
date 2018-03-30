package Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import Config.BaseURL;
import Model.PopularBrands_Model;
import codecanyon.grocery.R;

/**
 * Created by srikarn on 30-03-2018.
 */

public class PopularBrands_Adapter extends RecyclerView.Adapter<PopularBrands_Adapter.MyViewHolder> {

    private List<PopularBrands_Model> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.popularbrands);
        }
    }

    public PopularBrands_Adapter(List<PopularBrands_Model> modelList){
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.popularbrands, parent, false);

        context = parent.getContext();
        return new PopularBrands_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PopularBrands_Model mList = modelList.get(position);
        Log.v("Popular Brands",BaseURL.IMG_Brand_URL+mList.getImage()  );

        Glide.with(context)
                .load(BaseURL.IMG_Brand_URL+mList.getImage())
                .placeholder(R.drawable.logonew)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }




}
