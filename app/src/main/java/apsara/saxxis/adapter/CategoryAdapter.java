package apsara.saxxis.adapter;

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

import apsara.saxxis.R;
import apsara.saxxis.models.Category;
import apsara.saxxis.reterofit.APIUrls;

/**
 * Created by srikarn on 23-03-2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private List<Category> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tv_caterogi_title);
            image = view.findViewById(R.id.iv_categori);
        }
    }

    public CategoryAdapter(List<Category> modelList) {
        this.modelList = modelList;
    }

    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);

        context = parent.getContext();
        return new CategoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category mList = modelList.get(position);

        Glide.with(context)
                .load(APIUrls.IMG_CATEGORY_URL + mList.getImage())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_logonew).diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.image);

        holder.title.setText(mList.getTitle());
    }


    @Override
    public int getItemCount() {
        return modelList.size();
    }

}

