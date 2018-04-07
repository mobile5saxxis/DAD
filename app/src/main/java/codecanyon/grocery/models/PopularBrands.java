package codecanyon.grocery.models;

/**
 * Created by srikarn on 30-03-2018.
 */

public class PopularBrands {



    String Id;
    String name;
    String image;
    String Popular;
    String status;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPopular() {
        return Popular;
    }

    public void setPopular(String popular) {
        Popular = popular;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
