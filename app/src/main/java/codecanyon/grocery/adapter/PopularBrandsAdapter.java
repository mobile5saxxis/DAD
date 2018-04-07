package codecanyon.grocery.adapter;

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
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import codecanyon.grocery.R;
import codecanyon.grocery.models.PopularBrands;
import codecanyon.grocery.reterofit.APIUrls;

/**
 * Created by srikarn on 30-03-2018.
 */

public class PopularBrandsAdapter extends RecyclerView.Adapter<PopularBrandsAdapter.MyViewHolder> {

    private List<PopularBrands> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.popularbrands);
        }
    }

    public PopularBrandsAdapter(List<PopularBrands> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.popularbrands, parent, false);

        context = parent.getContext();
        return new PopularBrandsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PopularBrands mList = modelList.get(position);
        Log.v("Popular Brands", APIUrls.IMG_Brand_URL + mList.getImage());

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.logonew)
                .error(R.drawable.logonew)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(APIUrls.IMG_Brand_URL + mList.getImage())
                .apply(requestOptions)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


}
