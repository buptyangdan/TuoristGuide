package org.me.tuoristguide.ui.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.me.tuoristguide.R;
import org.me.tuoristguide.database.AndroidDatabaseManager;
import org.me.tuoristguide.database.DatabaseConnector;
import org.me.tuoristguide.entities.UserManager;
import org.me.tuoristguide.model.Comment;
import org.me.tuoristguide.model.CommentList;
import org.me.tuoristguide.model.User;
import org.me.tuoristguide.service.local.FacebookService;
import org.me.tuoristguide.service.remote.CommentService;
import org.me.tuoristguide.ui.adapter.CommentsAdapter;

import java.util.ArrayList;
import java.util.List;


public class ProfileActivity extends Activity implements FacebookService.OnFacebookLoggedIn, CommentService.CommentServiceInterface{

    private LoginButton btn_login;
    private TextView nameTextview;
    private TextView emailTextview;
    private ImageView photoImageview;
    private ListView IvComment;
    private CommentsAdapter adapter;
    private List<CommentList> mCommentList = new ArrayList<CommentList>();
    private PopupWindow mPopupWindow;
    private ImageButton savePassword;
    private ImageButton nsavePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init Facebook Service here
        FacebookService.getInstance(this);

        // then inflate the view
        setContentView(R.layout.activity_login);
        // set up login button
        btn_login = (LoginButton) findViewById(R.id.btn_login);
        FacebookService.getInstance(this).setupFBLoginButton(btn_login);

        // set up other components
        nameTextview = (TextView) findViewById(R.id.user_name);
        emailTextview = (TextView) findViewById(R.id.user_email);
        photoImageview = (ImageView) findViewById(R.id.profile_picture);
        IvComment = (ListView) findViewById(R.id.listview_comment);
        //add sample data for list;
        //we can get data from DB
        CommentService.getInstance().setController(this);





}



    @Override
    protected void onResume() {
        super.onResume();
        checkUserInfo();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        FacebookService.getInstance(this).onActivityResult(requestCode, resultCode, data);
    }
    private void checkUserInfo() {
        User user = UserManager.getInstance().getCurrentUser();
        if (user != null){
            CommentService.getInstance().getByUser(user.email);
            nameTextview.setText(user.name);
            emailTextview.setText(user.email);
            Picasso.with(this).load(user.picture_url)
                    .into(photoImageview);
           initPopupWindow();
        }

    }

    @Override
    public void onFacebookLoggedIn() {
        checkUserInfo();
    }

    @Override
    public void getJsonResponse(JSONObject jsonObject) {
           System.out.print("here is the by user response!");
           System.out.println(jsonObject);
           JSONArray arrayObj=null;

        try {
            String user_name=jsonObject.getString("user_name");
            String photo_url=jsonObject.getString("poto_url");
            String stores=jsonObject.getString("stores");
            arrayObj=new JSONArray(stores);
            for(int i=0;i<arrayObj.length();i++){
                JSONObject json=arrayObj.getJSONObject(i);
                mCommentList.add(new CommentList(user_name, photo_url, i+"" , json.getString("store_name"),json.getString("comment_txt"), json.getString("created_time")));
                adapter = new CommentsAdapter(getApplicationContext(),mCommentList);
                IvComment.setAdapter(adapter);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
           adapter = new CommentsAdapter(getApplicationContext(),mCommentList);
           IvComment = (ListView)findViewById(R.id.listview_comment);
           IvComment.setAdapter(adapter);
    }

    public  void initPopupWindow(){
        Toast.makeText(getBaseContext(), "POPPPPP!", Toast.LENGTH_LONG).show();

        View popupView = getLayoutInflater().inflate(R.layout.pop_up, null);
        mPopupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow.getContentView().setFocusableInTouchMode(true);
        mPopupWindow.getContentView().setFocusable(true);
        mPopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                    return true;
                }
                return false;
            }
        });
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
            mPopupWindow.showAtLocation(findViewById(R.id.login), Gravity.BOTTOM, 0, 0);
        }
        savePassword=(ImageButton)popupView.findViewById(R.id.image_ok);
        savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Successfull!", Toast.LENGTH_LONG).show();

                User user = UserManager.getInstance().getCurrentUser();
                if (user != null) {
                    DatabaseConnector databaseConnector = new DatabaseConnector(ProfileActivity.this, "User.db", null, 2);
                    databaseConnector.setUser(user);
                    ContentValues values = databaseConnector.insertValues();
                    SQLiteDatabase database = databaseConnector.getWritableDatabase();
                    database.insert("User", null, values);
                }
            }
        });
        nsavePassword=(ImageButton)popupView.findViewById(R.id.image_cancel);
        nsavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dbmanager = new Intent(ProfileActivity.this,AndroidDatabaseManager.class);
                startActivity(dbmanager);
            }
        });
    }




}

