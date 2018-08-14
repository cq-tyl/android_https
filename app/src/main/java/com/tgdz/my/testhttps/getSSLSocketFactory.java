package com.tgdz.my.testhttps;

import android.content.Context;



import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by tyl
 * 2018/7/12/012
 * Describe:
 */

public class getSSLSocketFactory {
    public static SSLSocketFactory getSSLSocketFactory(Context context, InputStream inputStream) {
        if (context == null) {
            throw new NullPointerException("context == null");
        }
        CertificateFactory certificateFactory = null;
//       SSLContext 安全套接字协议实现，它充当安全套接字工厂或SSLEngines的工厂
        SSLContext sslContext = null;
        try {
            try {
//                用于解析和管理证书、证书撤消列表 (CRL) 和证书路径的类和接口。
                certificateFactory = CertificateFactory.getInstance("X.509");
            } catch (CertificateException e) {
                e.printStackTrace();
            }

//            KeyStore加密密钥和证书的存储工具。
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);


            keyStore.setCertificateEntry("ca", certificateFactory.generateCertificate(inputStream));
            if (inputStream != null) {
                inputStream.close();
            }

            sslContext = SSLContext.getInstance("TLS");

//            sslContext.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());


            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);

            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);


        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }

        return sslContext.getSocketFactory();
    }
}
