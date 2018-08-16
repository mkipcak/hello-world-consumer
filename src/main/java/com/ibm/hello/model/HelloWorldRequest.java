package com.ibm.hello.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class HelloWorldRequest {
    private String name;

    public HelloWorldRequest() {
        super();
    }

    public HelloWorldRequest(String name) {
        this.name = name;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
