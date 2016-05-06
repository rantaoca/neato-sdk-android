package com.neatorobotics.sdk.android.authentication;

import java.util.Date;

/**
 * Neato-SDK
 * Created by Marco on 06/05/16.
 * Copyright © 2016 Neato Robotics. All rights reserved.
 */
public interface AccessTokenDatasource {
    void storeToken(String token, Date expires);
    String loadToken();
    void clearToken();
    boolean isTokenValid();
}
