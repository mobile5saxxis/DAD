package mydad.saxxis.models;

import java.util.ArrayList;
import java.util.List;

public class MyOrderDetailResponse {
    private List<MyOrderDetail> data;

    public List<MyOrderDetail> getData() {

        if (data == null) {
            data = new ArrayList<>();
        }

        return data;
    }

    public void setData(List<MyOrderDetail> data) {
        this.data = data;
    }
}
