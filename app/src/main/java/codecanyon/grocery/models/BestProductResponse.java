package codecanyon.grocery.models;

import java.util.ArrayList;
import java.util.List;

public class BestProductResponse {
    private String responce;
    private List<BestProducts> data;

    public String getResponce() {
        return responce;
    }

    public void setResponce(String responce) {
        this.responce = responce;
    }

    public List<BestProducts> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }

        return data;
    }

    public void setData(List<BestProducts> data) {
        this.data = data;
    }
}
