package codecanyon.grocery.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.util.Date;

public class Coupon extends SugarRecord {
    private String Coupon_value;
    private String Status;
    private Date From_Date;
    private String Coupon_title;
    private String Minimum_order_Amount;
    @Unique
    @SerializedName("Id")
    @Expose
    private String couponId;
    private Date To_Date;
    private String Times_Per_user;

    public Coupon() {
    }

    public String getCoupon_value() {
        return Coupon_value;
    }

    public void setCoupon_value(String coupon_value) {
        Coupon_value = coupon_value;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCoupon_title() {
        return Coupon_title;
    }

    public void setCoupon_title(String coupon_title) {
        Coupon_title = coupon_title;
    }

    public String getMinimum_order_Amount() {
        return Minimum_order_Amount;
    }

    public void setMinimum_order_Amount(String minimum_order_Amount) {
        Minimum_order_Amount = minimum_order_Amount;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String id) {
        couponId = id;
    }

    public Date getFrom_Date() {
        return From_Date;
    }

    public void setFrom_Date(Date from_Date) {
        From_Date = from_Date;
    }

    public Date getTo_Date() {
        return To_Date;
    }

    public void setTo_Date(Date to_Date) {
        To_Date = to_Date;
    }

    public String getTimes_Per_user() {
        return Times_Per_user;
    }

    public void setTimes_Per_user(String times_Per_user) {
        Times_Per_user = times_Per_user;
    }
}
