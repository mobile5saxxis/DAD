package codecanyon.grocery.models;

import java.util.List;

public class OffersResponse {
    private String responce;
    private List<Offers> data;

    public String getResponce() {
        return responce;
    }

    public void setResponce(String responce) {
        this.responce = responce;
    }

    public List<Offers> getData() {
        return data;
    }

    public void setData(List<Offers> data) {
        this.data = data;
    }
}
