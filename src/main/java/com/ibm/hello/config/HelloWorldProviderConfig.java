package com.ibm.hello.config;

import com.ibm.cloud_garage.rest_template.RestTemplateProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;

@Configuration
@ConfigurationProperties(prefix = "hello")
public class HelloWorldProviderConfig implements RestTemplateProperties<HelloWorldProviderConfig> {

    private String providerEndpoint;
    private int maxTotalConnections = DEFAULT_CONNECTIONS;
    private int connectTimeOutInSeconds = DEFAULT_TIMEOUT;
    private int socketTimeOutInSeconds = DEFAULT_TIMEOUT;
    private int connectionRequestTimeOutInSeconds = DEFAULT_TIMEOUT;
    private String proxyHostname;
    private int proxyPort = 0;
    private ClientHttpRequestInterceptor loggingInterceptor;

    @Override
    public String getProviderEndpoint() {
        return providerEndpoint;
    }

    @Override
    public void setProviderEndpoint(String providerEndpoint) {
        this.providerEndpoint = providerEndpoint;
    }

    @Override
    public int getMaxTotalConnections() {
        return maxTotalConnections;
    }

    @Override
    public void setMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    @Override
    public int getConnectTimeOutInSeconds() {
        return connectTimeOutInSeconds;
    }

    @Override
    public void setConnectTimeOutInSeconds(int connectTimeOutInSeconds) {
        this.connectTimeOutInSeconds = connectTimeOutInSeconds;
    }

    @Override
    public int getSocketTimeOutInSeconds() {
        return socketTimeOutInSeconds;
    }

    @Override
    public void setSocketTimeOutInSeconds(int socketTimeOutInSeconds) {
        this.socketTimeOutInSeconds = socketTimeOutInSeconds;
    }

    @Override
    public int getConnectionRequestTimeOutInSeconds() {
        return connectionRequestTimeOutInSeconds;
    }

    @Override
    public void setConnectionRequestTimeOutInSeconds(int connectionRequestTimeOutInSeconds) {
        this.connectionRequestTimeOutInSeconds = connectionRequestTimeOutInSeconds;
    }

    @Override
    public ClientHttpRequestInterceptor getLoggingInterceptor() {
        return loggingInterceptor;
    }

    @Override
    public void setLoggingInterceptor(ClientHttpRequestInterceptor loggingInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
    }

    @Override
    public String getProxyHostname() {
        return proxyHostname;
    }

    @Override
    public void setProxyHostname(String proxyHostname) {
        this.proxyHostname = proxyHostname;
    }

    @Override
    public int getProxyPort() {
        return proxyPort;
    }

    @Override
    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }
}
