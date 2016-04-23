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
import Dao.GetCommentsByStore;
import Dao.GetCommentsByUser;
import Models.Comments;
import Models.Store_info;
import Models.User_info;
import Models.Users_stores;


/**
 * Created by caoyi on 16/4/18.
 */
public class CreateUserServlet extends HttpServlet {
     
    public CreateUserServlet() {
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       
//        final PrintWriter out = resp.getWriter();
//        out.write("POST method (changing data) was invoked!");
        CreateTable createTable=new CreateTable();
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
            CreateUser createUser=new CreateUser();
            createUser.CreateUser(user);
        }else if(mode.equals("stores_info")){
            String store_name=map.get("store_name");
            String pic_url=map.get("pic_url");
            String store_geo=map.get("store_geo");
            Store_info store_info=new Store_info(store_name, pic_url, store_geo);
            CreateStore createStore=new CreateStore();
            createStore.CreateStore(store_info);
        }else if(mode.equals("users_stores")){
            String store_geo=map.get("store_geo");
            String email=map.get("email");
            String comment_text=map.get("comment_text");
            String create_time=String.valueOf(new Date());
            Users_stores users_stores=new Users_stores(email, store_geo, comment_text, create_time);
            CreateUserStore createUserStore=new CreateUserStore();
            createUserStore.CreateUserStore(users_stores);
        }
    }
    public void createTable(){
    	CreateTable createTable=new CreateTable();
        createTable.CreateTableShema("/Users/caoyi/Desktop/TuoristGuide/org.me.servlet/CreateTables.txt");
    }
   
    @SuppressWarnings("unchecked")
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {   
        Enumeration paramNames = req.getParameterNames();
        Map<String,String> map=new HashMap<String,String>();
        while(paramNames.hasMoreElements()){
            String paramName=(String) paramNames.nextElement();
            String[] paramValue=req.getParameterValues(paramName);
            map.put(paramName,paramValue[0]);
        }
        String mode=map.get("Mode");
        System.out.println(mode+"======");
        JSONObject result=new JSONObject();
        if(mode.equals("byUser")){
            //select by username
           GetCommentsByUser getCommentsByUser=new GetCommentsByUser();
           String email=map.get("email");
           JSONObject jsonObject=new JSONObject();
           List<Comments> commentses= getCommentsByUser.GetCommentsByUser(email);
           List<JSONObject> jsonObjects=new ArrayList<JSONObject>();
            for(Comments comment: commentses){
            	JSONObject buffjsonObject=new JSONObject();
                buffjsonObject.put("comment_txt",comment.getComment_txt());
                buffjsonObject.put("created_time",comment.getCreated_time());
                buffjsonObject.put("store_name", comment.getStore_name());
                buffjsonObject.put("pic_url", comment.getPic_url());
                jsonObjects.add(buffjsonObject);
            }
           result.put("stores", jsonObjects);
           result.put("user_name",commentses.get(0).getComment_user());
           result.put("poto_url", commentses.get(0).getPhoto_url());
           result.put("email", commentses.get(0).getEmail());
          
        }else if(mode.equals("byStore")){
            //select by storename
        	GetCommentsByStore getCommentsByStore=new GetCommentsByStore();
        	String store_geo=map.get("store_geo");
        	JSONObject jsonObject=new JSONObject();
        	List<Comments> commentes=getCommentsByStore.GetCommentsByStore(store_geo);
            List<JSONObject> jsonObjects=new ArrayList<JSONObject>();
            for(Comments comment: commentes){
            	JSONObject buffjsonObject=new JSONObject();
                buffjsonObject.put("user_name",comment.getComment_user());
                buffjsonObject.put("email",comment.getEmail());
                buffjsonObject.put("comment_txt",comment.getComment_txt());
                buffjsonObject.put("comment_user",comment.getComment_user());
                buffjsonObject.put("created_time",comment.getCreated_time());
                buffjsonObject.put("photo_url",comment.getPhoto_url());
                jsonObjects.add(buffjsonObject);
            }
            result.put("users", jsonObjects);
            result.put("store_name",commentes.get(0).getStore_name());
            result.put("pic_url", commentes.get(0).getPic_url());
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(result.toString());

    }

   
}
