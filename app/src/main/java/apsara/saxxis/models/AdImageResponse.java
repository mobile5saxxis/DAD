package apsara.saxxis.models;

import java.util.ArrayList;
import java.util.List;

public class AdImageResponse {
    private List<Ad> data;
    private String responce;

    public List<Ad> getData() {
        return data;
    }

    public void setData(List<Ad> data) {
        if (data == null) {
            data = new ArrayList<>();
        }

        this.data = data;
    }

    public String getResponce() {
        return responce;
    }

    public void setResponce(String responce) {
        this.responce = responce;
    }
}
