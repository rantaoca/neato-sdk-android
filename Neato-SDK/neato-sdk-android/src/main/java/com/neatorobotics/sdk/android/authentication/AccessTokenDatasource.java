package com.neatorobotics.sdk.android.authentication;

import java.util.Date;

/**
 * Created by Marco on 29/03/16.
 */
public interface AccessTokenDatasource {
    void storeToken(String token, Date expires);
    String loadToken();
    void clearToken();
    boolean isTokenValid();
}
