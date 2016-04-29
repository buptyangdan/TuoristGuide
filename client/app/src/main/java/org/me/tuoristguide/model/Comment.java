package org.me.tuoristguide.model;

/**
 * Created by zy on 4/14/16.
 */
public class Comment {
    public String email;
    public String store_id;
    public String comment_text;
    public String created_time;
    public String photo_url;
    public String pic_url;
    public String user_name;
    public String store_name;

    public Comment(String comment_text, String created_time, String email, String store_id) {
        this.comment_text = comment_text;
        this.created_time = created_time;
        this.email = email;
        this.store_id = store_id;
    }


    public Comment(String comment_text, String created_time,  String photo_url, String pic_url,  String store_name, String user_name) {
        this.comment_text = comment_text;
        this.created_time = created_time;
        this.photo_url = photo_url;
        this.pic_url = pic_url;
        this.store_name = store_name;
        this.user_name = user_name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }
}
