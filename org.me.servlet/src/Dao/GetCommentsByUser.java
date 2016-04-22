package Dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import DB.JDBCAdapter;
import Models.Comments;


/*
 * Created by caoyi on 16/4/19.
 */
public class GetCommentsByUser extends JDBCAdapter {
    public GetCommentsByUser(String fileName) {
        super(fileName);
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
            sql="select store_geo from users_stores where email='"+email+"';";
            System.out.println(sql);
            rSet=this.stmt.executeQuery(sql);
            List<String> store_geos=new ArrayList<String>();
            while(rSet.next()){
            	System.out.println(rSet.getString(1));
                store_geos.add(rSet.getString(1));
                //store_name=rSet.getString(1);
            }
            for(String geo:store_geos){
                sql="select store_name, pic_url, comment_text, created_time from stores_info where store_geo='"+geo+"';";
                System.out.println(sql);
                rSet=this.stmt.executeQuery(sql);
                if(rSet.next()){
                	store_name=rSet.getString(1);
                    pic_url=rSet.getString(2);
                    comment_text=rSet.getString(3);
                    created_time=rSet.getString(4);
                }
                Comments comments=new Comments(comment_text,created_time,email,photo_url,pic_url,store_name,user_name);
                commentses.add(comments);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
       
        return  commentses;

    }

}
