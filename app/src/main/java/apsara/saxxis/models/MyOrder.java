package apsara.saxxis.models;

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
    private String final_amount;
    private String gst_amount;
    private String product_image;
    private String discount_amount;
    private String delivery_amount;
    private String delivery_amount_text;
    private String saved_amount;

    public String getSaved_amount ()
    {
        return saved_amount;
    }

    public void setSaved_amount (String saved_amount)
    {
        this.saved_amount = saved_amount;
    }


    public String getDelivery_amount() {
        return delivery_amount;
    }

    public void setDelivery_amount(String delivery_amount) {
        this.delivery_amount = delivery_amount;
    }

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

    public String getFinal_amount ()
    {
        return final_amount;
    }

    public void setFinal_amount (String final_amount)
    {
        this.final_amount = final_amount;
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

    public String getGst_amount ()
    {
        return gst_amount;
    }

    public void setGst_amount (String gst_amount)
    {
        this.gst_amount = gst_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }
    public String getDelivery_amount_text ()
    {
        return delivery_amount_text;
    }

    public void setDelivery_amount_text (String delivery_amount_text)
    {
        this.delivery_amount_text = delivery_amount_text;
    }

}
