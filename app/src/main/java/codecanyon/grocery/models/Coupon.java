package codecanyon.grocery.models;

public class Coupon {
    private String Coupon_value;
    private String Status;
    private String From_Date;
    private String Coupon_title;
    private String Minimum_order_Amount;
    private String Id;
    private String To_Date;
    private String Times_Per_user;

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

    public String getFrom_Date() {
        return From_Date;
    }

    public void setFrom_Date(String from_Date) {
        From_Date = from_Date;
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

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTo_Date() {
        return To_Date;
    }

    public void setTo_Date(String to_Date) {
        To_Date = to_Date;
    }

    public String getTimes_Per_user() {
        return Times_Per_user;
    }

    public void setTimes_Per_user(String times_Per_user) {
        Times_Per_user = times_Per_user;
    }
}
