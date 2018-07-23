package apsara.saxxis.models;

/**
 * Created by Rajesh Dabhi on 6/7/2017.
 */

public class DeliveryAddress {

    private String pincode;
    private String delivery_charge;
    private String house_no;
    private String user_id;
    private String receiver_mobile;
    private String socity_name;
    private String receiver_name;
    private String socity_id;
    private String location_id;
    private boolean ischeckd;

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getReceiver_mobile() {
        return receiver_mobile;
    }

    public void setReceiver_mobile(String receiver_mobile) {
        this.receiver_mobile = receiver_mobile;
    }

    public String getSocity_name() {
        return socity_name;
    }

    public void setSocity_name(String socity_name) {
        this.socity_name = socity_name;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getSocity_id() {
        return socity_id;
    }

    public void setSocity_id(String socity_id) {
        this.socity_id = socity_id;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public boolean getIscheckd() {
        return ischeckd;
    }

    public void setIscheckd(boolean ischeckd) {
        this.ischeckd = ischeckd;
    }
}
