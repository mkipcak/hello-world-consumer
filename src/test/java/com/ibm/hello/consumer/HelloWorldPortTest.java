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
import org.apache.http.entity.ContentType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.ibm.hello.config.HelloWorldProviderConfig;
import com.ibm.hello.model.HelloWorldResponse;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "HelloWorld_Provider", hostInterface = "localhost", port = "9080")
@DisplayName("HelloWorldPort")
public class HelloWorldPortTest {

    final String inputName = "John";
    final String greeting = "Greeting";

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

    @Nested
    @DisplayName("Given [GET] /hello")
    public class GivenGetHello {
        @Nested
        @DisplayName("When name is provided")
        public class WhenNameIsProvided {
            @Pact(provider = "HelloWorld_Provider", consumer = "HelloWorld_Consumer")
            public RequestResponsePact createInputNameFragment(PactDslWithProvider builder) {
                Map<String, String> responseHeaders = new HashMap<>();
                responseHeaders.put("Content-Type", "application/json;charset=UTF-8");

                return builder
                        .uponReceiving("a GET request for greeting with name provided")
                        .path("/hello")
                        .query(String.format("name=%s", inputName))
                        .method("GET")
                        .willRespondWith()
                        .status(200)
                        .headers(responseHeaders)
                        .body(newJsonBody(o -> {
                            o.stringValue("name", inputName);
                            o.stringValue("greeting", greeting);
                        }).build())
                        .toPact();
            }

            @Test
            @DisplayName("Then return greeting`")
            @PactTestFor(pactMethod = "createInputNameFragment")
            public void hello_inputName(MockServer mockServer) {
                HelloWorldPort port = buildHelloWorldPort(mockServer);

                HelloWorldResponse result = port.getGreetingGet(inputName);
                assertEquals(inputName, result.getName());
                assertEquals(greeting, result.getGreeting());
            }
        }

        @Nested
        @DisplayName("When no input name is provided")
        public class WhenNoInputNameIsProvided {
            @Pact(provider = "HelloWorld_Provider", consumer = "HelloWorld_Consumer")
            public RequestResponsePact createNoInputFragment(PactDslWithProvider builder) {
                Map<String, String> responseHeaders = new HashMap<>();
                responseHeaders.put("Content-Type", "application/json;charset=UTF-8");

                return builder
                        .uponReceiving("a GET request for greeting with no name provided")
                        .path("/hello")
                        .method("GET")
                        .willRespondWith()
                        .status(406)
                        .toPact();
            }

            @Test
            @DisplayName("Then throw MissingInputName exception")
            @PactTestFor(pactMethod = "createNoInputFragment")
            public void hello_noInputName(MockServer mockServer) {
                final HelloWorldPort port = buildHelloWorldPort(mockServer);

                assertThrows(HelloWorldPort.MissingInputName.class, () -> {
                    port.getGreetingGet("");
                });
            }
        }
    }

    @Nested
    @DisplayName("Given [POST] /hello")
    public class GivenPostHello {
        @Nested
        @DisplayName("When name is provided")
        public class WhenNameIsProvided {
            @Pact(provider = "HelloWorld_Provider", consumer = "HelloWorld_Consumer")
            public RequestResponsePact createInputNameFragment(PactDslWithProvider builder) {
                Map<String, String> responseHeaders = new HashMap<>();
                responseHeaders.put("Content-Type", "application/json;charset=UTF-8");

                return builder
                        .uponReceiving("a POST request for greeting with name provided")
                        .path("/hello")
                        .method("POST")
                        .body(String.format("{\"name\": \"%s\"}", inputName), ContentType.APPLICATION_JSON)
                        .willRespondWith()
                        .status(200)
                        .headers(responseHeaders)
                        .body(newJsonBody(o -> {
                            o.stringValue("name", inputName);
                            o.stringValue("greeting", greeting);
                        }).build())
                        .toPact();
            }

            @Test
            @DisplayName("Then return greeting")
            @PactTestFor(pactMethod = "createInputNameFragment")
            public void hello_inputName(MockServer mockServer) {
                HelloWorldPort port = buildHelloWorldPort(mockServer);

                HelloWorldResponse result = port.getGreetingPost(inputName);
                assertEquals(inputName, result.getName());
                assertEquals(greeting, result.getGreeting());
            }
        }

        @Nested
        @DisplayName("When no name is provied")
        public class WhenNoNameIsProvided {
            @Pact(provider = "HelloWorld_Provider", consumer = "HelloWorld_Consumer")
            public RequestResponsePact createNoNameFragment(PactDslWithProvider builder) {
                Map<String, String> responseHeaders = new HashMap<>();
                responseHeaders.put("Content-Type", "application/json;charset=UTF-8");

                return builder
                        .uponReceiving("a POST request for greeting with no name provided")
                        .path("/hello")
                        .method("POST")
                        .body("{}", ContentType.APPLICATION_JSON)
                        .willRespondWith()
                        .status(406)
                        .toPact();
            }

            @Test
            @DisplayName("Then throw MissingInputName exception")
            @PactTestFor(pactMethod = "createNoNameFragment")
            public void hello_nullInputName(MockServer mockServer) {
                final HelloWorldPort port = buildHelloWorldPort(mockServer);

                assertThrows(HelloWorldPort.MissingInputName.class, () -> {
                    port.getGreetingPost(null);
                });
            }
        }

        @Nested
        @DisplayName("When empty name is provided")
        public class WhenEmptyNameIsProvided {
            @Pact(provider = "HelloWorld_Provider", consumer = "HelloWorld_Consumer")
            public RequestResponsePact createEmptyNameFragment(PactDslWithProvider builder) {
                Map<String, String> responseHeaders = new HashMap<>();
                responseHeaders.put("Content-Type", "application/json;charset=UTF-8");

                return builder
                        .uponReceiving("a POST request for greeting with no name provided")
                        .path("/hello")
                        .method("POST")
                        .body("{\"name\":\"\"}", ContentType.APPLICATION_JSON)
                        .willRespondWith()
                        .status(406)
                        .toPact();
            }

            @Test
            @DisplayName("Then throw MissingInputName exception")
            @PactTestFor(pactMethod = "createEmptyNameFragment")
            public void hello_emptyInputName(MockServer mockServer) {
                final HelloWorldPort port = buildHelloWorldPort(mockServer);

                assertThrows(HelloWorldPort.MissingInputName.class, () -> {
                    port.getGreetingPost("");
                });
            }
        }
    }
}
