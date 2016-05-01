package org.me.tuoristguide.ui.adapter;

  import android.content.Context;
  import android.support.v7.widget.CardView;
  import android.view.View;
  import android.view.ViewGroup;
  import android.widget.BaseAdapter;
  import android.widget.ImageView;
  import android.widget.TextView;
  import android.widget.Toast;

  import com.squareup.picasso.Picasso;
  import com.yelp.clientlib.connection.YelpAPIFactory;

  import org.me.tuoristguide.R;
  import org.me.tuoristguide.model.CommentList;

  import java.util.List;

public class CommentsAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommentList> mCommentList;
    private  CommentsAdapterInterface controller=null;
    private static CommentsAdapter instance=null;
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

        TextView tvName = (TextView)v.findViewById(R.id.StoreNameText);
        TextView tvComment = (TextView)v.findViewById(R.id.CommentText);
        TextView tvTime = (TextView)v.findViewById(R.id.TimeText);
        ImageView imageView=(ImageView)v.findViewById(R.id.comment_profile_picture);
        TextView tvUserName=(TextView)v.findViewById(R.id.comment_user_name);
        CardView cardView = (CardView)v.findViewById(R.id.cardview);
        cardView.setCardBackgroundColor(-1);

        tvName.setText("Store:" + mCommentList.get(position).getStore_name());
        tvComment.setText("Comment: "+mCommentList.get(position).getComment_text());
        tvTime.setText("Time: "+mCommentList.get(position).getCreated_time());
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

