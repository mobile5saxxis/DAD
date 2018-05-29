package mydad.saxxis.models;

public class CouponAvailableResponse {
    private boolean responce;
    private CouponAvailable data;

    public boolean isResponce() {
        return responce;
    }

    public void setResponce(boolean responce) {
        this.responce = responce;
    }

    public CouponAvailable getData() {
        return data;
    }

    public void setData(CouponAvailable data) {
        this.data = data;
    }
}
