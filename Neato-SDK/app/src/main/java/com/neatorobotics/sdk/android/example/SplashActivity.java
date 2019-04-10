package com.neatorobotics.sdk.android.example;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.neatorobotics.sdk.android.authentication.NeatoAuthentication;
import com.neatorobotics.sdk.android.example.login.LoginActivity;
import com.neatorobotics.sdk.android.example.robots.RobotsActivity;

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 *
 * The splash activity simply check if we already have a valid access token. If we have one
 * we'll go to robots page, otherwise we need to sign in.
 */
public class SplashActivity extends AppCompatActivity {

    private NeatoAuthentication neatoAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //here we're checking the access token
        neatoAuth = NeatoAuthentication.getInstance(this);
        if(neatoAuth.isAuthenticated()) {
            openRobotsActivity();
        }else {
            //need to sign in first
            openLoginActivity();
        }
    }

    private void openRobotsActivity() {
        Intent intent = new Intent(this, RobotsActivity.class);
        startActivity(intent);
        finish();
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
