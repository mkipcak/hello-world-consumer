package com.ibm.hello.consumer;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ibm.hello.config.HelloWorldProviderConfig;
import com.ibm.hello.model.HelloWorldRequest;
import com.ibm.hello.model.HelloWorldResponse;

@Component
public class HelloWorldPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldPort.class);

    @Autowired
    private HelloWorldProviderConfig config;

    @Autowired @Qualifier("helloWorldRestTemplate")
    private RestTemplate restTemplate;

    @Bean("helloWorldRestTemplate")
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    public HelloWorldResponse getGreetingGet(String inputName) {

        final String urlString = config.getProviderEndpoint() + "/hello" + buildQueryString(inputName);
        try {
            final URI uri = new URI(urlString);
            LOGGER.debug("Calling service with url: " + uri);

            return getHelloWorldResponse(
                    RequestEntity
                            .get(uri)
                            .build());
        } catch (URISyntaxException ex) {
            throw new BadUrl(urlString);
        }
    }

    protected String buildQueryString(String inputName) {
        return (StringUtils.isEmpty(inputName) ? "" : "?name=" + inputName);
    }

    public HelloWorldResponse getGreetingPost(String inputName) {

        final String urlString = config.getProviderEndpoint() + "/hello";
        try {
            final URI uri = new URI(urlString);
            LOGGER.debug("Calling service with url: " + uri);

            return getHelloWorldResponse(
                    RequestEntity
                            .post(uri)
                            .body(new HelloWorldRequest(inputName)));
        } catch (URISyntaxException e) {
            throw new BadUrl(urlString);
        }
    }

    protected HelloWorldResponse getHelloWorldResponse(RequestEntity requestEntity) {
        try {
            return restTemplate.exchange(
                    requestEntity,
                    HelloWorldResponse.class
            ).getBody();
        } catch (HttpClientErrorException ex) {
            if (ex.getRawStatusCode() == 406) {
                throw new MissingInputName();
            }
            throw ex;
        }
    }

    public static class MissingInputName extends RuntimeException { }

    public static class BadUrl extends RuntimeException {
        public BadUrl(String url) {
            super(url);
        }
    }
}
