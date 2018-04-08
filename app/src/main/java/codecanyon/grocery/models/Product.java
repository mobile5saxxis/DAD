package codecanyon.grocery.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class Product implements Parcelable {

   /* String product_id;
    String product_name;
    String product_description;
    String product_image;
    String category_id;
    String in_stock;
    String price;
    String unit_value;
    String unit;
    String increament;
    String tv_subcat_title;
    String brand;
    String specifications;
    String how_to_use;
    String stock;


    protected Product(Parcel in) {
        product_id = in.readString();
        product_name = in.readString();
        product_description = in.readString();
        product_image = in.readString();
        category_id = in.readString();
        in_stock = in.readString();
        price = in.readString();
        unit_value = in.readString();
        unit = in.readString();
        increament = in.readString();
        tv_subcat_title = in.readString();
        brand = in.readString();
        specifications = in.readString();
        how_to_use = in.readString();
        stock = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public String getProduct_image() {
        return product_image;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getIn_stock() {
        return in_stock;
    }

    public String getPrice() {
        return price;
    }

    public String getUnit_value() {
        return unit_value;
    }

    public String getUnit() {
        return unit;
    }


    public String getIncreament() {
        return increament;
    }

    public String getTitle() {
        return tv_subcat_title;
    }


    public String getBrand() {
        return brand;
    }
    public String getSpecifications() {
        return specifications;
    }
    public String getHow_to_use() {
        return how_to_use;
    }
    public String getStock() {
        return stock;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(product_id);
        dest.writeString(product_name);
        dest.writeString(product_description);
        dest.writeString(product_image);
        dest.writeString(category_id);
        dest.writeString(in_stock);
        dest.writeString(price);
        dest.writeString(unit_value);
        dest.writeString(unit);
        dest.writeString(increament);
        dest.writeString(tv_subcat_title);
        dest.writeString(brand);
        dest.writeString(specifications);
        dest.writeString(how_to_use);
        dest.writeString(stock);
    }*/
   String product_id;
    String product_name;
    String brand;
    String product_description;
    String specifications;
    String how_to_use;
    String product_image;
    String category_id;
    String in_stock;
    String best_seller;
    String offer;
    String price;
    String unit_value;
    String unit;
    String quantity_per_user;
    String increament;
    String createddate;
    String stock;
    String title;
    String id;
    String pid;
    String quantity;
    String price_val;
    String strikeprice;


    protected Product(Parcel in) {
        product_id = in.readString();
        product_name = in.readString();
        brand = in.readString();
        product_description = in.readString();
        specifications = in.readString();
        how_to_use = in.readString();
        product_image = in.readString();
        category_id = in.readString();
        in_stock = in.readString();
        best_seller = in.readString();
        offer = in.readString();
        price = in.readString();
        unit_value = in.readString();
        unit = in.readString();
        quantity_per_user = in.readString();
        increament = in.readString();
        createddate = in.readString();
        stock = in.readString();
        title = in.readString();
        id = in.readString();
        pid = in.readString();
        quantity = in.readString();
        price_val = in.readString();
        strikeprice = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
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
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getHow_to_use() {
        return how_to_use;
    }

    public void setHow_to_use(String how_to_use) {
        this.how_to_use = how_to_use;
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

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
        return quantity_per_user;
    }

    public void setQuantity_per_user(String quantity_per_user) {
        this.quantity_per_user = quantity_per_user;
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

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice_val() {
        return price_val;
    }

    public void setPrice_val(String price_val) {
        this.price_val = price_val;
    }

    public String getStrikeprice() {
        return strikeprice;
    }

    public void setStrikeprice(String strikeprice) {
        this.strikeprice = strikeprice;
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
        dest.writeString(specifications);
        dest.writeString(how_to_use);
        dest.writeString(product_image);
        dest.writeString(category_id);
        dest.writeString(in_stock);
        dest.writeString(best_seller);
        dest.writeString(offer);
        dest.writeString(price);
        dest.writeString(unit_value);
        dest.writeString(unit);
        dest.writeString(quantity_per_user);
        dest.writeString(increament);
        dest.writeString(createddate);
        dest.writeString(stock);
        dest.writeString(title);
        dest.writeString(id);
        dest.writeString(pid);
        dest.writeString(quantity);
        dest.writeString(price_val);
        dest.writeString(strikeprice);
    }
}
