package Models;

/**
 * Created by caoyi on 16/4/19.
 */
public class Comments {
    String email;
    String store_name;
    String comment_txt;
    String created_time;
    String photo_url;
    String pic_url;
    String comment_user;
    public Comments(String comment_user, String comment_txt, String created_time, String email, String photo_url, String pic_url, String store_name) {
        this.comment_txt = comment_txt;
        this.created_time = created_time;
        this.email = email;
        this.photo_url = photo_url;
        this.pic_url = pic_url;
        this.store_name = store_name;
        this.comment_user=comment_user;
    }
    public String getComment_txt() {
        return comment_txt;
    }

    public void setComment_txt(String comment_txt) {
        this.comment_txt = comment_txt;
    }
    public String getComment_user() {
        return comment_user;
    }

    public void setComment_user(String comment_user) {
        this.comment_user = comment_user;
    }


    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }
}
