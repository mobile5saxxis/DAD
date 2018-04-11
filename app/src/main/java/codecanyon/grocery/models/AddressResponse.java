package codecanyon.grocery.models;

import java.util.List;

public class AddressResponse {
    private List<DeliveryAddress> data;
    private boolean responce;

    public List<DeliveryAddress> getData() {
        return data;
    }

    public void setData(List<DeliveryAddress> data) {
        this.data = data;
    }

    public boolean isResponce() {
        return responce;
    }

    public void setResponce(boolean responce) {
        this.responce = responce;
    }
}
