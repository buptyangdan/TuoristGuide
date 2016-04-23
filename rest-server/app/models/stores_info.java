package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

/**
 * Created by caoyi on 16/4/14.
 */
@Entity
public class stores_info extends Model{
    @Id
    private Long id;
    private String store_name;
    private String pic_url;
    private String comment_text;
    private String created_time;
    public stores_info() {
    }

    public Long getId(){
        return id;
    }
    public void setId(){
        this.id=getId();
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public stores_info(String comment_text, String store_name, String pic_url, String created_time) {
        this.store_name = store_name;
        this.pic_url = pic_url;
        this.comment_text=comment_text;
        this.created_time=created_time;
    }
    public static Model.Finder<Long,stores_info>  find = new Model.Finder(Long.class, stores_info.class);

}
