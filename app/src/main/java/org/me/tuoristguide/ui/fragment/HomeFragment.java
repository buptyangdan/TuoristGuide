package org.me.tuoristguide.ui.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.login.widget.LoginButton;

import org.me.tuoristguide.R;
import org.me.tuoristguide.ui.activity.Login;

public class HomeFragment extends Fragment {

	public HomeFragment(){}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        LoginButton btn_facebook=(LoginButton) rootView.findViewById(R.id.facebook_login);
        Login.btn_login=btn_facebook;
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        btn_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accessToken != null) {
                    //  LoginManager.getInstance().logOut();
                }

            }


        });

        return rootView;
    }
}
