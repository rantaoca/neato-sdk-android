package com.neatorobotics.sdk.android.example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.neatorobotics.sdk.android.authentication.NeatoAuthentication
import com.neatorobotics.sdk.android.example.login.LoginActivity
import com.neatorobotics.sdk.android.example.robots.RobotsActivity

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 *
 * The splash activity simply check if we already have a valid access token. If we have one
 * we'll go to robots page, otherwise we need to sign in.
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //here we're checking the access token
        if (NeatoAuthentication.isAuthenticated) {
            openRobotsActivity()
        } else {
            //need to sign in first
            openLoginActivity()
        }
    }

    private fun openRobotsActivity() {
        val intent = Intent(this, RobotsActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
