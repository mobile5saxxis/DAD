package codecanyon.grocery.models;

import java.util.ArrayList;
import java.util.List;

public class MyOrderResponse {
    boolean responce;
    List<MyOrder> data;

    public boolean isResponce() {
        return responce;
    }

    public void setResponce(boolean responce) {
        this.responce = responce;
    }

    public List<MyOrder> getData() {

        if (data == null) {
            data = new ArrayList<>();
        }

        return data;
    }

    public void setData(List<MyOrder> data) {
        this.data = data;
    }
}
