package com.ibm.hello.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ibm.hello.config.HelloWorldProviderConfig;
import com.ibm.hello.model.HelloWorldResult;

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

    public HelloWorldResult getGreeting(String inputName) {
        ParameterizedTypeReference<HelloWorldResult> responseType = new ParameterizedTypeReference<HelloWorldResult>() {
        };

        final String url = config.getProviderEndpoint() + "/hello/" + inputName;
        LOGGER.debug("Calling service with url: " + url);

        try {
            return restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    responseType
            ).getBody();
        } catch (HttpClientErrorException ex) {
            if (ex.getRawStatusCode() == 406) {
                throw new MissingInputName();
            }
            throw ex;
        }
    }

    public static class MissingInputName extends RuntimeException { }
}
