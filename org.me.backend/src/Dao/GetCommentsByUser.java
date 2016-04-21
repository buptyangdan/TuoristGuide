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

    public List<Comments> GetCommentsByUser(String user_name){
        String sql=null;
        String email=null;
        String photo_url=null;
        String pic_url=null;
        String comment_text=null;
        String created_time=null;
        List<Comments> commentses=new ArrayList<Comments>();
        try{
            sql="select email, photo_url from user_info where user_name='"+user_name+"';";
            System.out.println(sql);
            ResultSet rSet=this.stmt.executeQuery(sql);
            if(rSet.next()){
                email=rSet.getString(1);
                photo_url=rSet.getString(2);
            }
            sql="select store_name from users_stores where user_name='"+user_name+"';";
            System.out.println(sql);
            rSet=this.stmt.executeQuery(sql);
            List<String> store_names=new ArrayList<String>();
            while(rSet.next()){
                store_names.add(rSet.getString(1));
                //store_name=rSet.getString(1);
            }
            for(String name:store_names){
                sql="select pic_url, comment_text, created_time from stores_info where store_name='"+name+"';";
                System.out.println(sql);
                rSet=this.stmt.executeQuery(sql);
                if(rSet.next()){
                    pic_url=rSet.getString(1);
                    comment_text=rSet.getString(2);
                    created_time=rSet.getString(3);
                    System.out.println(comment_text);
                }
                Comments comments=new Comments(comment_text,created_time,email,photo_url,pic_url,name,user_name);
                commentses.add(comments);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return  commentses;

    }

}
