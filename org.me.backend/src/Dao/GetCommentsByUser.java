package Dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import DB.JDBCAdapter;
import Models.Comments;
import Models.Users_stores;


/*
 * Created by caoyi on 16/4/19.
 */
public class GetCommentsByUser extends JDBCAdapter {
    public GetCommentsByUser() {
        super();
    }

    public List<Comments> GetCommentsByUser(String email){
        String sql=null;
        String user_name=null;
        String photo_url=null;
        String pic_url=null;
        String comment_text=null;
        String created_time=null;
        String store_name=null;
        List<Comments> commentses=new ArrayList<Comments>();
        try{
            sql="select user_name, photo_url from user_info where email='"+email+"';";
            System.out.println(sql);
            ResultSet rSet=this.stmt.executeQuery(sql);
            if(rSet.next()){
                user_name=rSet.getString(1);
                photo_url=rSet.getString(2);
            }
            sql="select store_geo, comment_text, created_time from users_stores where email='"+email+"';";
            System.out.println(sql);
            rSet=this.stmt.executeQuery(sql);
            List<Users_stores> users_stores_list=new ArrayList<Users_stores>();
            while(rSet.next()){
            	System.out.println(rSet.getString(1));
                Users_stores users_stores=new Users_stores(email,rSet.getString(1), rSet.getString(2), rSet.getString(3));
                users_stores_list.add(users_stores);
            }
            
            for(Users_stores users_stores:users_stores_list){
                sql="select store_name, pic_url from stores_info where store_geo='"+users_stores.getStore_geo()+"';";
                System.out.println(sql);
                rSet=this.stmt.executeQuery(sql);
                if(rSet.next()){
                	store_name=rSet.getString(1);
                    pic_url=rSet.getString(2);
                }
                Comments comments=new Comments(user_name, users_stores.getComment_text(),users_stores.getCreate_time(),users_stores.getEmail(),photo_url,pic_url,store_name);
                commentses.add(comments);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
       
        return  commentses;

    }

}
