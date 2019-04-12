package com.neatorobotics.sdk.android.example.login

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast

import com.neatorobotics.sdk.android.authentication.NeatoAuthentication
import com.neatorobotics.sdk.android.authentication.NeatoAuthenticationResponse
import com.neatorobotics.sdk.android.authentication.NeatoOAuth2Scope
import com.neatorobotics.sdk.android.example.R
import com.neatorobotics.sdk.android.example.robots.RobotsActivity
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 *
 * When the user tap the LOGIN button the OAuth2.0 authentication flow begin.
 * In the current implementation we're using the external browser method to obtain the
 * token. This method is more secure than the in-app web view.
 */
class LoginActivity : AppCompatActivity() {

    private var REDIRECT_URI = "my-neato-app://neato"
    private var CLIENT_ID = "32547b00f17b08e408d93a5b922fa97a23ff5e6d953427a6f3f9a98122c16c17"
    private var scopes = arrayOf(NeatoOAuth2Scope.CONTROL_ROBOTS,
                                                        NeatoOAuth2Scope.PUBLIC_PROFILE,
                                                        NeatoOAuth2Scope.MAPS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val uri = intent.data
        if (uri != null) {
            val response = NeatoAuthentication.getOAuth2AuthResponseFromUri(uri)

            when (response.type) {
                NeatoAuthenticationResponse.Response.TOKEN -> {
                    Log.d(TAG, response.token)
                    onAuthenticated()
                }
                NeatoAuthenticationResponse.Response.ERROR -> Toast.makeText(
                    this,
                    "Authentication error.",
                    Toast.LENGTH_SHORT
                ).show()
                else -> {}
            }
        }
    }

    private fun onAuthenticated() {
        openRobotsActivity()
    }

    private fun openRobotsActivity() {
        val intent = Intent(this, RobotsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
        finish()
    }

    fun loginClick(view: View) {
        //we start the auth flow here
        //later we'll receive the result in the onNewIntent method above
        NeatoAuthentication.openLoginInBrowser(this, CLIENT_ID, REDIRECT_URI, scopes)
    }

    companion object {

        private const val TAG = "LoginActivity"
    }
}
