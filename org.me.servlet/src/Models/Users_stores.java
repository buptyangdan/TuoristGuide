package Models;

/**
 * Created by caoyi on 16/4/19.
 */
public class Users_stores {
    private String email;
    private String store_geo;
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
	public Users_stores(String email, String store_geo) {
		super();
		this.email = email;
		this.store_geo = store_geo;
	}

    
}

