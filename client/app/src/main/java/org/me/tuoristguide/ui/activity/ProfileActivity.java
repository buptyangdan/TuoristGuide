package org.me.tuoristguide.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.me.tuoristguide.R;
import org.me.tuoristguide.database.DatabaseConnector;
import org.me.tuoristguide.entities.UserManager;
import org.me.tuoristguide.model.CommentListItem;
import org.me.tuoristguide.model.User;
import org.me.tuoristguide.service.local.FacebookService;
import org.me.tuoristguide.service.remote.CommentService;
import org.me.tuoristguide.ui.adapter.CommentsAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;


@ContentView(R.layout.activity_login)
public class ProfileActivity extends RoboActivity implements FacebookService.OnFacebookLoggedIn, CommentService.CommentServiceInterface, CommentsAdapter.CommentsAdapterInterface{

    @InjectView(R.id.btn_login)         private LoginButton btn_login;
    @InjectView(R.id.user_name)         private TextView nameTextview;
    @InjectView(R.id.user_email)        private TextView emailTextview;
    @InjectView(R.id.profile_picture)   private ImageView photoImageview;
    @InjectView(R.id.listview_comment)  private ListView commentListView;

    private CommentsAdapter adapter;

    final static String LOG_TAG = ProfileActivity.class.getSimpleName();

    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init Facebook Service here
        FacebookService.getInstance(this);

        // then inflate the view
        if(!FacebookService.getInstance().checkLoginStatus()){
            // set up login button
            FacebookService.getInstance(this).setupFBLoginButton(btn_login);
        }

        CommentService.getInstance().setController(this);

        // create new adapter and set it to listView
        adapter = new CommentsAdapter(this)
                .setController(this,"profile")
                .showStoreName();
        shareDialog = new ShareDialog(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // check user info on resume
        commentListView.setAdapter(adapter);
        checkUserInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        FacebookService.getInstance(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void checkUserInfo() {
        User user = UserManager.getInstance().getCurrentUser();
        if (user!=null&&user.email!=null){
            CommentService.getInstance().getByUser(user.email);
            nameTextview.setText(user.name);
            emailTextview.setText(user.email);
            Picasso.with(this).load(user.picture_url)
                    .into(photoImageview);

            DatabaseConnector databaseConnector = new DatabaseConnector(ProfileActivity.this, "User.db", null, 2);
            databaseConnector.setUser(user);
            ContentValues values = databaseConnector.insertValues();
            SQLiteDatabase database = databaseConnector.getWritableDatabase();
            database.execSQL("delete from User;");
            database.insert("User", null, values);
        }
    }

    @Override
    public void onFacebookLoggedIn() {
        checkUserInfo();
    }

    @Override
    public void getJsonResponse(JSONObject jsonObject) {
        System.out.print("here is the by user response!");
        System.out.println(jsonObject);
        JSONArray arrayObj = null;

        try {
            String user_name = jsonObject.getString("user_name");
            String photo_url = jsonObject.getString("poto_url");
            String stores = jsonObject.getString("stores");
            arrayObj = new JSONArray(stores);
            for (int i = 0; i < arrayObj.length(); i++) {
                JSONObject json = arrayObj.getJSONObject(i);
                adapter.add(new CommentListItem(user_name, photo_url, i + "", json.getString("store_name"), json.getString("comment_txt"), json.getString("created_time")));
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Select profile Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    /**** this method used for select image From Gallery  *****/

    private void onSelectFromGalleryResult(Intent data) {

        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        Bitmap thumbnail;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        thumbnail = BitmapFactory.decodeFile(selectedImagePath, options);

        ShareDialog(thumbnail);
    }


    /***  this method used for take profile photo *******/
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ShareDialog(thumbnail);
    }

    // This method is used to share Image on facebook timeline.
    public void ShareDialog(Bitmap imagePath){

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(imagePath)
                .setCaption("Testing")
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        shareDialog.show(content);
    }


    @Override
    public void onSelectImage() {
          selectImage();
    }
}

