package apsara.saxxis.models;

import java.util.List;

public class PopularBrandsResponse {
    private String responce;
    private List<PopularBrands> data;

    public String getResponce() {
        return responce;
    }

    public void setResponce(String responce) {
        this.responce = responce;
    }

    public List<PopularBrands> getData() {
        return data;
    }

    public void setData(List<PopularBrands> data) {
        this.data = data;
    }
}
