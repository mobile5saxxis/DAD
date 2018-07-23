package apsara.saxxis.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import apsara.saxxis.R;
import apsara.saxxis.activities.MainActivity;
import apsara.saxxis.fragments.SubCategoryFragment;
import apsara.saxxis.models.Category;
import apsara.saxxis.reterofit.APIUrls;
import apsara.saxxis.reterofit.RetrofitInstance;
import apsara.saxxis.reterofit.RetrofitService;


/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class CategoryHomeAdapter extends CommonRecyclerAdapter<Category> {

    private MainActivity activity;
    private RequestOptions requestOptions;
    private Context context;

    public CategoryHomeAdapter(Activity activity) {
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate();
        this.activity = (MainActivity) activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_home, parent, false);
        return new CategoryHomeViewHolder(itemView);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        CategoryHomeViewHolder categoryVH = (CategoryHomeViewHolder) holder;
        categoryVH.bindData(position);
    }

    public class CategoryHomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_title;
        public ImageView iv_category;
        public View card_view;

        CategoryHomeViewHolder(View view) {
            super(view);
            tv_title = view.findViewById(R.id.tv_title);
            iv_category = view.findViewById(R.id.iv_category);
            card_view = view.findViewById(R.id.card_view);
            card_view.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        void bindData(int position) {
            Category category = getItem(position);

            Glide.with(context)
                    .load(APIUrls.IMG_CATEGORY_URL + category.getImage())
                    .apply(requestOptions)
                    .into(iv_category);

            tv_title.setText(category.getTitle());
        }

        @Override
        public void onClick(View v) {
            if (activity != null && !activity.isFinishing()) {
                int position = getAdapterPosition();

                Category category = getItem(position);

                String id = category.getId();
                String title = category.getTitle();

                FragmentManager fM = activity.getSupportFragmentManager();
                FragmentTransaction ft = fM.beginTransaction();

                ft.replace(R.id.frame_layout, SubCategoryFragment.newInstance(id, title), SubCategoryFragment.class.getSimpleName())
                        .addToBackStack(SubCategoryFragment.class.getSimpleName());
                ft.commit();
            }
        }
    }
}

