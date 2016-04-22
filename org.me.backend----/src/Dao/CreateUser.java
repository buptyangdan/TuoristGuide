package Dao;

import Models.User_info;


/**
 * Created by caoyi on 16/4/19.
 */
public class CreateUser extends DB.JDBCAdapter {


    public CreateUser(String fileName) {
        super(fileName);
    }

    /***
     * This method is used to insert user into db
     * @param user
     */
    public void CreateUser(User_info user){
        String sql=null;
        try{
            String user_name=user.getUser_name();
            String email=user.getEmail();
            String photo_url=user.getPhoto_url();
            sql="insert into user_info (user_name, email, photo_url) values ('"+user_name+"','"+email+"','"+photo_url+"');";
            System.out.println(sql);
            this.stmt.executeUpdate(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
