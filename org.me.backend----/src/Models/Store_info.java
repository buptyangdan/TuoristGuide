package Models;

/**
 * Created by caoyi on 16/4/19.
 */
public class Store_info {

    private String store_name;
    private String pic_url;
    private String comment_text;
    private String created_time;

    public Store_info(String store_name, String comment_text,  String pic_url, String created_time) {
        this.store_name = store_name;
        this.comment_text = comment_text;
        this.pic_url = pic_url;
        this.created_time=created_time;
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
