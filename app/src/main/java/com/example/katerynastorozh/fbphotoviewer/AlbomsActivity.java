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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
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

public class AlbomsActivity extends AppCompatActivity {
    ProfileTracker profileTracker;
    RecyclerView recyclerView;


    final String LOG_TAG = AlbomsActivity.class.getSimpleName();
    ImageView accountButton;

    List<AlbomAdapter.AlbomItem> albomItems = new ArrayList<>();
    RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alboms);
        recyclerView = (RecyclerView) findViewById(R.id.destination_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AlbomAdapter(albomItems, new AlbomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AlbomAdapter.AlbomItem item) {
                Toast.makeText(AlbomsActivity.this, "Item Clicked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AlbomsActivity.this, PhotosActivity.class);
                intent.putExtra("ALBOM_ID", item.id);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        accountButton = (ImageView) findViewById(R.id.account_button);
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
                                Toast.makeText(AlbomsActivity.this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
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
                .into(accountButton);
    }





}
