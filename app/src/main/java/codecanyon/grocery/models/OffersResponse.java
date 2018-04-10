package codecanyon.grocery.models;

import java.util.List;

public class OffersResponse {
    private String responce;
    private List<Product> data;

    public String getResponce() {
        return responce;
    }

    public void setResponce(String responce) {
        this.responce = responce;
    }

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }
}
