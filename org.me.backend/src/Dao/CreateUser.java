package Dao;

import java.sql.ResultSet;

import Models.User_info;


/**
 * Created by caoyi on 16/4/19.
 */
public class CreateUser extends DB.JDBCAdapter {


    public CreateUser() {
        super();
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
            sql="select user_name, photo_url from user_info where email='"+email+"';";
            ResultSet rSet=this.stmt.executeQuery(sql);
            if(rSet.next()){
            	//this means the db already has the user, then update the user
            	sql="UPDATE user_info SET user_name ='"+user_name+"',photo_url='"+photo_url+"' where email ='"+email+"';";
            	System.out.println(sql);
    		    this.stmt.executeUpdate(sql);	
            	return ;
            }else{
              	 sql="insert into user_info (user_name, email, photo_url) values ('"+user_name+"','"+email+"','"+photo_url+"');";
                 System.out.println(sql);
                 this.stmt.executeUpdate(sql);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
