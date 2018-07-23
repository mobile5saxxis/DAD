package apsara.saxxis.models;

public class ProductDetailResponse {
    private Product data;

    private String responce;

    public Product getData() {
        return data;
    }

    public void setData(Product data) {
        this.data = data;
    }

    public String getResponce() {
        return responce;
    }

    public void setResponce(String responce) {
        this.responce = responce;
    }
}
