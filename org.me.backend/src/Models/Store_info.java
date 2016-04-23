package Models;

/**
 * Created by caoyi on 16/4/19.
 */
public class Store_info {

    private String store_name;
    private String pic_url;
    private String store_geo;
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
	public String getStore_geo() {
		return store_geo;
	}
	public void setStore_geo(String store_geo) {
		this.store_geo = store_geo;
	}
	public Store_info(String store_name, String pic_url, String store_geo) {
		super();
		this.store_name = store_name;
		this.pic_url = pic_url;
		this.store_geo = store_geo;
	}
    
    
}
