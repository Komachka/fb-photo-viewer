package com.example.katerynastorozh.fbphotoviewer.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.katerynastorozh.fbphotoviewer.alboms.AlbumActivity;
import com.example.katerynastorozh.fbphotoviewer.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public CallbackManager callbackManager;
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(getResources().getString(R.string.EMAIL_PERMISSION)));
        loginButton.setReadPermissions(Arrays.asList(getResources().getString(R.string.USER_PHOTOS_PERMISSION)));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        launchAlbumActivity();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, R.string.can_not_login, Toast.LENGTH_LONG).show();

                    }
                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        Button toAlbom = findViewById(R.id.to_alboms);
        toAlbom.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void launchAlbumActivity()
    {


        if (AccessToken.getCurrentAccessToken() != null)
        {
            Intent intent = new Intent(this, AlbumActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(this, R.string.login_first, Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.to_alboms)
        {

            launchAlbumActivity();
        }
    }
}
