# hello-world-consumer

This project provides an example Pact consumer. The pact test is provided in `com.ibm.hello.consumer.HelloWorldPortTest`.

The project itself has been built using the SpringBoot template project - [seansund/template-spring-boot](https://github.ibm.com/seansund/template-spring-boot)
which includes the Pact build configuration and Junit5. The SpringBoot template project also provides a HelloWorld service
that will be used as the provider for the consumer code provided here.

## Usage

### Run the test and generate the pact

`./gradlew test`

### Publish the pact to a pact-broker

`./gradlew pactPublish`

`./gradlew pactPublish -PpactBrokerUrl={url}`



