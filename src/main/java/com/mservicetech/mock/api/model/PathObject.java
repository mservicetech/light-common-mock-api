package com.mservicetech.mock.api.model;

import java.util.List;

public class PathObject {
    private  String required;

    private Response defaultResponse;
    private java.util.List<Case> cases;

    public PathObject() {
        // intentionally empty
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public Response getDefaultResponse() {
        return defaultResponse;
    }

    public void setDefaultResponse(Response defaultResponse) {
        this.defaultResponse = defaultResponse;
    }

    public List<Case> getCases() {
        return cases;
    }

    public void setCases(List<Case> cases) {
        this.cases = cases;
    }
}
