package com.example.katerynastorozh.fbphotoviewer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.PopupMenu;

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

/**
 * Created by kateryna on 18.11.18.
 */

public class AlbomsActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    ProfileTracker profileTracker;
    RecyclerView recyclerView;

    //TODO fix images radius in photos icons
    final String LOG_TAG = AlbomsActivity.class.getSimpleName();
    ImageView accountButton;
    ImageView homeButton;

    List<AlbomAdapter.AlbomItem> albomItems = new ArrayList<>();
    RecyclerView.Adapter adapter;


    //TODO add empty views
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alboms);
        recyclerView = (RecyclerView) findViewById(R.id.destination_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AlbomAdapter(albomItems, new AlbomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AlbomAdapter.AlbomItem item) {
                Intent intent = new Intent(AlbomsActivity.this, PhotosActivity.class);
                intent.putExtra("ALBOM_ID", item.id);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        accountButton = (ImageView) findViewById(R.id.account_button);
        homeButton = (ImageView) findViewById(R.id.home_button);
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

        if (AccessToken.getCurrentAccessToken() != null)
            createRequest();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(myToolbar);
       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        */
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        accountButton.setOnClickListener(this);
        homeButton.setOnClickListener(this);
    }

    private void createRequest() {
        if(AccessToken.getCurrentAccessToken().getPermissions().contains("user_photos")) {
            GraphRequest request = new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me/albums",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            JSONObject jsonResponce = response.getJSONObject();
                            Log.d(LOG_TAG, jsonResponce.toString());

                            try {
                                JSONArray jsonData = jsonResponce.getJSONArray("data");

                                for (int i = 0; i < jsonData.length(); i++) {
                                    JSONObject albomItemJSON = jsonData.getJSONObject(i);
                                    String name = albomItemJSON.getString("name");
                                    String id = albomItemJSON.getString("id");

                                    String coverPhoto = albomItemJSON.getJSONObject("picture").getJSONObject("data").getString("url");
                                    Log.d(LOG_TAG, "albom name " + name + " url " + coverPhoto);
                                    albomItems.add(new AlbomAdapter.AlbomItem(id, name, coverPhoto));
                                }

                            } catch (JSONException ex) {
                                Log.d(LOG_TAG, ex.getMessage());
                            }
                            adapter.notifyDataSetChanged();

                        }
                    }
            );
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,picture.type(album)");
            request.setParameters(parameters);
            request.executeAsync();
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
    public void onClick(View view) {
        if (view.getId() == R.id.home_button)
        {
            startActivity(new Intent(this, MainActivity.class));
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
