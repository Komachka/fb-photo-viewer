package com.example.katerynastorozh.fbphotoviewer.alboms;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.katerynastorozh.fbphotoviewer.photosgreed.PhotosActivity;
import com.example.katerynastorozh.fbphotoviewer.R;
import com.example.katerynastorozh.fbphotoviewer.login.LoginActivity;
import com.example.katerynastorozh.fbphotoviewer.utils.ImageHelper;
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

import static com.example.katerynastorozh.fbphotoviewer.utils.Constants.ALBUM_ID;
import static com.example.katerynastorozh.fbphotoviewer.utils.Constants.FB_ALBUMS_REQUEST;
import static com.example.katerynastorozh.fbphotoviewer.utils.Constants.USER_PHOTOS_PERMISSION;

/**
 * Created by kateryna on 18.11.18.
 */

public class AlbumActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    final String LOG_TAG = AlbumActivity.class.getSimpleName();

    ProfileTracker profileTracker;
    RecyclerView recyclerView;
    ImageView accountButton;
    ImageView homeButton;
    View emptyView;
    ImageHelper imageHelper;

    List<AlbumAdapter.AlbumItem> albumItems = new ArrayList<>();
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alboms);
        imageHelper = new ImageHelper(this);
        recyclerView = (RecyclerView) findViewById(R.id.destination_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        emptyView = findViewById(R.id.empty_view);
        adapter = new AlbumAdapter(this, albumItems, new AlbumAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AlbumAdapter.AlbumItem item) {
                Intent intent = new Intent(AlbumActivity.this, PhotosActivity.class);
                intent.putExtra(ALBUM_ID, item.id);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        setEmptyView();
        accountButton = (ImageView) findViewById(R.id.account_button);
        homeButton = (ImageView) findViewById(R.id.home_button);
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged (Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    imageHelper.displayProfilePic(currentProfile, accountButton);
                }
            }
        };
        Profile currentProfile = Profile.getCurrentProfile();
        if (currentProfile != null) {
            imageHelper.displayProfilePic(currentProfile, accountButton);
        }
        else {
            Profile.fetchProfileForCurrentAccessToken();
        }

        if (AccessToken.getCurrentAccessToken() != null)
            createRequest();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        accountButton.setOnClickListener(this);
        homeButton.setOnClickListener(this);
    }

    private void setEmptyView() {
        if (albumItems.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    private void createRequest() {
        if(AccessToken.getCurrentAccessToken().getPermissions().contains(USER_PHOTOS_PERMISSION)) {
            GraphRequest request = new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    FB_ALBUMS_REQUEST,
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {

                            try {
                                JSONObject jsonResponce = response.getJSONObject();
                                Log.d(LOG_TAG, jsonResponce.toString());

                                JSONArray jsonData = jsonResponce.getJSONArray("data");

                                for (int i = 0; i < jsonData.length(); i++) {
                                    JSONObject albumItemJSON = jsonData.getJSONObject(i);
                                    String name = albumItemJSON.getString("name");
                                    String id = albumItemJSON.getString("id");

                                    String coverPhoto = albumItemJSON.getJSONObject("picture").getJSONObject("data").getString("url");
                                    Log.d(LOG_TAG, "Album name " + name + " url " + coverPhoto);
                                    albumItems.add(new AlbumAdapter.AlbumItem(id, name, coverPhoto));
                                }

                            } catch (JSONException ex) {
                                Toast.makeText(AlbumActivity.this, R.string.albums_can_not_be_loaded, Toast.LENGTH_LONG).show();
                                Log.d(LOG_TAG, ex.getMessage());
                            }
                            catch (NullPointerException ex)
                            {
                                Toast.makeText(AlbumActivity.this, R.string.albums_can_not_be_loaded, Toast.LENGTH_LONG).show();
                                Log.d(LOG_TAG, ex.getMessage());
                            }
                            adapter.notifyDataSetChanged();
                            setEmptyView();

                        }
                    }
            );
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,picture.type(album)");
            request.setParameters(parameters);
            request.executeAsync();
            }
        }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.home_button)
        {
            startActivity(new Intent(this, LoginActivity.class));
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
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return true;
    }

}
