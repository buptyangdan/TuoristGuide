package DB;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by caoyi on 16/4/19.
 */
public class JDBCAdapter {
    //this class is used to connect to the database; it also includes the operations for car

    public Connection connection;
    public Statement stmt;
   // public ResultSet rSet;
    //constructor connect to the database


    public JDBCAdapter(String filename) {
        // TODO Auto-generated constructor stub
   

        try {

            String url="jdbc:mysql://localhost:3306/ToursitGuide";
            String Driver="com.mysql.jdbc.Driver";
            String username="root";
            String password="root";
            Class.forName(Driver);
            connection = DriverManager.getConnection(url, username, password);
            stmt = connection.createStatement();

        } catch ( ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public void CloseDB(){
        try {
            this.connection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
