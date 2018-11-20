package com.example.katerynastorozh.fbphotoviewer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PhotosActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    final String LOG_TAG = PhotosActivity.class.getSimpleName();
    List<String> photos = new ArrayList<>();
    PictersAdaptter picturesAdapter;
    GridView gridView;
    ImageView accountButton;
    ImageView homeButton;
    ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        String albomID = getIntent().getStringExtra("ALBOM_ID");

        accountButton = (ImageView) findViewById(R.id.account_button);
        homeButton = (ImageView) findViewById(R.id.home_button);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        accountButton.setOnClickListener(this);
        homeButton.setOnClickListener(this);

        gridView = (GridView) findViewById(R.id.grid_adapter);
        View emptyView = findViewById(R.id.empty_view);
        gridView.setEmptyView(emptyView);
        picturesAdapter = new PictersAdaptter(photos);

        gridView.setAdapter(picturesAdapter);


        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged (Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    displayProfilePic(currentProfile);
                }
            }
        };
        Profile currentProfile = Profile.getCurrentProfile();
        if (currentProfile != null) {
            displayProfilePic(currentProfile);
        }
        else {
            Profile.fetchProfileForCurrentAccessToken();
        }


        if (AccessToken.getCurrentAccessToken() != null) {
            createRequest(albomID);
        }
    }

    private void createRequest(String albomID) {
        if (AccessToken.getCurrentAccessToken().getPermissions().contains("user_photos")) {
            GraphRequest request = new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/" + albomID + "/photos",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            /* handle the result */


                            try {
                                JSONObject jsonResponce = response.getJSONObject();
                                Log.d(LOG_TAG, jsonResponce.toString());
                                JSONArray jsonData = jsonResponce.getJSONArray("data");
                                for (int i = 0; i < jsonData.length(); i++) {
                                    JSONObject image = jsonData.getJSONObject(i);
                                    String url = image.getString("source");
                                    Log.d(LOG_TAG, "source  " + url);
                                    photos.add(url);
                                }
                            } catch (JSONException ex) {
                                Log.d(LOG_TAG, ex.getMessage());
                                Toast.makeText(PhotosActivity.this, "Alboms can not be loaded", Toast.LENGTH_LONG).show();

                            }
                            catch (NullPointerException ex)
                            {
                                Log.d(LOG_TAG, ex.getMessage());
                                Toast.makeText(PhotosActivity.this, "Alboms can not be loaded", Toast.LENGTH_LONG).show();
                            }
                            picturesAdapter.notifyDataSetChanged();
                        }
                    }
            );

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,source");

            request.setParameters(parameters);
            request.executeAsync();


        }
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.home_button)
        {
            finish();
        }
        if (view.getId() == R.id.account_button)
        {
            PopupMenu popup = new PopupMenu(this,view);
            popup.setOnMenuItemClickListener(this);// to implement on click event on items of menu
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.account_menu, popup.getMenu());
            popup.show();
        }
    }


    private void displayProfilePic(Profile profile) {
        Uri uri = profile.getProfilePictureUri(28, 28);
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(this)
                .load(uri)
                .placeholder(R.drawable.progress_animation)
                .transform(transformation)
                .centerCrop()
                .fit()
                .into(accountButton);
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.log_out)
        {
            LoginManager.getInstance().logOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        }
        return true;
    }
}
