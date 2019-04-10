package com.neatorobotics.sdk.android.example.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.neatorobotics.sdk.android.authentication.NeatoAuthentication;
import com.neatorobotics.sdk.android.authentication.NeatoAuthenticationResponse;
import com.neatorobotics.sdk.android.authentication.NeatoOAuth2Scope;
import com.neatorobotics.sdk.android.example.R;
import com.neatorobotics.sdk.android.example.robots.RobotsActivity;

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 *
 * When the user tap the LOGIN button the OAuth2.0 authentication flow begin.
 * In the current implementation we're using the external browser method to obtain the
 * token. This method is more secure than the in-app web view.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private NeatoAuthentication neatoAuth;

    String REDIRECT_URI = "my-neato-app://neato";
    String CLIENT_ID = "32547b00f17b08e408d93a5b922fa97a23ff5e6d953427a6f3f9a98122c16c17";
    NeatoOAuth2Scope[] scopes = new NeatoOAuth2Scope[]{
            NeatoOAuth2Scope.CONTROL_ROBOTS,
            NeatoOAuth2Scope.PUBLIC_PROFILE,
            NeatoOAuth2Scope.MAPS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        neatoAuth = NeatoAuthentication.getInstance(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri uri = intent.getData();
        if (uri != null) {
            NeatoAuthenticationResponse response = neatoAuth.getOAuth2AuthResponseFromUri(uri);

            switch (response.getType()) {
                case TOKEN:
                    Log.d(TAG, response.getToken());
                    onAuthenticated();
                    break;
                case ERROR:
                    Toast.makeText(this,"Authentication error.",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    //Nothing to do here
            }
        }
    }

    private void onAuthenticated() {
        openRobotsActivity();
    }

    private void openRobotsActivity() {
        Intent intent = new Intent(this, RobotsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    public void loginClick(View view) {
        //we start the auth flow here
        //later we'll receive the result in the onNewIntent method above
        neatoAuth.openLoginInBrowser(this,CLIENT_ID,REDIRECT_URI,scopes);
    }
}
