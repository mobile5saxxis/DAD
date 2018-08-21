package apsara.saxxis.models;

/**
 * Created by Rajesh Dabhi on 30/6/2017.
 */

public class MyOrderDetail {

    String sale_item_id;
    String sale_id;
    String product_id;
    String product_name;
    String qty;
    String unit;
    String unit_value;
    String price;
    String qty_in_kg;
    String product_image;
    private String final_amount;
    public String getFinal_amount ()
    {
        return final_amount;
    }

    public void setFinal_amount (String final_amount)
    {
        this.final_amount = final_amount;
    }

    public String getSale_item_id(){
        return sale_item_id;
    }

    public String getSale_id(){
        return sale_id;
    }

    public String getProduct_id(){
        return product_id;
    }

    public String getProduct_name(){
        return product_name;
    }

    public String getQty(){
        return qty;
    }

    public String getUnit(){
        return unit;
    }

    public String getUnit_value(){
        return unit_value;
    }

    public String getPrice(){
        return price;
    }

    public String getQty_in_kg(){
        return qty_in_kg;
    }

    public String getProduct_image(){
        return product_image;
    }

}
