package codecanyon.grocery.models;

/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class MyOrder {

    private String sale_id;
    private String user_id;
    private String on_date;
    private String delivery_time_from;
    private  String delivery_time_to;
    private String status;
    private String note;
    private String is_paid;
    private String total_amount;
    private String total_kg;
    private String total_items;
    private String socity_id;
    private String delivery_address;
    private String location_id;
    private String delivery_charge;
    private String product_image;
    private String discount_amount;

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getSale_id() {
        return sale_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getOn_date() {
        return on_date;
    }

    public String getDelivery_time_from() {
        return delivery_time_from;
    }

    public String getDelivery_time_to() {
        return delivery_time_to;
    }

    public String getStatus() {
        return status;
    }

    public String getNote() {
        return note;
    }

    public String getIs_paid() {
        return is_paid;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public String getTotal_kg() {
        return total_kg;
    }

    public String getTotal_items() {
        return total_items;
    }

    public String getSocity_id() {
        return socity_id;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public String getLocation_id() {
        return location_id;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }
}
