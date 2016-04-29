package org.me.tuoristguide.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
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
import org.me.tuoristguide.entities.CommentManager;
import org.me.tuoristguide.entities.StoreManager;
import org.me.tuoristguide.entities.UserManager;
import org.me.tuoristguide.model.Comment;
import org.me.tuoristguide.model.CommentList;
import org.me.tuoristguide.model.Store;
import org.me.tuoristguide.service.remote.CommentService;
import org.me.tuoristguide.service.remote.StoreService;
import org.me.tuoristguide.ui.adapter.CommentsAdapter;
import org.me.tuoristguide.util.NetworkConnector;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zy on 4/12/16.
 */
public class DetailActivity extends Activity implements CommentService.CommentServiceInterface {

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
    private ListView IvComment;
    private CommentsAdapter adapter;
    private List<CommentList> mCommentList = new ArrayList<CommentList>();
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
        setContentView(R.layout.activity_detail);
        Store store= StoreManager.getInstance().getCurrent_store();
         store_name =store.store_name;
         store_id=store.store_id;
         store_pic=store.store_pic;
         store_ratting=store.store_rating;
         storeNameText = (TextView) findViewById(R.id.store_name);
         place_image=(ImageView)findViewById(R.id.place_image);
         storeRattingText=(TextView)findViewById(R.id.store_rate);
         commentText=(TextView)findViewById(R.id.comment_content);
         IvComment = (ListView)findViewById(R.id.listview_comment);
        setListViewScrollable(IvComment);
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
        getComments(store_id);
        // test butotn
        submitButton = (Button)findViewById(R.id.submit);
        submitButton.setOnClickListener(new OnSubmitButtonClicked());
        adapter = new CommentsAdapter(getApplicationContext(),mCommentList);
        IvComment.setAdapter(adapter);

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
        CommentService.getInstance().setController(this);
        CommentService.getInstance().getByStore(store_id);
    }

    @Override
    public void getJsonResponse(JSONObject jsonObject) {
            System.out.println("here is the json response!!!");
            JSONArray arrayObj=null;
            if(jsonObject!=null){

                try {
                    String store_name=jsonObject.getString("store_name");
                    String users=jsonObject.getString("users");
                     arrayObj=new JSONArray(users);
                     for(int i=0;i<arrayObj.length();i++){
                         JSONObject json=arrayObj.getJSONObject(i);
                         System.out.println(json.getString("user_name"));
                         System.out.println(json.getString("photo_url"));
                         mCommentList.add(new CommentList(json.getString("user_name"), json.getString("photo_url"), i + "", store_name, json.getString("comment_txt"), json.getString("created_time")));
                         adapter.notifyDataSetChanged();
                     }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
    }

    // listeners

    private class OnSubmitButtonClicked implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // register for callback of clicking button
           // NetworkConnector.getInstance().getJsonData("/promos/editor", new OnReceiveJSONResponse());
            //save the comment and store_info into the server
           //if left comment, means the user has gone to the place
            comment_content=String.valueOf(commentText.getText());
            //use adapter to inflate the view to viewpage
            if(comment_content!=null&& UserManager.getInstance().getCurrentUser()!=null){
                Comment comment=new Comment(comment_content,String.valueOf(new Date()), UserManager.getInstance().getCurrentUser().email,StoreManager.getInstance().getCurrent_store().store_id);
                mCommentList.add(new CommentList(UserManager.getInstance().getCurrentUser().name, UserManager.getInstance().getCurrentUser().picture_url, "1",StoreManager.getInstance().getCurrent_store().store_name , comment.comment_text, comment.created_time));
               //adapter = new CommentsAdapter(getApplicationContext(),mCommentList);
                adapter.notifyDataSetChanged();
                StoreService.getInstance().CreateStore(StoreManager.getInstance().getCurrent_store());
                StoreService.getInstance().CreateUserStore(comment);
            }else{
                 Toast.makeText(getBaseContext(),"Please login!", Toast.LENGTH_LONG).show();
            }

        }
    }


}
