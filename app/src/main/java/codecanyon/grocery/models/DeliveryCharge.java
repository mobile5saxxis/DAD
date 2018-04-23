package codecanyon.grocery.models;

public class DeliveryCharge {
    private String amount;

    private String charge_rs;

    private boolean responce;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCharge_rs() {
        return charge_rs;
    }

    public void setCharge_rs(String charge_rs) {
        this.charge_rs = charge_rs;
    }

    public boolean isResponce() {
        return responce;
    }

    public void setResponce(boolean responce) {
        this.responce = responce;
    }
}
