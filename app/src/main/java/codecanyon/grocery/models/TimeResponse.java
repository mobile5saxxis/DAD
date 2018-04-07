package codecanyon.grocery.models;

import java.util.List;

public class TimeResponse {
    private boolean responce;
    private List<String> times;

    public boolean getResponce() {
        return responce;
    }

    public void setResponce(boolean responce) {
        this.responce = responce;
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }
}
