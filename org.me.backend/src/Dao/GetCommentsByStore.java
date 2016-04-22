package Dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import DB.JDBCAdapter;
import Models.Comments;
import Models.Store_info;


/**
 * Created by caoyi on 16/4/19.
 */
public class GetCommentsByStore extends JDBCAdapter {
    public GetCommentsByStore(String fileName) {
        super(fileName);
    }
    public List<Comments> GetCommentsByStore(String store_geo){
        String sql=null;
        String user_name=null;
        String photo_url=null;
        String pic_url=null;
        String comment_text=null;
        String created_time=null;
        String store_name=null;
        List<Comments> commentses=new ArrayList<Comments>();
        try{
        	sql="select store_name, pic_url, comment_text, created_time from stores_info where store_geo='"+store_geo+"';";
        	System.out.println(sql);
        	 ResultSet rSet=this.stmt.executeQuery(sql);
        	 int i=0;
        	 List<Store_info> stores=new ArrayList<Store_info>();
        	 while(rSet.next()){
        		 Store_info store_info=new Store_info(store_geo, rSet.getString(1), rSet.getString(3), rSet.getString(2), rSet.getString(4));
        	     stores.add(store_info);
        	 }
             for(Store_info store:stores){
            	 store_name=store.getStore_name();
                 pic_url=store.getPic_url();
                 comment_text=store.getComment_text();
                 System.out.println(comment_text);
                 created_time=store.getCreated_time();
                 String sql1="select email from users_stores where store_geo='"+store_geo+"';";
                 System.out.println(sql1);
                 ResultSet rSet1=this.stmt.executeQuery(sql1);
                 List<String> store_geos=new ArrayList<String>();
                 while(rSet1.next()){
                     store_geos.add(rSet1.getString(1));
                 }
                   String email=store_geos.get(i);
                 	String sql2="select user_name, photo_url from user_info where email='"+email+"';";
                 	System.out.println(sql2);
                    ResultSet rSet2=this.stmt.executeQuery(sql2);
                     if(rSet2.next()){
                         user_name=rSet2.getString(1);
                         photo_url=rSet2.getString(2);
                     }
                 i++;
                 Comments comments=new Comments(comment_text,created_time,email,photo_url,pic_url,store_name,user_name);
                 commentses.add(comments);
             
             }
            
           
        }catch (Exception e){
            e.printStackTrace();
        }
//        for(Comments comments:commentses){
//        	System.out.println(comments.getComment_txt());
//        }
        return  commentses;

    }

}
