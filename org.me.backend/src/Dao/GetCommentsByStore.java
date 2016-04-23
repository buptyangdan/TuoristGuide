package Dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import DB.JDBCAdapter;
import Models.Comments;
import Models.Store_info;
import Models.Users_stores;


/**
 * Created by caoyi on 16/4/19.
 */
public class GetCommentsByStore extends JDBCAdapter {
    public GetCommentsByStore() {
        super();
    }
    public List<Comments> GetCommentsByStore(String store_geo){
        String sql=null;
        String photo_url=null;
        String pic_url=null;
        String comment_text=null;
        String comment_user=null;
        String created_time=null;
        String store_name=null;
        List<Comments> commentses=new ArrayList<Comments>();
        try{
        	sql="select store_name, pic_url from stores_info where store_geo='"+store_geo+"';";
        	System.out.println(sql);
        	 ResultSet rSet=this.stmt.executeQuery(sql);
        	 if(rSet.next()){
        		 store_name=rSet.getString(1);
        		 pic_url=rSet.getString(2);
        	 }
             String sql1="select email, store_geo, comment_text, created_time from users_stores where store_geo='"+store_geo+"';";
             System.out.println(sql1);
             ResultSet rSet1=this.stmt.executeQuery(sql1);
             List<Users_stores> users_stores_list=new ArrayList<Users_stores>();
             while(rSet1.next()){
            	 Users_stores users_stores=new Users_stores(rSet1.getString(1), rSet1.getString(2), rSet1.getString(3), rSet1.getString(4));
                 users_stores_list.add(users_stores);
             }
             for(Users_stores users_stores:users_stores_list){
            	 String sql2="select user_name, photo_url from user_info where email='"+users_stores.getEmail()+"';";
               	 System.out.println(sql2);
                 ResultSet rSet2=this.stmt.executeQuery(sql2);
                 if(rSet2.next()){
                 	 comment_user=rSet2.getString(1);
                 	 photo_url=rSet2.getString(2);
                 }
                 Comments comments=new Comments(comment_user, users_stores.getComment_text(),users_stores.getCreate_time(),users_stores.getEmail(),photo_url,pic_url,store_name);
                 commentses.add(comments);
             }
             
        }catch (Exception e){
            e.printStackTrace();
        }
        return  commentses;

    }

}
