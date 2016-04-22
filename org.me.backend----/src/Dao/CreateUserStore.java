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
            String user_name=users_stores.getUser_name();
            String store_name=users_stores.getStore_name();
            sql="insert into users_stores(user_name, store_name) values ('"+user_name+"','"+store_name+"');";
            this.stmt.executeUpdate(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
