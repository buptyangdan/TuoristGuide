package Models;

/**
 * Created by caoyi on 16/4/19.
 */
public class User_info {
    private String user_name;
    private String email;
    private String photo_url;

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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public User_info(String user_name, String email, String photo_url) {
        this.user_name = user_name;
        this.email = email;
        this.photo_url = photo_url;
    }
}
