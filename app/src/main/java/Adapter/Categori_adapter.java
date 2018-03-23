package Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import Config.BaseURL;
import Model.Category_model;
import Model.Product_model;
import codecanyon.grocery.R;
import util.DatabaseHandler;

/**
 * Created by srikarn on 23-03-2018.
 */

public class Categori_adapter extends RecyclerView.Adapter<Categori_adapter.MyViewHolder> {
    private List<Category_model> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_caterogi_title);
            image = (ImageView) view.findViewById(R.id.iv_categori);
        }
    }

    public Categori_adapter(List<Category_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Categori_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_categori_rv, parent, false);

        context = parent.getContext();
        return new Categori_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category_model mList = modelList.get(position);

        Glide.with(context)
                .load(BaseURL.IMG_CATEGORY_URL+mList.getImage())
                .placeholder(R.drawable.logonew)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);

        holder.title.setText(mList.getTitle());
    }


    @Override
    public int getItemCount () {
        return modelList.size();
    }

    }

