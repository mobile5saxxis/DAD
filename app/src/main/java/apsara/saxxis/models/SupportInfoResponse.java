package apsara.saxxis.models;

import java.util.List;

public class SupportInfoResponse {
    private boolean responce;
    private List<SupportInfo> data;

    public boolean isResponce() {
        return responce;
    }

    public void setResponce(boolean responce) {
        this.responce = responce;
    }

    public List<SupportInfo> getData() {
        return data;
    }

    public void setData(List<SupportInfo> data) {
        this.data = data;
    }
}
