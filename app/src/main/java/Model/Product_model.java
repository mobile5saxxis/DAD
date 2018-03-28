package Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class Product_model implements Parcelable {

    String product_id;
    String product_name;
    String product_description;
    String product_image;
    String category_id;
    String in_stock;
    String price;
    String unit_value;
    String unit;
    String increament;
    String title;

    protected Product_model(Parcel in) {
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
        title = in.readString();
    }

    public static final Creator<Product_model> CREATOR = new Creator<Product_model>() {
        @Override
        public Product_model createFromParcel(Parcel in) {
            return new Product_model(in);
        }

        @Override
        public Product_model[] newArray(int size) {
            return new Product_model[size];
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
        return title;
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
        dest.writeString(title);
    }
}
