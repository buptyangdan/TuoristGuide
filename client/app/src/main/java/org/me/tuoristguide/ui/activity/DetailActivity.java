package org.me.tuoristguide.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;
import org.me.tuoristguide.R;
import org.me.tuoristguide.util.NetworkConnector;

/**
 * Created by zy on 4/12/16.
 */
public class DetailActivity extends AppCompatActivity {

    private TextView storeNameText;
    private Button submitButton;
    private String store_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        String storename = getIntent().getExtras().getString("store_name");
        store_id = getIntent().getExtras().getString("store_id");
        String rate_string = getIntent().getExtras().getString("store_rate");

        storeNameText = (TextView) findViewById(R.id.store_name);

        if (storeNameText != null) {
            storeNameText.setText(storename);
        }


        // test butotn
        submitButton = (Button)findViewById(R.id.submit);
        submitButton.setOnClickListener(new OnSubmitButtonClicked());

    }

    @Override
    public void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

    }


    private void getComments(String store_id) {

        // get all comments of one store from server
    }

    // listeners

    private class OnSubmitButtonClicked implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // register for callback of clicking button
            NetworkConnector.getInstance().getJsonData("/promos/editor", new OnReceiveJSONResponse());
        }
    }

    private class OnReceiveJSONResponse implements NetworkConnector.OnJSONResponseListener {

        @Override
        public void onResponse(JSONObject result) {

            // do some work here after received the response JSON data
            //storeName.setText(result.toString());
        }
    }
}
