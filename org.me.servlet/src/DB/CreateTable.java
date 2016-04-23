package DB;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by caoyi on 16/4/19.
 */
public class CreateTable extends JDBCAdapter{
    public CreateTable() {
        super();
    }

    //read file and create the table
    public  void CreateTableShema(String fileName){
        // createTable schema
        try {
        	System.out.println("createtables");
            BufferedReader br=new BufferedReader(new FileReader(new File(fileName)));
            String line;
            StringBuffer sb;
            boolean eof=false;
            while(!eof){

                sb = new StringBuffer();
                line = br.readLine();
                if (line == null){
                    eof = true;
                }else if (line.length()!=0){
                    while(line.indexOf(';')==-1){
                        sb.append(line);
                        line = br.readLine();
                    }
                    sb.append(line);
                    this.stmt.executeUpdate(sb.toString());
                }
            }
            br.close();
        } catch (IOException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void dropTable(){
        //drop the table if the table exist
        try{

            System.out.println("delete tables if exists");
            String sql="DROP TABLE IF EXISTS `stores_info`;";
            this.stmt.execute(sql);
             sql="DROP TABLE IF EXISTS `user_info`;";
            this.stmt.execute(sql);
             sql="DROP TABLE IF EXISTS `users_stores`;";
            this.stmt.execute(sql);

        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public boolean hasTable(String tableName){
        DatabaseMetaData dbm;
        try {
            dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, tableName, null);
            if (tables.next()) {
                // Table exists
                return false;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // check if "employee" table is there
        return true;

    }


}


