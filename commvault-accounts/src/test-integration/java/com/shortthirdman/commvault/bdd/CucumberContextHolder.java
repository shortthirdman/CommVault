package com.shortthirdman.commvault.bdd;

import io.restassured.response.Response;
import org.springframework.stereotype.Component;

/**
 * Used to hold data that is shared between test steps
 */
@Component
public class CucumberContextHolder {
    private Object parameters;
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Object getParameters() {
        return parameters;
    }

    public void setParameters(Object parameters) {
        this.parameters = parameters;
    }
}
