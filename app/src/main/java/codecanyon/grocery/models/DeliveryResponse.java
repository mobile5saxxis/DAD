package codecanyon.grocery.models;

public class DeliveryResponse {
    private DeliveryAddress data;
    private boolean responce;

    public DeliveryAddress getData() {
        return data;
    }

    public void setData(DeliveryAddress data) {
        this.data = data;
    }

    public boolean isResponce() {
        return responce;
    }

    public void setResponce(boolean responce) {
        this.responce = responce;
    }
}
