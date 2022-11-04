package com.mservicetech.mock.api.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PathConfig  {

    private String path;
    
    
    public enum MethodEnum {
        
        @JsonProperty("DELETE")
        DELETE ("DELETE"), 
        
        @JsonProperty("POST")
        POST ("POST"), 
        
        @JsonProperty("GET")
        GET ("GET"), 
        
        @JsonProperty("PUT")
        PUT ("PUT"), 
        
        @JsonProperty("PATCH")
        PATCH ("PATCH"); 
        

        private final String value;

        MethodEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static MethodEnum fromValue(String text) {
            for (MethodEnum b : MethodEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                return b;
                }
            }
            return null;
        }
    }

    private MethodEnum method;

    
    private String requiredParam;
    private Response defaultResponse;
    private java.util.List<Case> cases;

    public PathConfig () {
    }

    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @JsonProperty("method")
    public MethodEnum getMethod() {
        return method;
    }

    public void setMethod(MethodEnum method) {
        this.method = method;
    }

    @JsonProperty("requiredParam")
    public String getRequiredParam() {
        return requiredParam;
    }

    public void setRequiredParam(String requiredParam) {
        this.requiredParam = requiredParam;
    }

    @JsonProperty("defaultResponse")
    public Response getDefaultResponse() {
        return defaultResponse;
    }

    public void setDefaultResponse(Response defaultResponse) {
        this.defaultResponse = defaultResponse;
    }

    @JsonProperty("cases")
    public java.util.List<Case> getCases() {
        return cases;
    }

    public void setCases(java.util.List<Case> cases) {
        this.cases = cases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PathConfig PathConfig = (PathConfig) o;

        return Objects.equals(path, PathConfig.path) &&
               Objects.equals(method, PathConfig.method) &&
               Objects.equals(requiredParam, PathConfig.requiredParam) &&
               Objects.equals(defaultResponse, PathConfig.defaultResponse) &&
               Objects.equals(cases, PathConfig.cases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, method, requiredParam, defaultResponse, cases);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PathConfig {\n");
        sb.append("    path: ").append(toIndentedString(path)).append("\n");        sb.append("    method: ").append(toIndentedString(method)).append("\n");        sb.append("    requiredParam: ").append(toIndentedString(requiredParam)).append("\n");        sb.append("    defaultResponse: ").append(toIndentedString(defaultResponse)).append("\n");        sb.append("    cases: ").append(toIndentedString(cases)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
