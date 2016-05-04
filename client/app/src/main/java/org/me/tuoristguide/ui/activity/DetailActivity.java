package org.me.tuoristguide.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.me.tuoristguide.R;
import org.me.tuoristguide.entities.StoreManager;
import org.me.tuoristguide.entities.UserManager;
import org.me.tuoristguide.model.Comment;
import org.me.tuoristguide.model.CommentListItem;
import org.me.tuoristguide.model.Store;
import org.me.tuoristguide.service.remote.CommentService;
import org.me.tuoristguide.service.remote.StoreService;
import org.me.tuoristguide.ui.adapter.CommentsAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by zy on 4/12/16.
 */

@ContentView(R.layout.activity_detail)
public class DetailActivity extends RoboActivity implements CommentService.CommentServiceInterface {

    private String store_id;
    private String store_name;
    private Store store;

    @InjectView(R.id.submit)            private Button submitButton;
    @InjectView(R.id.store_name)        private TextView storeNameText;
    @InjectView(R.id.place_image)       private ImageView place_image;
    @InjectView(R.id.store_rate)        private TextView storeRattingText;
    @InjectView(R.id.comment_content)   private TextView commentText;
    @InjectView(R.id.listview_comment)  private ListView IvComment;

    private CommentsAdapter adapter;

    private void setListViewScrollable(final ListView list) {
        list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        store = StoreManager.getInstance().getCurrent_store();
        store_name = store.store_name;
        store_id = store.store_id;
    }

    @Override
    public void onResume() {

        super.onResume();
        if (storeNameText != null) {
            storeNameText.setText(store_name);
        }
        if(storeRattingText!=null){
            storeRattingText.setText(String.valueOf(store.store_rating));
        }
        if(place_image!=null){
            Picasso.with(this).load(store.store_pic).into(place_image);
        }
        submitButton.setOnClickListener(new OnSubmitButtonClicked());

        adapter = new CommentsAdapter(this);
        setListViewScrollable(IvComment);
        IvComment.setAdapter(adapter);

        getComments(store_id);
    }


    private void getComments(String store_id) {
        // get all comments of one store from server
        CommentService.getInstance().setController(this);
        CommentService.getInstance().getByStore(store_id);
    }

    @Override
    public void getJsonResponse(JSONObject jsonObject) {
        System.out.println("here is the json response!!!");

        if(jsonObject != null){
            try {
                String store_name = jsonObject.getString("store_name");
                JSONArray users = jsonObject.getJSONArray("users");
                for(int i = 0;i<users.length();i++) {
                    JSONObject json = users.getJSONObject(i);
                    adapter.add(new CommentListItem(json.getString("user_name"), json.getString("photo_url"), i + "", store_name, json.getString("comment_txt"), json.getString("created_time")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }

    // listeners
    private class OnSubmitButtonClicked implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // register for callback of clicking button
            // NetworkConnector.getInstance().getJsonData("/promos/editor", new OnReceiveJSONResponse());
            //save the comment and store_info into the server
            //if left comment, means the user has gone to the place
            String comment_content=String.valueOf(commentText.getText());
            //use adapter to inflate the view to viewpage
            if(comment_content!=null&& UserManager.getInstance().getCurrentUser()!=null){
                Comment comment=new Comment(comment_content,String.valueOf(new Date()), UserManager.getInstance().getCurrentUser().email,StoreManager.getInstance().getCurrent_store().store_id);
                //Toast.makeText(getApplicationContext(),StoreManager.getInstance().getCurrent_store().store_name ,Toast.LENGTH_LONG).show();
                adapter.add(new CommentListItem(UserManager.getInstance().getCurrentUser().name, UserManager.getInstance().getCurrentUser().picture_url, "1", StoreManager.getInstance().getCurrent_store().store_name, comment.comment_text, comment.created_time));
                //adapter = new CommentsAdapter(getApplicationContext(),mCommentList);
                adapter.notifyDataSetChanged();
                StoreService.getInstance().CreateStore(StoreManager.getInstance().getCurrent_store());
                StoreService.getInstance().CreateUserStore(comment);

                commentText.clearFocus();
                commentText.setText("");
                // dismiss keyboard
                View view = DetailActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }else{
                Toast.makeText(getBaseContext(),"Please login!", Toast.LENGTH_LONG).show();
            }

        }
    }


}
