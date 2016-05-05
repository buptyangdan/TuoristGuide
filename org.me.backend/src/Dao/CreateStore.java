package Dao;

import java.sql.ResultSet;

import DB.JDBCAdapter;
import Models.Store_info;


/**
 * Created by caoyi on 16/4/19.
 */
public class CreateStore extends JDBCAdapter {
    public CreateStore() {
        super();
    }
    /***
     * This method is used to insert user into db
     * @param
     */
    public void CreateStore(Store_info store){
        String sql=null;
        try{
           String store_geo=store.getStore_geo();	
           String store_name=store.getStore_name();
           String pic_url=store.getPic_url();
           sql="select store_name, pic_url from stores_info where store_geo="+store_geo;
           ResultSet rSet=this.stmt.executeQuery(sql);
           if(rSet.next()){
           	//this means the db already has the store, then update the user
        	sql="UPDATE stores_info SET  store_name='"+store_name+"',pic_url='"+pic_url+"' where store_geo ='"+store_geo+"';";
           	System.out.println(sql);
   		    this.stmt.executeUpdate(sql);	
           	  return ;	
           }else{sql="insert into stores_info (store_geo, store_name, pic_url) values ('"+store_geo+"','"+store_name+"','"+pic_url+"');";
            System.out.println(sql);
            this.stmt.executeUpdate(sql);
           }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
