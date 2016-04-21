package Dao;

import DB.JDBCAdapter;
import Models.Store_info;


/**
 * Created by caoyi on 16/4/19.
 */
public class CreateStore extends JDBCAdapter {
    public CreateStore(String fileName) {
        super(fileName);
    }
    /***
     * This method is used to insert user into db
     * @param
     */
    public void CreateStore(Store_info store){
        String sql=null;
        try{
           String store_name=store.getStore_name();
           String comment_txt=store.getComment_text();
           String pic_url=store.getPic_url();
           String create_time=store.getCreated_time();
            sql="insert into stores_info (store_name,pic_url, comment_text, created_time) values ('"+store_name+"','"+pic_url+"','"+comment_txt+"','"+create_time+"');";
            System.out.println(sql);
            this.stmt.executeUpdate(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
