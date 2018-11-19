package com.example.katerynastorozh.fbphotoviewer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PhotosActivity extends AppCompatActivity {

    final String LOG_TAG = PhotosActivity.class.getSimpleName();
    List<String> photos = new ArrayList<>();
    PictersAdaptter picturesAdapter;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        String albomID = getIntent().getStringExtra("ALBOM_ID");
        gridView = (GridView) findViewById(R.id.grid_adapter);

        picturesAdapter = new PictersAdaptter(photos);
        gridView.setAdapter(picturesAdapter);
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
                            JSONObject jsonResponce = response.getJSONObject();
                            Log.d(LOG_TAG, jsonResponce.toString());

                            try {
                                JSONArray jsonData = jsonResponce.getJSONArray("data");
                                for (int i = 0; i < jsonData.length(); i++) {
                                    JSONObject image = jsonData.getJSONObject(i);
                                    String url = image.getString("source");
                                    Log.d(LOG_TAG, "source  " + url);
                                    photos.add(url);
                                }
                            } catch (JSONException ex) {
                                Log.d(LOG_TAG, ex.getMessage());
                                Toast.makeText(PhotosActivity.this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
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
}
