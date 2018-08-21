package apsara.saxxis.models;

import java.util.ArrayList;
import java.util.List;

public class MyOrderDetailResponse {
    private List<MyOrderDetail> data;
    private MyOrderDetail order_details;

    private String products_data;

    public List<MyOrderDetail> getData() {

        if (data == null) {
            data = new ArrayList<>();
        }

        return data;
    }
    public MyOrderDetail getOrder_details ()
    {
        return order_details;
    }

    public void setOrder_details (MyOrderDetail order_details)
    {
        this.order_details = order_details;
    }

    public String getProducts_data ()
    {
        return products_data;
    }

    public void setProducts_data (String products_data)
    {
        this.products_data = products_data;
    }

    public void setData(List<MyOrderDetail> data) {
        this.data = data;
    }
}
