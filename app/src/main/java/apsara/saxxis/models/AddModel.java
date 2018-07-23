package apsara.saxxis.models;

/**
 * Created by srikarn on 28-03-2018.
 */

public class AddModel {

    private String id;
    private String Position;
    private String add_url;
    private String add_img;
    private String add_status;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getAdd_url() {
        return add_url;
    }

    public void setAdd_url(String add_url) {
        this.add_url = add_url;
    }

    public String getAdd_img() {
        return add_img;
    }

    public void setAdd_img(String add_img) {
        this.add_img = add_img;
    }

    public String getAdd_status() {
        return add_status;
    }

    public void setAdd_status(String add_status) {
        this.add_status = add_status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
