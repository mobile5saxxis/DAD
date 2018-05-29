package mydad.saxxis.models;

import java.util.ArrayList;
import java.util.List;

public class CategoryResponse {
    private String responce;
    private List<Category> data;

    public String getResponce() {
        return responce;
    }

    public void setResponce(String responce) {
        this.responce = responce;
    }

    public List<Category> getData() {

        if (data == null) {
            data = new ArrayList<>();
        }

        return data;
    }

    public void setData(List<Category> data) {
        this.data = data;
    }
}
