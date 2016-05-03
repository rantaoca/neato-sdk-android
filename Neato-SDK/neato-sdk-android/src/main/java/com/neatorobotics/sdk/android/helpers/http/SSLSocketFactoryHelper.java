package com.neatorobotics.sdk.android.helpers.http;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Marco on 14/04/15.
 * SSL Socket Factory to trust the self signed Neato certificates.
 */
public class SSLSocketFactoryHelper {

    private static final String TAG = "SSLSocketFactoryHelper";

    public static final int NUCLEO = 1;

    public static SSLSocketFactory nucleoSSLSocketFactory;

    public static SSLSocketFactory get(Context context, int type) {
        if(type == NUCLEO) {
            return getNucleoSSLSocketFactory(context);
        }else {
            Log.e(TAG,"SSLSocketFactory type not supported...");
            return null;
        }
    }

    private static SSLSocketFactory getNucleoSSLSocketFactory(Context context) {
        if(nucleoSSLSocketFactory != null) return nucleoSSLSocketFactory;

        try {
            // Load CAs from an InputStream
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream is = context.getResources().getAssets().open("certificates/neatocloud.com.self-signed.server.crt");
            InputStream caInput = new BufferedInputStream(is);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                Log.d(TAG,"ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext sslcontext = SSLContext.getInstance("TLSv1");

            sslcontext.init(null,
                    tmf.getTrustManagers(),
                    null);
            SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());
            nucleoSSLSocketFactory = NoSSLv3Factory;
            return nucleoSSLSocketFactory;
        }catch(Exception e) {Log.e(TAG,"Exception",e);}
        return nucleoSSLSocketFactory;//probably null
    }
}
