package com.tydic.amm.openup.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Slf4j
public class HttpRequestFactory {
    public static HttpRequest getHttpRequest(boolean ignoreSSL){
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectionRequestTimeout(300000);
        factory.setConnectTimeout(300000);
        factory.setReadTimeout(300000);
        CloseableHttpClient httpClient = getHttpsClient(ignoreSSL);
        factory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(factory);
        return new HttpRequest(restTemplate);
    }
    private static CloseableHttpClient getHttpsClient(boolean ignoreSSL) {
        CloseableHttpClient httpClient;
        if (ignoreSSL) {
            SSLContext sslContext = null;
            try {
                sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
                    @Override
                    public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        return true;
                    }
                }).build();
            } catch (Exception e) {
                log.warn("创建SSLContext失败", e);
            }
            httpClient = HttpClients.custom().setSSLContext(sslContext).
                    setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
        } else {
            httpClient = HttpClients.createDefault();
        }
        return httpClient;
    }
}
