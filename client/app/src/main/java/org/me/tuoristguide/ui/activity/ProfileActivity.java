package org.me.tuoristguide.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


import com.facebook.login.widget.LoginButton;

import org.me.tuoristguide.R;
import org.me.tuoristguide.entities.UserManager;
import org.me.tuoristguide.model.User;
import org.me.tuoristguide.service.local.FacebookService;


public class ProfileActivity extends Activity implements FacebookService.OnFacebookLoggedIn{

    private LoginButton btn_login;
    private TextView nameTextview;
    private TextView emailTextview;


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
        }

    }

    @Override
    public void onFacebookLoggedIn() {
        checkUserInfo();
    }
}
