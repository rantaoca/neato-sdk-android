package com.neatorobotics.sdk.android.example.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.neatorobotics.sdk.android.NeatoClient;
import com.neatorobotics.sdk.android.authentication.NeatoAuthenticationResponse;
import com.neatorobotics.sdk.android.authentication.NeatoOAuth2Scope;
import com.neatorobotics.sdk.android.example.R;
import com.neatorobotics.sdk.android.example.robots.RobotsActivity;

/**
 * Created by Marco on 25/03/16.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private NeatoClient neatoClient;

    String REDIRECT_URI = "marco-app://neato";
    String CLIENT_ID = "c54d0ac5def8323befb61cfc74e514af80bde385d878c23e47ca990fccb40258";
    NeatoOAuth2Scope[] scopes = new NeatoOAuth2Scope[]{NeatoOAuth2Scope.CONTROL_ROBOTS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        neatoClient = NeatoClient.getInstance(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri uri = intent.getData();
        if (uri != null) {
            NeatoAuthenticationResponse response = neatoClient.getOAuth2AuthResponseFromUri(uri);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    Log.d(TAG, response.getToken());
                    onAuthenticated();
                    break;
                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    String errorCode = response.getError();
                    String errorDescription = response.getErrorDescription();
                    Toast.makeText(this,"Authentication error.",Toast.LENGTH_SHORT).show();
                    break;
                // Most likely auth flow was cancelled
                default:
                    //You can do nothing
            }
        }
    }

    private void onAuthenticated() {
        openRobotsActivity();
    }

    private void openRobotsActivity() {
        Intent intent = new Intent(this, RobotsActivity.class);
        startActivity(intent);
        finish();
    }

    public void loginClick(View view) {
        neatoClient.openLoginInBrowser(CLIENT_ID,REDIRECT_URI,scopes);
    }
}
