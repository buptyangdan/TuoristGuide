package org.me.tuoristguide.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.me.tuoristguide.R;
import org.me.tuoristguide.model.CommentList;

import java.util.List;

public class CommentsAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommentList> mCommentList;
    private  CommentsAdapterInterface controller=null;
    private ImageView imageView1,imageView2;
    private static CommentsAdapter instance=null;
    final static String LOG_TAG =  CommentsAdapter.class.getSimpleName();
    TextView tv1,tv2;

    public CommentsAdapter(Context mContext, List<CommentList> mCommentList) {

        this.mCommentList = mCommentList;
        this.mContext = mContext;
    }

    public CommentsAdapter(){

    }
    public void setController(CommentsAdapterInterface controller){
        if (instance != null)
            instance.controller = controller;

    }
    public static CommentsAdapter getInstance() {
        if(instance == null) {
            instance = new CommentsAdapter();
        }
        return instance;
    }

    @Override
    public int getCount() {
        return mCommentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCommentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.comment_item, null);
        TextView tvName = (TextView) v.findViewById(R.id.StoreNameText);
        TextView tvComment = (TextView) v.findViewById(R.id.CommentText);
        TextView tvTime = (TextView) v.findViewById(R.id.TimeText);
        final ImageView imageView = (ImageView) v.findViewById(R.id.comment_profile_picture);
        TextView tvUserName = (TextView) v.findViewById(R.id.comment_user_name);
        CardView cardView = (CardView) v.findViewById(R.id.cardview);

        imageView1 = (ImageView) v.findViewById(R.id.img_like);
        imageView1.setClickable(true);
        imageView2 = (ImageView) v.findViewById(R.id.img_dislike);
        imageView2.setClickable(true);

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = (ViewGroup) v.getParent();
                TextView tv = (TextView) viewGroup.findViewById(R.id.text_like_cnt);
                int cnt = Integer.parseInt(tv.getText().toString());
                ++cnt;
                tv.setText(Integer.toString(cnt));
                v.setClickable(false);
                ImageView im2 = (ImageView)viewGroup.findViewById(R.id.img_dislike);
                im2.setClickable(false);
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = (ViewGroup) v.getParent();
                TextView tv = (TextView) viewGroup.findViewById(R.id.text_dislike_cnt);
                int cnt = Integer.parseInt(tv.getText().toString());
                ++cnt;
                tv.setText(Integer.toString(cnt));
                v.setClickable(false);
                ImageView im1 = (ImageView)viewGroup.findViewById(R.id.img_like);
                im1.setClickable(false);
            }
        });
        cardView.setCardBackgroundColor(-1);
        tvName.setText("Store:" + mCommentList.get(position).getStore_name());
        tvComment.setText("Comment: " + mCommentList.get(position).getComment_text());
        tvTime.setText("Time: " + mCommentList.get(position).getCreated_time());
        Picasso.with(mContext).load(mCommentList.get(position).getPhoto_url())
                .into(imageView);
        tvUserName.setText(mCommentList.get(position).getComment_user());
        v.setTag(mCommentList.get(position).getComment_id());
        tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (instance.controller != null)
                    instance.controller.onSelectImage();
            }
        });
        return v;
    }

    public interface CommentsAdapterInterface{
        void onSelectImage();
    }

}

