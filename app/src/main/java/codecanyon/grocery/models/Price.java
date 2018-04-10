package codecanyon.grocery.models;

public class Price {
    private String id;
    private String stock;
    private String strikeprice;
    private String status;
    private String quantity;
    private String price_val;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getStrikeprice() {
        return strikeprice;
    }

    public void setStrikeprice(String strikeprice) {
        this.strikeprice = strikeprice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice_val() {
        return price_val;
    }

    public void setPrice_val(String price_val) {
        this.price_val = price_val;
    }
}
