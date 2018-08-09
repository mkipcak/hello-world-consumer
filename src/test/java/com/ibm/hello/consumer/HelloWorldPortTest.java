package com.ibm.hello.consumer;

import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.ibm.hello.config.HelloWorldProviderConfig;
import com.ibm.hello.model.HelloWorldResult;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "HelloWorld_Provider", hostInterface = "localhost", port = "9080")
@DisplayName("Given /hello")
public class HelloWorldPortTest {

    final String inputName = "John";

    @NotNull
    private HelloWorldPort buildHelloWorldPort(MockServer mockServer) {
        HelloWorldPort port = new HelloWorldPort();

        ReflectionTestUtils.setField(
                port,
                "config",
                new HelloWorldProviderConfig().withProviderEndpoint(mockServer.getUrl()));
        ReflectionTestUtils.setField(port, "restTemplate", new RestTemplate());

        return port;
    }

    @Pact(provider = "HelloWorld_Provider", consumer = "HelloWorld_Consumer")
    public RequestResponsePact createInputNameFragment(PactDslWithProvider builder) {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json;charset=UTF-8");

        return builder
                .uponReceiving("a request for greeting with name provided")
                        .path(String.format("/hello/%s", inputName))
                        .method("GET")
                        .willRespondWith()
                        .status(200)
                        .headers(responseHeaders)
                        .body(newJsonBody(o -> {
                            o.stringValue("name", inputName);
                            o.stringValue("greeting", "Hello there, " + inputName + "!");
                        }).build())
                .toPact();
    }

    @Test
    @DisplayName("When `John` provided for input name then return greeting `Hello there, John!`")
    @PactTestFor(pactMethod = "createInputNameFragment")
    public void hello_inputName(MockServer mockServer) {
        HelloWorldPort port = buildHelloWorldPort(mockServer);

        HelloWorldResult result = port.getGreeting(inputName);
        assertEquals(inputName, result.getName());
        assertEquals("Hello there, " + inputName + "!", result.getGreeting());
    }

    @Pact(provider = "HelloWorld_Provider", consumer = "HelloWorld_Consumer")
    public RequestResponsePact createNoInputFragment(PactDslWithProvider builder) {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json;charset=UTF-8");

        return builder
                .uponReceiving("a request for greeting with no name provided")
                .path("/hello/")
                .method("GET")
                .willRespondWith()
                .status(400)
                .toPact();
    }

    @Test
    @DisplayName("When no input name is provided then throw exception")
    @PactTestFor(pactMethod = "createNoInputFragment")
    public void hello_noInputName(MockServer mockServer) {
        final HelloWorldPort port = buildHelloWorldPort(mockServer);

        assertThrows(HelloWorldPort.MissingInputName.class, () -> {
            port.getGreeting("");
        });
    }
}
