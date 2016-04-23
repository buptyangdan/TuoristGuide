package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by caoyi on 16/4/14.
 */
@Entity(name="user_info")
public class user_info  extends  Model{
    @Id
    private Long id;

    private String user_name;
    private String email;
    private String photo_url;


    public user_info() {
    }

    public String getUser_name(){
        return user_name;
    }

    public void setUser_name(String user_name){
        this.user_name=user_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoto_url() {
        return photo_url;
    }



    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public user_info(String user_name, String email, String photo_url) {
        this.user_name = user_name;
        this.email = email;
        this.photo_url = photo_url;
    }
    public static Model.Finder<Long,user_info>  find = new Model.Finder(Long.class, user_info.class);

}
