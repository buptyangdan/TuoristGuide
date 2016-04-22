package Dao;
import DB.JDBCAdapter;
import Models.Users_stores;



/**
 * Created by caoyi on 16/4/19.
 */
public class CreateUserStore extends JDBCAdapter {
    public CreateUserStore(String fileName) {
        super(fileName);
    }

    public void CreateUserStore(Users_stores users_stores){
        String sql=null;
        try{
            String store_geo=users_stores.getStore_geo();
            String email=users_stores.getEmail();
            sql="insert into users_stores(email, store_geo) values ('"+email+"','"+store_geo+"');";
            this.stmt.executeUpdate(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
