package controllers;


import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import play.libs.Json;
import play.mvc.*;
import views.html.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static play.data.Form.form;
import static play.libs.Json.toJson;

public class Application extends Controller {

    public Result index() {

        return ok(index.render());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result postUserInfo() {
        JsonNode json = request().body().asJson();
        String user_name = json.findPath("user_name").textValue();
        String email=json.findPath("email").textValue();
        String photo_url=json.findPath("photo_url").textValue();
        if(user_name == null||email==null||photo_url==null) {
            return badRequest("Missing parameter");
        } else {
             //send json data
            //save into database
            user_info user=new user_info(user_name,email,photo_url);
            user.save();
            return ok(toJson("A new user_info has been created!"));
        }
    }

//    @BodyParser.Of(BodyParser.Json.class)
//    public Result postUserComments(){
//        JsonNode json = request().body().asJson();
//        Long  user_id=json.findPath("user_id").asLong();
//        Long  store_id=json.findPath("store_id").asLong();
//        String comment_text=json.findPath("comment_text").textValue();
//        String create_time=String.valueOf(new Date());
//        if(user_id==null||store_id==null||comment_text==null){
//            return badRequest("Missing parameter");
//        }else{
//            user_activity user=new user_activity(user_id,store_id,comment_text,create_time);
//            user.save();
//            return ok(toJson("A new user_activity has been created!"));
//        }
//    }



    @BodyParser.Of(BodyParser.Json.class)
    public Result postStoreInfo(){
        JsonNode json=request().body().asJson();
        String store_name=json.findPath("store_name").textValue();
        String pic_url=json.findPath("pic_url").textValue();
        String comment_text=json.findPath("comment_text").textValue();
        String time=String.valueOf(new Date());
        if(store_name==null||pic_url==null||comment_text==null||time==null){
            return badRequest("Missing parameter");
        }else{
            stores_info store_info=new stores_info(comment_text,store_name,pic_url,time);
            store_info.save();
            return ok(toJson("A new store_info has been created!"));
        }
    }
    @BodyParser.Of(BodyParser.Json.class)
    public Result postUserStore(){
        JsonNode json=request().body().asJson();
        String store_name=json.findPath("store_name").textValue();
        String user_name = json.findPath("user_name").textValue();
        users_stores users_stores=new users_stores(user_name,store_name);
        users_stores.save();
        return ok(toJson("A new users_stores has been created!"));
    }





    //get the comments by user_name

    public Result getComments(String user_name){
        List<user_info> userInfos=user_info.find.where().eq("user_name",user_name).findList();
        String email=userInfos.get(0).getEmail();
        String photo_url=userInfos.get(0).getPhoto_url();
        Long user_id=userInfos.get(0).getId();
        System.out.println(user_id+"sdssdds");
        List<users_stores> uss=users_stores.find.where().eq("user_name",user_name).findList();
        List<String> store_names=new ArrayList<String>();
        for(users_stores  us: uss){
            store_names.add(us.getStore_name());
        }
        //find the store_info
        List<ObjectNode> results=new ArrayList<ObjectNode>();
        for(String store_name:store_names){
            ObjectNode result = Json.newObject();
            result.put("user_name",user_name);
            result.put("email",email);
            result.put("photo_url",photo_url);
            result.put("store_name",store_name);
            result.put("comment_text",stores_info.find.where().eq("store_name",store_name).findUnique().getComment_text());
            result.put("pic_url",stores_info.find.where().eq("store_name",store_name).findUnique().getPic_url());
            results.add(result);
        }
        return ok(Json.toJson(results));
    }












}
