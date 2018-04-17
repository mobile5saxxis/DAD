package codecanyon.grocery.reterofit;

import java.util.List;

import codecanyon.grocery.models.AddDeliveryRequest;
import codecanyon.grocery.models.AddOrderRequest;
import codecanyon.grocery.models.AddressDeleteRequest;
import codecanyon.grocery.models.AddressResponse;
import codecanyon.grocery.models.BestProductResponse;
import codecanyon.grocery.models.CategoryRequest;
import codecanyon.grocery.models.CategoryResponse;
import codecanyon.grocery.models.ChangePasswordRequest;
import codecanyon.grocery.models.CheckLoginRequest;
import codecanyon.grocery.models.CouponAvailableResponse;
import codecanyon.grocery.models.CouponResponse;
import codecanyon.grocery.models.DeliveryAddress;
import codecanyon.grocery.models.DeliveryRequest;
import codecanyon.grocery.models.DeliveryResponse;
import codecanyon.grocery.models.ForgotPasswordRequest;
import codecanyon.grocery.models.LimitCheck;
import codecanyon.grocery.models.LoginRequest;
import codecanyon.grocery.models.LoginResponse;
import codecanyon.grocery.models.MyOrder;
import codecanyon.grocery.models.MyOrderDetail;
import codecanyon.grocery.models.OffersResponse;
import codecanyon.grocery.models.OrderRequest;
import codecanyon.grocery.models.PopularBrandsResponse;
import codecanyon.grocery.models.ProductRequest;
import codecanyon.grocery.models.ProductResponse;
import codecanyon.grocery.models.Quantity;
import codecanyon.grocery.models.RegisterRequest;
import codecanyon.grocery.models.RequestResponse;
import codecanyon.grocery.models.SliderImage;
import codecanyon.grocery.models.SoCity;
import codecanyon.grocery.models.SubCategoryResponse;
import codecanyon.grocery.models.SupportInfoResponse;
import codecanyon.grocery.models.TimeRequest;
import codecanyon.grocery.models.TimeResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
                                   @Field("location") String location, @Field("data") String data, @Field("coupon_id") String coupon_id, @Field("payment_mode") String payment_mode);

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
    Call<List<MyOrder>> getOrder(@Field("user_id") String user_id);

    @GET(APIUrls.GET_PRODUCT_URL)
    Call<ProductResponse> getProducts(@Query("cat_id") String cat_id);

    @GET(APIUrls.GET_SLIDER_PRODUCT_URL)
    Call<ProductResponse> getSliderProducts(@Query("slider_id") String slider_id);

    @GET(APIUrls.GET_PRODUCT_URL)
    Call<ProductResponse> search(@Query("search") String search);

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
    Call<LoginResponse> forgotPassword(@Body ForgotPasswordRequest tr);

    @POST(APIUrls.REGISTER_URL)
    @FormUrlEncoded
    Call<RequestResponse> register(@Field("user_name") String name,
                                   @Field("user_mobile") String email,
                                   @Field("user_email") String phoneNo,
                                   @Field("password") String password);

    @POST(APIUrls.ORDER_DETAIL_URL)
    @FormUrlEncoded
    Call<List<MyOrderDetail>> orderDetail(@Field("sale_id") String sale_id);

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

    @POST(APIUrls.ONLINE_TRANSACTION)
    @FormUrlEncoded
    Call<RequestResponse> saveProductTransaction(@Field("PaymentId") String paymentId, @Field("AccountId") String accountId
            , @Field("MerchantRefNo") String merchantRefNo, @Field("Amount") String amount, @Field("SecureHash")
                                                String secureHash, @Field("order_id") String order_id);

    @GET(APIUrls.GET_SLIDER_URL)
    Call<List<SliderImage>> getSliderImages();

    @GET(APIUrls.GET_COUPON_URL)
    Call<CouponResponse> getCoupons();

    @GET(APIUrls.GET_STOCK_AVAILABILITY)
    Call<Quantity> getStockAvailability(@Query("pid") int pid);

    @GET(APIUrls.GET_APPLY_COUPON)
    Call<CouponAvailableResponse> getCouponAvailability(@Query("cid") String cid, @Query("udi") String udi);
}
