package org.me.tuoristguide.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.me.tuoristguide.R;
import org.me.tuoristguide.entities.UserManager;
import org.me.tuoristguide.model.CommentList;
import org.me.tuoristguide.model.User;
import org.me.tuoristguide.service.local.FacebookService;
import org.me.tuoristguide.ui.adapter.CommentsAdapter;

import java.util.ArrayList;
import java.util.List;


public class ProfileActivity extends Activity implements FacebookService.OnFacebookLoggedIn{

    private LoginButton btn_login;
    private TextView nameTextview;
    private TextView emailTextview;
    private ImageView photoImageview;
    private ListView IvComment;
    private CommentsAdapter adapter;
    private List<CommentList> mCommentList = new ArrayList<CommentList>();


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
        photoImageview=(ImageView) findViewById(R.id.profile_picture);

        //add sample data for list;
        //we can get data from DB
        mCommentList.add(new CommentList("user","lll","1","SafeWay","Very good!","2015-01-12"));
        mCommentList.add(new CommentList("user","lll","2","Appstore","Not bad!","2015-04-12"));

        adapter = new CommentsAdapter(getApplicationContext(),mCommentList);
        IvComment = (ListView)findViewById(R.id.listview_comment);
        IvComment.setAdapter(adapter);

        IvComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Clicked Store = " + view.getTag(), Toast.LENGTH_SHORT).show();
            }
        });
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
            nameTextview.setText(user.name);
            emailTextview.setText(user.email);
            Picasso.with(this).load(user.picture_url)
                    .into(photoImageview);
        }

    }

    @Override
    public void onFacebookLoggedIn() {
        checkUserInfo();
    }
}

