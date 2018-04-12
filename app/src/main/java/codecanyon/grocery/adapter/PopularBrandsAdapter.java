package codecanyon.grocery.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import codecanyon.grocery.R;
import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.fragments.PopularBrandsFragment;
import codecanyon.grocery.models.PopularBrands;
import codecanyon.grocery.reterofit.APIUrls;
import codecanyon.grocery.reterofit.RetrofitInstance;
import codecanyon.grocery.reterofit.RetrofitService;

/**
 * Created by srikarn on 30-03-2018.
 */

public class PopularBrandsAdapter extends CommonRecyclerAdapter<PopularBrands> {

    private Context context;
    private MainActivity activity;

    public PopularBrandsAdapter(Activity activity) {
        this.activity = (MainActivity) activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_popular_brands, parent, false);

        context = parent.getContext();
        return new PopularBrandsViewHolder(itemView);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        PopularBrandsViewHolder vh = (PopularBrandsViewHolder) holder;
        vh.bindData(position);
    }


    public class PopularBrandsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iv_popular_brands;
        private RelativeLayout rl_popular_brand;

        private PopularBrandsViewHolder(View view) {
            super(view);
            iv_popular_brands = view.findViewById(R.id.iv_popular_brands);
            rl_popular_brand = view.findViewById(R.id.rl_popular_brand);
            rl_popular_brand.setOnClickListener(this);
            iv_popular_brands.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        private void bindData(int position) {
            PopularBrands mList = getItem(position);

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_logonew)
                    .error(R.drawable.ic_logonew)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(context)
                    .load(APIUrls.IMG_Brand_URL + mList.getImage())
                    .apply(requestOptions)
                    .into(iv_popular_brands);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            PopularBrands popularBrands = getItem(position);

            String id = popularBrands.getId();
            String title = popularBrands.getName();

            FragmentManager fM = activity.getSupportFragmentManager();
            FragmentTransaction ft = fM.beginTransaction();

            ft.replace(R.id.frame_layout, PopularBrandsFragment.newInstance(id, title), PopularBrandsFragment.class.getSimpleName())
                    .addToBackStack(PopularBrandsFragment.class.getSimpleName());
            ft.commit();
        }
    }
}
