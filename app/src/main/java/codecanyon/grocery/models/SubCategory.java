package codecanyon.grocery.models;

public class SubCategory {
    private String slider;
    private String id;
    private String title;
    private String Count;
    private String status;
    private String description;
    private String leval;
    private String image;
    private String parent;
    private String slug;
    private String PCount;


    public String getSlider() {
        return slider;
    }

    public void setSlider(String slider) {
        this.slider = slider;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLeval() {
        return leval;
    }

    public void setLeval(String leval) {
        this.leval = leval;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getPCount() {
        return PCount;
    }

    public void setPCount(String PCount) {
        this.PCount = PCount;
    }

    @Override
    public String toString() {
        return "SubCategory{" +
                "slider='" + slider + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", Count='" + Count + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", leval='" + leval + '\'' +
                ", iv_image='" + image + '\'' +
                ", parent='" + parent + '\'' +
                ", slug='" + slug + '\'' +
                ", PCount='" + PCount + '\'' +
                '}';
    }
}
