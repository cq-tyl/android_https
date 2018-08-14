package com.tgdz.my.testhttps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;

public class MainActivity extends AppCompatActivity {
    String url = "https://admin.96166dache.cn/xxx";
//    String url = "https://www.test.com";
    private TextView my_text;
    private WebView my_webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        my_text= (TextView) findViewById(R.id.my_text);
//        my_webview= (WebView) findViewById(R.id.my_webview);
//        my_webview.setWebViewClient(new SslWebViewClient(this));
//        my_webview.loadUrl(url);          //调用loadUrl方法为WebView加入链接
        getHttps();
    }
    public void getHttps() {
        try {
            OkHttpClient mOkHttpClient = null;
            mOkHttpClient = new OkHttpClient().newBuilder()
//                    主要就是下面2句，其他的和正常请求都一样的
                    .hostnameVerifier(new Home())//忽略服务器域名不信任警告
                    .sslSocketFactory(MySSLSocketFactory.getSocketFactory(MainActivity.this))//加入证书
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    Log.e("tyl", "onFailure=" + e.getMessage());
                }
                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    Log.e("tyl", "onResponse=" + response.body().string());
                }
            });
        } catch (Exception e) {
            Log.e("tyl", "IOException=" + e);
            e.printStackTrace();
        }
    }

    public class Home implements HostnameVerifier {
        public SSLSession sslSession;

        @Override
        public boolean verify(String hostname, SSLSession session) {
            this.sslSession = session;
            return true;
        }
    }
//    private static ConnectionSpec getConnectionSpec() {
//        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_0).cipherSuites(CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA256, CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA256, CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_3DES_EDE_CBC_SHA).build();
//        return spec;
//    }
}
