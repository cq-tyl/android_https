package com.tgdz.my.testhttps;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by tyl
 * 2018/7/9/009
 * Describe:
 */

public class MySSLSocketFactory {
    private static final String KEY_STORE_TYPE_BKS = "bks";//证书类型
    private static final String KEY_STORE_TYPE_P12 = "PKCS12";//证书类型
    private static final String KEY_STORE_PASSWORD = "123456";//证书密码（应该是客户端证书密码，没有密码的直接改为空字符串）
    private static final String KEY_STORE_TRUST_PASSWORD = "123456";//授信证书密码（应该是服务端证书密码）
    private static InputStream trust_input;
    private static InputStream client_input;

    public static SSLSocketFactory getSocketFactory(Context context)  {
//        可以使用bks和client.cer来验证 去掉注释的代码，client.p12替换ca.p12即可，也可以直接通过p12我测试的时候也是通过的
        try {
            //服务器授信证书
//            trust_input = context.getResources().getAssets().open("client.bks");
            //客户端证书
            client_input = context.getResources().getAssets().open("client.p12");
            SSLContext sslContext = SSLContext.getInstance("TLS");
//            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            trustStore.load(trust_input, KEY_STORE_TRUST_PASSWORD.toCharArray());
//            KeyStore存放证书及密匙的仓库
            KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);
            keyStore.load(client_input, KEY_STORE_PASSWORD.toCharArray());
//            KeyManagerFactory证书管理类
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, KEY_STORE_PASSWORD.toCharArray());
//            核心代码 SSLContext此类的实例表示安全套接字协议的实现， 它是SSLSocketFactory、SSLServerSocketFactory和SSLEngine的工厂。
//            这里注意有一个坑，之前我写的时候参考的网上文档大部分都是使用的是TrustManager系统默认的证书管理器但是自建证书需要使用X509TrustManager来实现
            sslContext.init(keyManagerFactory.getKeyManagers(),new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            SSLSocketFactory factory = sslContext.getSocketFactory();

            return factory;

        } catch (Exception e) {
            e.printStackTrace();
                Log.e("tyl","Exception="+e.getMessage());
            return null;
        } finally {
            try {
//                trust_input.close();
                client_input.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("tyl","Exception="+e.getMessage());
            }
        }
    }
};