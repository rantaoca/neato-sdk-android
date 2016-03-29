package com.neatorobotics.sdk.android.authentication;

import java.util.Date;

/**
 * Created by Marco on 29/03/16.
 */
public interface AccessTokenDatasource {
    void saveToken(String token, Date expires);
    String getToken();
    boolean isTokenValid();
}
