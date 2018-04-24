package codecanyon.grocery.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import codecanyon.grocery.adapter.BestArrivalPager;
import codecanyon.grocery.adapter.BestSellersAdapter;
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

public class HomeFragment extends Fragment implements BaseSliderView.OnSliderClickListener {

    private SliderLayout sliderLayout;
    private RecyclerView rv_offers;

    private CategoryHomeAdapter categoryHomeAdapter;
    private PopularBrandsAdapter popularBrandsAdapter;
    private OfferAdapter offerAdapter;
    private RetrofitService service;
    private RelativeLayout rl_loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

       /* AdView mAdView = (AdView) view.findViewById(R.id.adView_home);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (getContext() != null) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
        });

        if (getActivity() != null) {
            ((MainActivity) getActivity()).updateHeader();
        }

        service = RetrofitInstance.createService(RetrofitService.class);

        rl_loading = view.findViewById(R.id.rl_loading);
        rl_loading.setVisibility(View.VISIBLE);

        sliderLayout = view.findViewById(R.id.sliderLayout);
        RecyclerView rv_category = view.findViewById(R.id.rv_category);
        RecyclerView rv_popular_brands = view.findViewById(R.id.rv_popular_brands);
        rv_offers = view.findViewById(R.id.rv_offers);

        rv_category.setNestedScrollingEnabled(false);
        rv_popular_brands.setNestedScrollingEnabled(false);
        rv_offers.setNestedScrollingEnabled(false);

        TextView tv_search_bar = view.findViewById(R.id.tv_search_bar);
        ImageView iv_ad1 = view.findViewById(R.id.iv_ad1);
        ImageView iv_ad2 = view.findViewById(R.id.iv_ad2);

        iv_ad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment adFragment = AdFragment.newInstance("5");
                FragmentManager fM = getFragmentManager();

                if (fM != null) {
                    fM.beginTransaction()
                            .replace(R.id.frame_layout, adFragment, AdFragment.class.getSimpleName())
                            .addToBackStack(AdFragment.class.getSimpleName())
                            .commit();
                }

            }
        });

        iv_ad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment adFragment = AdFragment.newInstance("4");
                FragmentManager fM = getFragmentManager();

                if (fM != null) {
                    fM.beginTransaction()
                            .replace(R.id.frame_layout, adFragment, AdFragment.class.getSimpleName())
                            .addToBackStack(AdFragment.class.getSimpleName())
                            .commit();
                }

            }
        });

        rv_category.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rv_popular_brands.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        rv_offers.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));

        categoryHomeAdapter = new CategoryHomeAdapter(getActivity());
        rv_category.setAdapter(categoryHomeAdapter);

        popularBrandsAdapter = new PopularBrandsAdapter(getActivity());
        rv_popular_brands.setAdapter(popularBrandsAdapter);

        offerAdapter = new OfferAdapter(getActivity());
        rv_offers.setAdapter(offerAdapter);

        final TabLayout tab_layout = view.findViewById(R.id.tab_layout);
        final ViewPager view_pager = view.findViewById(R.id.view_pager);
        view_pager.setOffscreenPageLimit(0);
        tab_layout.setupWithViewPager(view_pager);
        BestArrivalPager bestArrivalPager = new BestArrivalPager(getChildFragmentManager(), getContext());
        view_pager.setAdapter(bestArrivalPager);

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
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

        getProducts();

        tv_search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment searchFragment = new SearchFragment();
                FragmentManager fM = getFragmentManager();

                if (fM != null) {
                    fM.beginTransaction()
                            .replace(R.id.frame_layout, searchFragment, SearchFragment.class.getSimpleName())
                            .addToBackStack(SearchFragment.class.getSimpleName())
                            .commit();
                }

            }
        });

        view.findViewById(R.id.tv_see_all_offers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment offersFragment = new OffersFragment();
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, offersFragment, OffersFragment.class.getSimpleName())
                        .addToBackStack(OffersFragment.class.getSimpleName())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });


        return view;
    }

    public void getProducts() {
        if (ConnectivityReceiver.isConnected()) {
            makeGetSliderRequest();
            makeGetCategoryRequest();
            makeGetPopularBrandRequest();
            makeGetOfferRequest();
        } else {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
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
                                    .image(APIUrls.IMG_SLIDER_URL + sliderImage.getSlider_image().replace(" ", "%20"))
                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                    .setOnSliderClickListener(HomeFragment.this);

                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle().putString("extra", sliderImage.getSlider_title());
                            textSliderView.getBundle().putString("slider_id", sliderImage.getId());

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
                    offerAdapter.addItems(cr.getData());
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

    @Override
    public void onSliderClick(BaseSliderView slider) {
        String sliderId = slider.getBundle().getString("slider_id");
        String sliderTitle = slider.getBundle().getString("extra");

        if (getActivity() != null) {
            FragmentManager fM = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fM.beginTransaction();

            ft.replace(R.id.frame_layout, SliderProductFragment.newInstance(sliderId, sliderTitle), SliderProductFragment.class.getSimpleName())
                    .addToBackStack(SliderProductFragment.class.getSimpleName());
            ft.commit();
        }
    }
}
