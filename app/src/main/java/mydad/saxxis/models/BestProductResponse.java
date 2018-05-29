package mydad.saxxis.models;

import java.util.ArrayList;
import java.util.List;

public class BestProductResponse {
    private boolean responce;
    private List<Product> data;

    public boolean getResponce() {
        return responce;
    }

    public void setResponce(boolean responce) {
        this.responce = responce;
    }

    public List<Product> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }

        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }
}
