package codecanyon.grocery.models;

public class RequestResponse {
    private boolean responce;
    private String data;
    private String message;
    private String error;
    private String order_id;
    private String required_otp;

    public String getRequired_otp() {
        return required_otp;
    }

    public void setRequired_otp(String required_otp) {
        this.required_otp = required_otp;
    }

    public boolean isResponce() {
        return responce;
    }

    public void setResponce(boolean responce) {
        this.responce = responce;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
