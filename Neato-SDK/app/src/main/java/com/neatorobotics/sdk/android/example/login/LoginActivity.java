package com.neatorobotics.sdk.android.example.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.neatorobotics.sdk.android.authentication.NeatoAuthentication;
import com.neatorobotics.sdk.android.authentication.NeatoAuthenticationResponse;
import com.neatorobotics.sdk.android.authentication.NeatoOAuth2Scope;
import com.neatorobotics.sdk.android.example.R;

/**
 * Created by Marco on 25/03/16.
 */
public class LoginActivity extends AppCompatActivity {

    String REDIRECT_URI = "marco-app://neato";
    String CLIENT_ID = "c54d0ac5def8323befb61cfc74e514af80bde385d878c23e47ca990fccb40258";
    NeatoOAuth2Scope[] scopes = new NeatoOAuth2Scope[]{NeatoOAuth2Scope.ROBOT_COMMANDS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri uri = intent.getData();
        if (uri != null) {
            NeatoAuthenticationResponse response = NeatoAuthenticationResponse.fromUri(uri);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    Toast.makeText(this,"TOKEN: "+response.getToken(),Toast.LENGTH_SHORT).show();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Toast.makeText(this,"AUTH ERROR",Toast.LENGTH_SHORT).show();
                    break;

                // Most likely auth flow was cancelled
                default:
                    Toast.makeText(this,"CANCELLED",Toast.LENGTH_SHORT).show();
                    // Handle other cases
            }
        }
    }

    public void loginClick(View view) {
        NeatoAuthentication neatoAuthentication = new NeatoAuthentication(CLIENT_ID,REDIRECT_URI,scopes);
        neatoAuthentication.openLoginInBrowser(this);
    }
}
