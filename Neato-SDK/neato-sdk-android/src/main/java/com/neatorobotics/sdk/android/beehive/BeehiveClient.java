package com.neatorobotics.sdk.android.beehive;

import android.os.AsyncTask;

import com.neatorobotics.sdk.android.NeatoCallback;
import com.neatorobotics.sdk.android.NeatoError;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Marco on 24/03/16.
 */
public class BeehiveClient {
    public void logout(final String oauth2AccessToken, final NeatoCallback<Boolean> callback) {
        final AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            protected void onPreExecute() {}
            protected Boolean doInBackground(Void... unused) {
                try {
                    BeehiveBaseClient.executeCall(oauth2AccessToken, "POST", new URL("https://beehive-playground.neatocloud.com/oauth2/revoke").toString(), null);
                    return true;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return false;
            }
            protected void onPostExecute(Boolean logout) {
                if(logout) callback.done(true);
                else callback.fail(NeatoError.GENERIC_ERROR);
            }
        };
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else {
            task.execute();
        }

    }
}
