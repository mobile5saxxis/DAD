package codecanyon.grocery.reterofit;

import java.util.List;

import codecanyon.grocery.models.AddDeliveryRequest;
import codecanyon.grocery.models.AddOrderRequest;
import codecanyon.grocery.models.AddressDeleteRequest;
import codecanyon.grocery.models.BestProductResponse;
import codecanyon.grocery.models.CategoryRequest;
import codecanyon.grocery.models.CategoryResponse;
import codecanyon.grocery.models.ChangePasswordRequest;
import codecanyon.grocery.models.CheckLoginRequest;
import codecanyon.grocery.models.DeliveryAddress;
import codecanyon.grocery.models.DeliveryRequest;
import codecanyon.grocery.models.ForgotPasswordRequest;
import codecanyon.grocery.models.LoginRequest;
import codecanyon.grocery.models.LoginResponse;
import codecanyon.grocery.models.MyOrder;
import codecanyon.grocery.models.MyOrderDetail;
import codecanyon.grocery.models.OffersResponse;
import codecanyon.grocery.models.OrderRequest;
import codecanyon.grocery.models.PopularBrandsResponse;
import codecanyon.grocery.models.ProductRequest;
import codecanyon.grocery.models.ProductResponse;
import codecanyon.grocery.models.RegisterRequest;
import codecanyon.grocery.models.RequestResponse;
import codecanyon.grocery.models.SliderImage;
import codecanyon.grocery.models.SoCity;
import codecanyon.grocery.models.SubCategoryResponse;
import codecanyon.grocery.models.SupportInfoResponse;
import codecanyon.grocery.models.TimeRequest;
import codecanyon.grocery.models.TimeResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by FAMILY on 14-12-2017.
 */

public interface RetrofitService {
//    @POST("register")
//    Call<RegisterResponse> registerChild(@Body Profile profile);
//
//    @GET("/profile/settings")
//    Call<ProfileResponse> getProfile(@Query("token") String token);

    @POST(APIUrls.GET_ADDRESS_URL)
    Call<List<DeliveryAddress>> getDeliveryAddressList(@Body DeliveryRequest deliveryRequest);

    @POST(APIUrls.ADD_ADDRESS_URL)
    Call<RequestResponse> addDeliveryList(@Body AddDeliveryRequest deliveryRequest);


    @POST(APIUrls.EDIT_ADDRESS_URL)
    Call<RequestResponse> editDeliveryList(@Body AddDeliveryRequest deliveryRequest);

    @POST(APIUrls.CHANGE_PASSWORD_URL)
    Call<RequestResponse> changePassword(@Body ChangePasswordRequest changePasswordRequest);

    @POST(APIUrls.ADD_ORDER_URL)
    Call<RequestResponse> addOrder(@Body AddOrderRequest addOrderRequest);

    @POST(APIUrls.GET_CATEGORY_URL)
    Call<CategoryResponse> getCategory();

    @POST(APIUrls.GET_CATEGORY_URL)
    Call<CategoryResponse> getCategory(@Query("parent") String parent);

    @POST(APIUrls.GET_CATEGORY_URL)
    Call<SubCategoryResponse> getSubCategories(@Query("parent") String parent);

    @POST(APIUrls.GET_BEST_PRODUCTS)
    Call<BestProductResponse> getBestProducts();

    @POST(APIUrls.GET_BEST_PRODUCTS_LIST)
    Call<ProductResponse> getBestProducts(@Query("bid")String bid);

    @POST(APIUrls.GET_BRAND_LIST_URL)
    Call<PopularBrandsResponse> getPopularBrands();

    @POST(APIUrls.GET_PRODUCT_URL)
    Call<ProductResponse> getPopularBrands(@Query("bid")String bid);

    @POST(APIUrls.GET_OFFERS_URL)
    Call<OffersResponse> getGetOffers();

    @POST(APIUrls.GET_ORDER_URL)
    Call<List<MyOrder>> getOrder(@Body DeliveryRequest deliveryRequest);

    @GET(APIUrls.GET_PRODUCT_URL)
    Call<ProductResponse> getProducts(@Query("cat_id") String cat_id);

    @GET(APIUrls.GET_PRODUCT_URL)
    Call<ProductResponse> search(@Query("search") String search);

    @POST(APIUrls.GET_PRODUCT_URL)
    Call<ProductResponse> getProducts(@Body ProductRequest pr);

    @GET(APIUrls.GET_SOCITY_URL)
    Call<List<SoCity>> getSoCity();

    @GET
    Call<SupportInfoResponse> getSupportInfo(@Url String url);

    @POST(APIUrls.GET_TIME_SLOT_URL)
    Call<TimeResponse> getTime(@Body TimeRequest tr);

    @POST(APIUrls.LOGIN_URL)
    Call<LoginResponse> login(@Body LoginRequest tr);

    @POST(APIUrls.FORGOT_URL)
    Call<LoginResponse> forgotPassword(@Body ForgotPasswordRequest tr);

    @POST(APIUrls.REGISTER_URL)
    Call<RequestResponse> register(@Body RegisterRequest tr);

    @POST(APIUrls.ORDER_DETAIL_URL)
    Call<List<MyOrderDetail>> orderDetail(@Body OrderRequest tr);

    @POST(APIUrls.DELETE_ORDER_URL)
    Call<RequestResponse> deleteOrder(@Body OrderRequest tr);

    @POST(APIUrls.DELETE_ADDRESS_URL)
    Call<RequestResponse> deleteAddress(@Body AddressDeleteRequest tr);

    @POST(APIUrls.JSON_RIGISTER_FCM)
    Call<RequestResponse> registerFCM(@Body CheckLoginRequest tr);

    @GET(APIUrls.GET_SLIDER_URL)
    Call<List<SliderImage>> getSliderImages();
}
