package Servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DB.CreateTable;
import Dao.CreateStore;
import Dao.CreateUser;
import Dao.CreateUserStore;
import Dao.GetCommentsByUser;
import Models.Comments;
import Models.Store_info;
import Models.User_info;
import Models.Users_stores;


/**
 * Created by caoyi on 16/4/18.
 */
public class CreateUserServlet extends HttpServlet {
     
	static String path="./dbInit.txt";
    public CreateUserServlet() {
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
//        final PrintWriter out = resp.getWriter();
//        out.write("POST method (changing data) was invoked!");
        CreateTable createTable=new CreateTable(path);
        if((createTable.hasTable("user_info"))){
        	createTable();
        }
      
        JSONObject jsonObject=new JSONObject();
        Enumeration paramNames = req.getParameterNames();
        Map<String,String> map=new HashMap<String,String>();
        while(paramNames.hasMoreElements()){
            String paramName=(String) paramNames.nextElement();
            String[] paramValue=req.getParameterValues(paramName);
            map.put(paramName,paramValue[0]);
        }

        String mode=map.get("Mode");
        System.out.println(mode + "|||||");
        if(mode.equals("user_info")){
            String user_name=map.get("user_name");
            String email=map.get("email");
            String photo_url=map.get("photo_url");
            User_info user=new User_info(user_name,email,photo_url);
            CreateUser createUser=new CreateUser(path);
            createUser.CreateUser(user);
        }else if(mode.equals("stores_info")){
            String store_name=map.get("store_name");
            String pic_url=map.get("pic_url");
            String comment_txt=map.get("comment_text");
            String create_time=String.valueOf(new Date());
            Store_info store_info=new Store_info(store_name,comment_txt,pic_url,create_time);
            CreateStore createStore=new CreateStore(path);
            createStore.CreateStore(store_info);
        }else if(mode.equals("users_stores")){
            String store_name=map.get("store_name");
            String user_name=map.get("user_name");
            Users_stores users_stores=new Users_stores(store_name,user_name);
            CreateUserStore createUserStore=new CreateUserStore(path);
            createUserStore.CreateUserStore(users_stores);
        }
    }
    public void createTable(){
    	CreateTable createTable=new CreateTable(path);
        createTable.CreateTableShema("/Users/caoyi/Documents/workspace-lunaee/org.me.backend/CreateTables.txt");
    }
   
    @SuppressWarnings("unchecked")
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
     
        List<JSONObject> jsonObjects=new ArrayList<JSONObject>();
        Enumeration paramNames = req.getParameterNames();
        Map<String,String> map=new HashMap<String,String>();
        while(paramNames.hasMoreElements()){
            String paramName=(String) paramNames.nextElement();
            String[] paramValue=req.getParameterValues(paramName);
            map.put(paramName,paramValue[0]);
        }
        String mode=map.get("Mode");
        System.out.println(mode+"======");
        if(mode.equals("byUser")){
            //select by username
           GetCommentsByUser getCommentsByUser=new GetCommentsByUser(path);
           String user_name=map.get("user_name");
           List<Comments> commentses= getCommentsByUser.GetCommentsByUser(user_name);
            for(Comments comment: commentses){
            	JSONObject jsonObject=new JSONObject();
                jsonObject.put("user_name",comment.getUser_name());
                jsonObject.put("store_name",comment.getStore_name());
                jsonObject.put("comment_txt",comment.getComment_txt());
                jsonObject.put("created_time",comment.getCreated_time());
                jsonObject.put("photo_url",comment.getPhoto_url());
                jsonObject.put("pic_url",comment.getPic_url());
                jsonObject.put("email",comment.getEmail());
                jsonObjects.add(jsonObject);
            }

        }else if(mode.equals("byStore")){
            //select by storename

        }
         System.out.println(jsonObjects.toString());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonObjects.toString());

    }

   
}
