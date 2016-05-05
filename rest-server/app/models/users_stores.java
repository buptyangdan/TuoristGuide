package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by caoyi on 16/4/14.
 */
@Entity
public class users_stores extends Model{
    @Id
    private Long id;
    private String user_name;
    private String store_name;


    public users_stores() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

     public users_stores(String user_name, String store_name) {
        this.user_name = user_name;
        this.store_name = store_name;
    }

    public static Model.Finder<Long,users_stores>  find = new Model.Finder(Long.class, users_stores.class);

}
