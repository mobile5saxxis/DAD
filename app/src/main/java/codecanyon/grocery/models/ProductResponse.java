package codecanyon.grocery.models;

import java.util.List;

public class ProductResponse {
    private boolean responce;
    private List<Product> data;

    public boolean isResponce() {
        return responce;
    }

    public void setResponce(boolean responce) {
        this.responce = responce;
    }

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }
}
