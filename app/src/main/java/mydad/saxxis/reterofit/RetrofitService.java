package mydad.saxxis.reterofit;

import java.util.List;

import mydad.saxxis.models.AdImageResponse;
import mydad.saxxis.models.AdResponse;
import mydad.saxxis.models.AddDeliveryRequest;
import mydad.saxxis.models.AddOrderRequest;
import mydad.saxxis.models.AddressDeleteRequest;
import mydad.saxxis.models.AddressResponse;
import mydad.saxxis.models.BestProductResponse;
import mydad.saxxis.models.CategoryRequest;
import mydad.saxxis.models.CategoryResponse;
import mydad.saxxis.models.ChangePasswordRequest;
import mydad.saxxis.models.CheckLoginRequest;
import mydad.saxxis.models.CouponAvailableResponse;
import mydad.saxxis.models.CouponResponse;
import mydad.saxxis.models.DeliveryAddress;
import mydad.saxxis.models.DeliveryCharge;
import mydad.saxxis.models.DeliveryRequest;
import mydad.saxxis.models.DeliveryResponse;
import mydad.saxxis.models.ForgotPasswordRequest;
import mydad.saxxis.models.LimitCheck;
import mydad.saxxis.models.LoginRequest;
import mydad.saxxis.models.LoginResponse;
import mydad.saxxis.models.MyOrder;
import mydad.saxxis.models.MyOrderDetail;
import mydad.saxxis.models.MyOrderDetailResponse;
import mydad.saxxis.models.MyOrderResponse;
import mydad.saxxis.models.OffersResponse;
import mydad.saxxis.models.OrderRequest;
import mydad.saxxis.models.PopularBrandsResponse;
import mydad.saxxis.models.ProductDetailResponse;
import mydad.saxxis.models.ProductRequest;
import mydad.saxxis.models.ProductResponse;
import mydad.saxxis.models.Quantity;
import mydad.saxxis.models.RegisterRequest;
import mydad.saxxis.models.RequestResponse;
import mydad.saxxis.models.SliderImage;
import mydad.saxxis.models.SoCity;
import mydad.saxxis.models.SubCategoryResponse;
import mydad.saxxis.models.SupportInfoResponse;
import mydad.saxxis.models.TimeRequest;
import mydad.saxxis.models.TimeResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.QueryName;
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
    @FormUrlEncoded
    Call<AddressResponse> getDeliveryAddressList(@Field("user_id") String user_id);

    @POST(APIUrls.ADD_ADDRESS_URL)
    @FormUrlEncoded
    Call<DeliveryResponse> addDeliveryList(@Field("user_id") String user_id,
                                           @Field("pincode") String pincode,
                                           @Field("socity_id") String socity_id,
                                           @Field("house_no") String house_no,
                                           @Field("receiver_name") String receiver_name,
                                           @Field("receiver_mobile") String receiver_mobile,
                                           @Field("location_id") String location_id);


    @POST(APIUrls.EDIT_ADDRESS_URL)
    Call<RequestResponse> editDeliveryList(@Body AddDeliveryRequest deliveryRequest);

    @POST(APIUrls.CHANGE_PASSWORD_URL)
    Call<RequestResponse> changePassword(@Body ChangePasswordRequest changePasswordRequest);

    @POST(APIUrls.ADD_ORDER_URL)
    @FormUrlEncoded
    Call<RequestResponse> addOrder(@Field("date") String date,
                                   @Field("time") String time,
                                   @Field("user_id") String user_id,
                                   @Field("location") String location, @Field("data") String data, @Field("coupon_id") String coupon_id, @Field("payment_mode") int payment_mode, @Field("otp") String otp);

    @POST(APIUrls.GET_CATEGORY_URL)
    Call<CategoryResponse> getCategory();

    @POST(APIUrls.GET_CATEGORY_URL)
    Call<CategoryResponse> getCategory(@Query("parent") String parent);

    @POST(APIUrls.GET_CATEGORY_URL)
    Call<SubCategoryResponse> getSubCategories(@Query("parent") String parent);

    @POST(APIUrls.GET_BEST_PRODUCTS)
    Call<BestProductResponse> getBestProducts();

    @POST(APIUrls.GET_BEST_PRODUCTS_LIST)
    Call<ProductResponse> getBestProducts(@Query("bid") String bid);

    @POST(APIUrls.GET_BRAND_LIST_URL)
    Call<PopularBrandsResponse> getPopularBrands();

    @POST(APIUrls.GET_PRODUCT_URL)
    Call<ProductResponse> getPopularBrands(@Query("bid") String bid);

    @GET(APIUrls.GET_NEW_ARRIVAL_URL)
    Call<ProductResponse> getNewArrivals();

    @POST(APIUrls.GET_OFFERS_URL)
    Call<OffersResponse> getGetOffers();

    @POST(APIUrls.GET_ORDER_URL)
    @FormUrlEncoded
    Call<MyOrderResponse> getOrder(@Field("user_id") String user_id);

    @GET(APIUrls.GET_PRODUCT_URL)
    Call<ProductResponse> getProducts(@Query("cat_id") String cat_id, @Query("sort") String sort);

    @GET(APIUrls.GET_SLIDER_PRODUCT_URL)
    Call<ProductResponse> getSliderProducts(@Query("slider_id") String slider_id);

    @GET(APIUrls.GET_PRODUCT_URL)
    Call<ProductResponse> search(@Query("search") String search);

    @GET(APIUrls.GET_AD_URL)
    Call<AdResponse> getAdResponse(@Query("id") String id);


    @GET(APIUrls.GET_LIMITE_SETTING_URL)
    Call<List<LimitCheck>> limitCheck();

    @POST(APIUrls.GET_PRODUCT_URL)
    Call<ProductResponse> getProducts(@Body ProductRequest pr);

    @GET(APIUrls.GET_SOCITY_URL)
    Call<List<SoCity>> getSoCity();

    @GET
    Call<SupportInfoResponse> getSupportInfo(@Url String url);

    @POST(APIUrls.GET_TIME_SLOT_URL)
    @FormUrlEncoded
    Call<TimeResponse> getTime(@Field("date") String date);

    @POST(APIUrls.LOGIN_URL)
    @FormUrlEncoded
    Call<LoginResponse> login(@Field("user_email") String email, @Field("password") String password);

    @POST(APIUrls.FORGOT_URL)
    @FormUrlEncoded
    Call<LoginResponse> forgotPassword(@Field("email") String email);

    @POST(APIUrls.REGISTER_URL)
    @FormUrlEncoded
    Call<RequestResponse> register(@Field("user_name") String name,
                                   @Field("user_email") String email,
                                   @Field("user_mobile") String phoneNo,
                                   @Field("password") String password);

    @POST(APIUrls.ORDER_DETAIL_URL)
    @FormUrlEncoded
    Call<MyOrderDetailResponse> orderDetail(@Field("sale_id") String sale_id);

    @POST(APIUrls.DELETE_ORDER_URL)
    @FormUrlEncoded
    Call<RequestResponse> deleteOrder(@Field("sale_id") String sale_id,
                                      @Field("user_id") String user_id);

    @POST(APIUrls.DELETE_ADDRESS_URL)
    @FormUrlEncoded
    Call<RequestResponse> deleteAddress(@Field("location_id") String location_id);

    @POST(APIUrls.JSON_RIGISTER_FCM)
    @FormUrlEncoded
    Call<RequestResponse> registerFCM(@Field("user_id") String user_id,
                                      @Field("token") String token,
                                      @Field("device") String device);

    @POST(APIUrls.ADD_TOKEN)
    @FormUrlEncoded
    Call<RequestResponse> addToken(@Field("token") String token);

    @POST(APIUrls.ONLINE_TRANSACTION)
    @FormUrlEncoded
    Call<RequestResponse> saveProductTransaction(@Field("PaymentId") String paymentId, @Field("AccountId") String accountId
            , @Field("MerchantRefNo") String merchantRefNo, @Field("Amount") String amount, @Field("SecureHash")
                                                         String secureHash, @Field("order_id") String order_id, @Field("status") String status);

    @GET(APIUrls.GET_SLIDER_URL)
    Call<List<SliderImage>> getSliderImages();

    @GET(APIUrls.GET_COUPON_URL)
    Call<CouponResponse> getCoupons();

    @GET(APIUrls.GET_APPLY_COUPON)
    Call<CouponAvailableResponse> getCouponAvailability(@Query("cid") String cid, @Query("uid") String uid);

    @GET(APIUrls.GET_DELIVERY_CHARGE)
    Call<DeliveryCharge> getDeliveryCharge();

    @GET(APIUrls.PRODUCT_DETAILS_URL)
    Call<ProductDetailResponse> getProductDetails(@Query("pid") String pid);

    @GET(APIUrls.ADS_URL)
    Call<AdImageResponse> getAds();
}
