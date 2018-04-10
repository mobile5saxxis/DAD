package codecanyon.grocery.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.List;

import codecanyon.grocery.adapter.BestProductAdapter;
import codecanyon.grocery.adapter.CategoryHomeAdapter;
import codecanyon.grocery.adapter.OfferAdapter;
import codecanyon.grocery.adapter.PopularBrandsAdapter;
import codecanyon.grocery.activities.MainActivity;
import codecanyon.grocery.R;
import codecanyon.grocery.models.BestProductResponse;
import codecanyon.grocery.models.CategoryResponse;
import codecanyon.grocery.models.OffersResponse;
import codecanyon.grocery.models.PopularBrandsResponse;
import codecanyon.grocery.models.SliderImage;
import codecanyon.grocery.reterofit.APIUrls;
import codecanyon.grocery.reterofit.RetrofitInstance;
import codecanyon.grocery.reterofit.RetrofitService;
import codecanyon.grocery.util.ConnectivityReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class HomeFragment extends Fragment {

    private SliderLayout sliderLayout;
    private RecyclerView rv_category;
    private RecyclerView rv_best_products;
    private RecyclerView rv_popular_brands;
    private RecyclerView rv_offers;

    private CategoryHomeAdapter categoryHomeAdapter;
    private BestProductAdapter bestProductAdapter;
    private PopularBrandsAdapter popularBrandsAdapter;
    private OfferAdapter offerAdapter;
    private RetrofitService service;
    private RelativeLayout rl_loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);


       /* AdView mAdView = (AdView) view.findViewById(R.id.adView_home);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        if (getActivity() != null) {
            ((MainActivity) getActivity()).updateHeader();
        }

        service = RetrofitInstance.createService(RetrofitService.class);

        rl_loading = view.findViewById(R.id.rl_loading);
        rl_loading.setVisibility(View.VISIBLE);

        sliderLayout = view.findViewById(R.id.sliderLayout);
        rv_category = view.findViewById(R.id.rv_category);
        rv_best_products = view.findViewById(R.id.rv_best_products);
        rv_popular_brands = view.findViewById(R.id.rv_popular_brands);
        rv_offers = view.findViewById(R.id.rv_offers);

        rv_category.setNestedScrollingEnabled(false);
        rv_best_products.setNestedScrollingEnabled(false);
        rv_popular_brands.setNestedScrollingEnabled(false);
        rv_offers.setNestedScrollingEnabled(false);

        TextView tv_search_bar = view.findViewById(R.id.tv_search_bar);
        ImageView iv_ad1 = view.findViewById(R.id.iv_ad1);
        ImageView iv_ad2 = view.findViewById(R.id.iv_ad2);

        rv_category.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rv_best_products.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        rv_popular_brands.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        rv_offers.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));

        categoryHomeAdapter = new CategoryHomeAdapter(getActivity());
        rv_category.setAdapter(categoryHomeAdapter);

        bestProductAdapter = new BestProductAdapter(getActivity());
        rv_best_products.setAdapter(bestProductAdapter);

        popularBrandsAdapter = new PopularBrandsAdapter(getActivity());
        rv_popular_brands.setAdapter(popularBrandsAdapter);

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_logonew)
                .error(R.drawable.ic_logonew)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true);


        Glide.with(this)
                .load(APIUrls.ADD_IMAGE_URL1 + "011.png")
                .apply(requestOptions)
                .into(iv_ad2);

        Glide.with(this)
                .load(APIUrls.ADD_IMAGE_URL1 + "imgpsh_fullsize.png")
                .apply(requestOptions)
                .into(iv_ad1);

        if (ConnectivityReceiver.isConnected()) {
            makeGetSliderRequest();
            makeGetCategoryRequest();
            makeGetBestProductsRequest();
            makeGetPopularBrandRequest();
            makeGetOfferRequest();
        }

        tv_search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment searchFragment = new SearchFragment();
                FragmentManager fM = getFragmentManager();

                if (fM != null) {
                    fM.beginTransaction()
                            .replace(R.id.frame_layout, searchFragment, SearchFragment.class.getSimpleName())
                            .commit();
                }

            }
        });

//        rv_popular_brands.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_category, new RecyclerTouchListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//                String getid = popularBrands_List.get(position).getId();
//                String gettitle = "Flipkart";
//                Boolean isCategory = false;
//                Bundle args = new Bundle();
//                Fragment fm = new ProductFragment();
//                args.putString("cat_id", getid);
//                args.putString("cat_title", gettitle);
//                args.putBoolean("isCategory", isCategory);
//                fm.setArguments(args);
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.frame_layout, fm)
//                        .addToBackStack(null).commit();
//
//
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));


        return view;
    }

    /**
     * Method to make json array request where json response starts wtih {
     */
    private void makeGetSliderRequest() {
        service.getSliderImages().enqueue(new Callback<List<SliderImage>>() {
            @Override
            public void onResponse(Call<List<SliderImage>> call, Response<List<SliderImage>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (SliderImage sliderImage : response.body()) {
                        try {
                            TextSliderView textSliderView = new TextSliderView(getActivity());

                            textSliderView.description(sliderImage.getSlider_title())
                                    .image(APIUrls.IMG_SLIDER_URL + sliderImage.getSlider_image())
                                    .setScaleType(BaseSliderView.ScaleType.Fit);

                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle().putString("extra", sliderImage.getSlider_title());

                            sliderLayout.addSlider(textSliderView);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SliderImage>> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Method to make json object request where json response starts wtih {
     */
    private void makeGetCategoryRequest() {
        service.getCategory().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    CategoryResponse cr = response.body();
                    rl_loading.setVisibility(View.GONE);
                    categoryHomeAdapter.addItems(cr.getData());
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void makeGetBestProductsRequest() {

        service.getBestProducts().enqueue(new Callback<BestProductResponse>() {
            @Override
            public void onResponse(Call<BestProductResponse> call, Response<BestProductResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    BestProductResponse cr = response.body();
                    bestProductAdapter.addItems(cr.getData());
                }
            }

            @Override
            public void onFailure(Call<BestProductResponse> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void makeGetPopularBrandRequest() {

        service.getPopularBrands().enqueue(new Callback<PopularBrandsResponse>() {
            @Override
            public void onResponse(Call<PopularBrandsResponse> call, Response<PopularBrandsResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    PopularBrandsResponse cr = response.body();

                    popularBrandsAdapter.addItems(cr.getData());
                }
            }

            @Override
            public void onFailure(Call<PopularBrandsResponse> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void makeGetOfferRequest() {
        service.getGetOffers().enqueue(new Callback<OffersResponse>() {
            @Override
            public void onResponse(Call<OffersResponse> call, Response<OffersResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    OffersResponse cr = response.body();

                    offerAdapter = new OfferAdapter(cr.getData(), getActivity());
                    rv_offers.setAdapter(offerAdapter);
                    offerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<OffersResponse> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.connection_time_out, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem check = menu.findItem(R.id.action_change_password);
        check.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
          /*  case R.id.action_search:
                Fragment fm = new SearchFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
                return false;*/
        }
        return false;
    }

}
