package com.neatorobotics.sdk.android.nucleo;

import android.os.AsyncTask;
import androidx.annotation.VisibleForTesting;

import com.neatorobotics.sdk.android.NeatoCallback;
import com.neatorobotics.sdk.android.NeatoError;
import com.neatorobotics.sdk.android.models.Robot;
import com.neatorobotics.sdk.android.models.RobotState;

import org.json.JSONObject;

/**
 * Created by Marco on 02/11/15.
 */
public class NucleoClient {

    private static final String TAG = "NucleoClient";

    @VisibleForTesting
    public NucleoBaseClient nucleoBaseClient = new NucleoBaseClient();

    /**
     * Execute an async Nucleo call.
     * @param robot
     * @param params JSON to send as input to the robot
     * @param callback
     */
    public void sendCommandAsync(final Robot robot, final JSONObject params, final NeatoCallback<NucleoResponse> callback) {
        final AsyncTask<Void, Void, NucleoResponse> task = new AsyncTask<Void, Void, NucleoResponse>() {
            protected void onPreExecute() {}
            protected NucleoResponse doInBackground(Void... unused) {
                NucleoResponse result = nucleoBaseClient.executeNucleoCall(Nucleo.URL, robot.serial, params, robot.secret_key);
                return result;
            }
            protected void onPostExecute(NucleoResponse result) {
                if(result != null && result.getJSON() != null && result.isOK()) {
                    //directly inject robot state
                    if(result.isStateResponse()) robot.state = new RobotState(result.getJSON());
                    callback.done(result);
                }
                else {
                    NeatoError error = result != null ? result.getError() : NeatoError.GENERIC_ERROR;
                    callback.fail(error);
                }
            }
        };
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else {
            task.execute();
        }
    }
}
