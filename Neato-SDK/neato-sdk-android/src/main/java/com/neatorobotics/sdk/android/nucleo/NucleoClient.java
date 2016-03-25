package com.neatorobotics.sdk.android.nucleo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

/**
 * Created by Marco on 11/03/16.
 */
public class NucleoClient {
    public static void call() {
        try {
            HttpsURLConnection conn = new HttpsURLConnection(new URL("http://www.google.it")) {
                @Override
                public String getCipherSuite() {
                    return null;
                }

                @Override
                public Certificate[] getLocalCertificates() {
                    return new Certificate[0];
                }

                @Override
                public Certificate[] getServerCertificates() throws SSLPeerUnverifiedException {
                    return new Certificate[0];
                }

                @Override
                public void disconnect() {

                }

                @Override
                public boolean usingProxy() {
                    return false;
                }

                @Override
                public void connect() throws IOException {

                }
            };
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
