package org.me.tuoristguide.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.me.tuoristguide.R;
import org.me.tuoristguide.entities.CommentManager;
import org.me.tuoristguide.entities.StoreManager;
import org.me.tuoristguide.entities.UserManager;
import org.me.tuoristguide.model.Comment;
import org.me.tuoristguide.model.Store;
import org.me.tuoristguide.service.remote.StoreService;
import org.me.tuoristguide.util.NetworkConnector;

import java.util.Date;

/**
 * Created by zy on 4/12/16.
 */
public class DetailActivity extends Activity {

    private TextView storeNameText;
    private Button submitButton;
    private String store_id;
    private String store_name;
    private String store_pic;
    private Double store_ratting;
    private ImageView place_image;
    private TextView storeRattingText;
    private  TextView commentText;
    private String comment_content;
    StoreService storeService=new StoreService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Store store= StoreManager.getInstance().getCurrent_store();
        System.out.println("goodplace!");
        System.out.println(store.store_name + ";" + store.store_id + ";" + store.store_pic);
         store_name =store.store_name;
         store_id=store.store_id;
         store_pic=store.store_pic;
         store_ratting=store.store_rating;
         storeNameText = (TextView) findViewById(R.id.store_name);
         place_image=(ImageView)findViewById(R.id.place_image);
         storeRattingText=(TextView)findViewById(R.id.store_rate);
         commentText=(TextView)findViewById(R.id.comment_content);

        if (storeNameText != null) {
            storeNameText.setText(store_name);
        }
        if(storeRattingText!=null){
            storeRattingText.setText(String.valueOf(store_ratting));
        }
        if(place_image!=null){
            Picasso.with(this).load(store_pic)
                    .into(place_image);
        }
        if(commentText!=null){
            comment_content=String.valueOf(commentText.getText());
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
           // NetworkConnector.getInstance().getJsonData("/promos/editor", new OnReceiveJSONResponse());
            //save the comment and store_info into the server

           //if left comment, means the user has gone to the place
            storeService.CreateStore(StoreManager.getInstance().getCurrent_store());
            Comment comment=new Comment(comment_content,String.valueOf(new Date()), UserManager.getInstance().getCurrentUser().email,StoreManager.getInstance().getCurrent_store().store_id);
            storeService.CreateUserStore(comment);

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
