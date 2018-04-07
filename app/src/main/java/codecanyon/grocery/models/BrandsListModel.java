package codecanyon.grocery.models;

/**
 * Created by srikarn on 26-03-2018.
 */

public class BrandsListModel {

    String id;
    String name;
    String image;
    String popular;
    String status;


    public String getId(){
        return  id;
    }

    public void setId(String id){
        this.id = id;
    }


    public String getName(){
        return  name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getImage(){
        return  image;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getPopular(){
        return  popular;
    }

    public void setPopular(String popular){
        this.popular = popular;
    }

    public String getStatus(){
        return  status;
    }

    public void setStatus(String status){
        this.status = status;
    }



}
