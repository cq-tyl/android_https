package com.tgdz.my.testhttps;

import android.util.Log;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Created by tyl
 * 2018/7/12/012
 * Describe:
 */


public class TrustAllCerts implements X509TrustManager {
//    默认的下面3个接口都会抛出一个异常,这里直接去掉异常,就是客户端忽略验证服务器端的验证信息直接通过
    @Override
    public void checkClientTrusted(
            X509Certificate[] chain, String authType) {
        Log.e("tyl","checkClientTrusted");
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {
        Log.e("tyl","checkServerTrusted");
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        Log.e("tyl","getAcceptedIssuers");
        return new X509Certificate[]{};}
}