package com.ibm.hello.config;

public class AbstractProviderConfig<T extends AbstractProviderConfig<T>> {
    private String providerEndpoint;

    public String getProviderEndpoint() {
        return providerEndpoint;
    }

    public void setProviderEndpoint(String providerEndpoint) {
        this.providerEndpoint = providerEndpoint;
    }

    public T withProviderEndpoint(String providerEndpoint) {
        this.setProviderEndpoint(providerEndpoint);
        return (T) this;
    }
}
