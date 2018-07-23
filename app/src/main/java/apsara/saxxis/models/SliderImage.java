package apsara.saxxis.models;

public class SliderImage {
    private String id;

    private String slider_status;

    private String slider_url;

    private String slider_image;

    private String slider_title;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getSlider_status ()
    {
        return slider_status;
    }

    public void setSlider_status (String slider_status)
    {
        this.slider_status = slider_status;
    }

    public String getSlider_url ()
    {
        return slider_url;
    }

    public void setSlider_url (String slider_url)
    {
        this.slider_url = slider_url;
    }

    public String getSlider_image ()
    {
        return slider_image;
    }

    public void setSlider_image (String slider_image)
    {
        this.slider_image = slider_image;
    }

    public String getSlider_title ()
    {
        return slider_title;
    }

    public void setSlider_title (String slider_title)
    {
        this.slider_title = slider_title;
    }

    @Override
    public String toString()
    {
        return "SliderImage [id = "+id+", slider_status = "+slider_status+", slider_url = "+slider_url+", slider_image = "+slider_image+", slider_title = "+slider_title+"]";
    }
}
