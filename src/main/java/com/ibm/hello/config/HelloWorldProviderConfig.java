package com.ibm.hello.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "hello")
public class HelloWorldProviderConfig extends AbstractProviderConfig<HelloWorldProviderConfig> {
}
