package Models;

/**
 * Created by caoyi on 16/4/19.
 */
public class Users_stores {
    private String user_name;
    private String store_name;

    public Users_stores(String store_name, String user_name) {
        this.store_name = store_name;
        this.user_name = user_name;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}

