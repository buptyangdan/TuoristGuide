package Models;

/**
 * Created by caoyi on 16/4/19.
 */
public class Users_stores {
    private String email;
    private String store_geo;
    private String comment_text;
    private String create_time;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStore_geo() {
		return store_geo;
	}
	public void setStore_geo(String store_geo) {
		this.store_geo = store_geo;
	}
	
	public String getComment_text() {
		return comment_text;
	}
	public void setComment_text(String comment_text) {
		this.comment_text = comment_text;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public Users_stores(String email, String store_geo, String comment_text,
			String create_time) {
		super();
		this.email = email;
		this.store_geo = store_geo;
		this.comment_text = comment_text;
		this.create_time = create_time;
	}
    
	
    
}

