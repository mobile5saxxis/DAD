package apsara.saxxis.models;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryResponse {
    private boolean responce;
    private List<SubCategory> data;

    public boolean isResponce() {
        return responce;
    }

    public void setResponce(boolean responce) {
        this.responce = responce;
    }

    public List<SubCategory> getData() {

        if (data == null) {
            data = new ArrayList<>();
        }

        return data;
    }

    public void setData(List<SubCategory> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SubCategoryResponse{" +
                "responce=" + responce +
                ", data=" + data +
                '}';
    }
}
