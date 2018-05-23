package codecanyon.grocery.models;

public class Ad {
    private String Created;

    private String add_img;

    private String add_status;

    private String Position;

    private String Id;

    private String add_url;

    public String getCreated() {
        return Created;
    }

    public void setCreated(String created) {
        Created = created;
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

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getAdd_url() {
        return add_url;
    }

    public void setAdd_url(String add_url) {
        this.add_url = add_url;
    }

    @Override
    public String toString() {
        return "Ad{" +
                "Created='" + Created + '\'' +
                ", add_img='" + add_img + '\'' +
                ", add_status='" + add_status + '\'' +
                ", Position='" + Position + '\'' +
                ", Id='" + Id + '\'' +
                ", add_url='" + add_url + '\'' +
                '}';
    }
}
