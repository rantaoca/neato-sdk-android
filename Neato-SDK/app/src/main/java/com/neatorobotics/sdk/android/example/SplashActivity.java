package com.neatorobotics.sdk.android.example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.neatorobotics.sdk.android.NeatoClient;
import com.neatorobotics.sdk.android.example.login.LoginActivity;
import com.neatorobotics.sdk.android.example.robots.RobotsActivity;

public class SplashActivity extends AppCompatActivity {

    private NeatoClient neatoClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        neatoClient = NeatoClient.getInstance(this);
        if(neatoClient.isAuthenticated()) {
            openRobotsActivity();
        }else {
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
