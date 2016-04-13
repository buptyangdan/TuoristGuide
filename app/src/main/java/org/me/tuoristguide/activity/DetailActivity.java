package org.me.tuoristguide.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.me.tuoristguide.R;

/**
 * Created by zy on 4/12/16.
 */
public class DetailActivity extends AppCompatActivity {

    private TextView storeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        String storename = getIntent().getExtras().getString("store_name");

        storeName = (TextView) findViewById(R.id.store_name);

        storeName.setText(storename);

    }

    @Override
    public void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

    }




}
