package org.me.tuoristguide.ui.adapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.me.tuoristguide.R;
import org.me.tuoristguide.model.CommentListItem;
import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommentListItem> mCommentList;
    private CommentsAdapterInterface controller;
    private ImageView imageView1;
    private ImageView imageView2;

    CardView mLinearLayout;
    CardView cardView;

    final static String LOG_TAG =  CommentsAdapter.class.getSimpleName();

    public CommentsAdapter(Context mContext) {
        mCommentList = new ArrayList<>();
        this.mContext = mContext;
    }

    public void set(List<CommentListItem> items){
        mCommentList.clear();
        mCommentList.addAll(items);
    }

    public void add(CommentListItem item){
        mCommentList.add(item);
    }


    public CommentsAdapter setController(CommentsAdapterInterface controller){
        this.controller = controller;
        return this;
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
                ImageView im1 = (ImageView) viewGroup.findViewById(R.id.img_like);
                im1.setClickable(false);
            }
        });
        cardView.setCardBackgroundColor(-1);


        mLinearLayout = (CardView) v.findViewById(R.id.expandable);
        //set visibility to GONE
        mLinearLayout.setVisibility(View.GONE);

        //set button
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = (ViewGroup) v.getParent();
                mLinearLayout = (CardView) viewGroup.findViewById(R.id.expandable);
                mLinearLayout.setCardBackgroundColor(-1);
                if (mLinearLayout.getVisibility() == View.GONE) {
                    //set Visible
                    mLinearLayout.setVisibility(View.VISIBLE);

                    final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    mLinearLayout.measure(widthSpec, heightSpec);

                    ValueAnimator mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight(), mLinearLayout);
                    mAnimator.start();
                } else {
                    int finalHeight = mLinearLayout.getHeight();
                    ValueAnimator mAnimator = slideAnimator(finalHeight, 0, mLinearLayout);
                    mAnimator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            //Height=0, but it set visibility to GONE
                            mLinearLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    mAnimator.start();
                }
            }
        });
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
                if (controller != null)
                    controller.onSelectImage();
            }
        });
        return v;
    }

    private ValueAnimator slideAnimator(int start, int end, final CardView mLinearLayout) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mLinearLayout.getLayoutParams();
                layoutParams.height = value;
                mLinearLayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    public interface CommentsAdapterInterface{
        void onSelectImage();
    }

    public class InnerListview extends ListView {
        public InnerListview(Context context) {
            super(context);
        }

        public InnerListview(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public InnerListview(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);

        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    setParentScrollAble(false);
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:

                    setParentScrollAble(true);
                    break;

            }
            return super.onInterceptTouchEvent(ev);

        }

        private void setParentScrollAble(boolean flag) {
            getParent().requestDisallowInterceptTouchEvent(!flag);
        }

    }
}

