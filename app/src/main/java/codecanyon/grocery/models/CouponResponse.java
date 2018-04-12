package codecanyon.grocery.models;

import java.util.ArrayList;
import java.util.List;

public class CouponResponse {
    private boolean responce;
    private List<Coupon> data;

    public boolean isResponce() {
        return responce;
    }

    public void setResponce(boolean responce) {
        this.responce = responce;
    }

    public List<Coupon> getData() {

        if (data == null) {
            data = new ArrayList<>();
        }

        return data;
    }

    public void setData(List<Coupon> data) {
        this.data = data;
    }
}
