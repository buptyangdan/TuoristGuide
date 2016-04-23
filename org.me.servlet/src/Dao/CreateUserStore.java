package Dao;
import DB.JDBCAdapter;
import Models.Users_stores;



/**
 * Created by caoyi on 16/4/19.
 */
public class CreateUserStore extends JDBCAdapter {
    public CreateUserStore() {
        super();
    }

    public void CreateUserStore(Users_stores users_stores){
        String sql=null;
        try{
            String store_geo=users_stores.getStore_geo();
            String email=users_stores.getEmail();
            String comment_text=users_stores.getComment_text();
            String created_time=users_stores.getCreate_time();
            sql="insert into users_stores(email, store_geo, comment_text, created_time) values ('"+email+"','"+store_geo+"','"+comment_text+"','"+created_time+"');";
            this.stmt.executeUpdate(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
