package mydad.saxxis.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by srikarn on 29-03-2018.
 */

public class BestProducts implements Parcelable {

    String product_id;
    String product_name;
    String brand;
    String product_description;
    String Specifications;
    String How_to_use;
    String product_image;
    String category_id;
    String in_stock;
    String best_seller;
    String price;
    String offer;
    String unit_value;
    String unit;
    String Quantity_per_user;
    String increament;
    String createddate;


    protected BestProducts(Parcel in) {
        product_id = in.readString();
        product_name = in.readString();
        brand = in.readString();
        product_description = in.readString();
        Specifications = in.readString();
        How_to_use = in.readString();
        product_image = in.readString();
        category_id = in.readString();
        in_stock = in.readString();
        best_seller = in.readString();
        price = in.readString();
        offer = in.readString();
        unit_value = in.readString();
        unit = in.readString();
        Quantity_per_user = in.readString();
        increament = in.readString();
        createddate = in.readString();
    }

    public static final Creator<BestProducts> CREATOR = new Creator<BestProducts>() {
        @Override
        public BestProducts createFromParcel(Parcel in) {
            return new BestProducts(in);
        }

        @Override
        public BestProducts[] newArray(int size) {
            return new BestProducts[size];
        }
    };

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getSpecifications() {
        return Specifications;
    }

    public void setSpecifications(String specifications) {
        Specifications = specifications;
    }

    public String getHow_to_use() {
        return How_to_use;
    }

    public void setHow_to_use(String how_to_use) {
        How_to_use = how_to_use;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getIn_stock() {
        return in_stock;
    }

    public void setIn_stock(String in_stock) {
        this.in_stock = in_stock;
    }

    public String getBest_seller() {
        return best_seller;
    }

    public void setBest_seller(String best_seller) {
        this.best_seller = best_seller;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getUnit_value() {
        return unit_value;
    }

    public void setUnit_value(String unit_value) {
        this.unit_value = unit_value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQuantity_per_user() {
        return Quantity_per_user;
    }

    public void setQuantity_per_user(String quantity_per_user) {
        Quantity_per_user = quantity_per_user;
    }

    public String getIncreament() {
        return increament;
    }

    public void setIncreament(String increament) {
        this.increament = increament;
    }

    public String getCreateddate() {
        return createddate;
    }

    public void setCreateddate(String createddate) {
        this.createddate = createddate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(product_id);
        dest.writeString(product_name);
        dest.writeString(brand);
        dest.writeString(product_description);
        dest.writeString(Specifications);
        dest.writeString(How_to_use);
        dest.writeString(product_image);
        dest.writeString(category_id);
        dest.writeString(in_stock);
        dest.writeString(best_seller);
        dest.writeString(price);
        dest.writeString(offer);
        dest.writeString(unit_value);
        dest.writeString(unit);
        dest.writeString(Quantity_per_user);
        dest.writeString(increament);
        dest.writeString(createddate);
    }
}
