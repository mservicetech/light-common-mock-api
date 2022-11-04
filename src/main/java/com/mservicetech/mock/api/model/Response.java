package com.mservicetech.mock.api.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response  {

    private Integer statusCode;
    private String responsePath;
    private String responseBody;
    private java.util.List<Header> headers;

    public Response () {
    }

    @JsonProperty("statusCode")
    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @JsonProperty("responsePath")
    public String getResponsePath() {
        return responsePath;
    }

    public void setResponsePath(String responsePath) {
        this.responsePath = responsePath;
    }

    @JsonProperty("responseBody")
    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    @JsonProperty("headers")
    public java.util.List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(java.util.List<Header> headers) {
        this.headers = headers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Response Response = (Response) o;

        return Objects.equals(statusCode, Response.statusCode) &&
               Objects.equals(responsePath, Response.responsePath) &&
               Objects.equals(responseBody, Response.responseBody) &&
               Objects.equals(headers, Response.headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusCode, responsePath, responseBody, headers);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Response {\n");
        sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");        sb.append("    responsePath: ").append(toIndentedString(responsePath)).append("\n");        sb.append("    responseBody: ").append(toIndentedString(responseBody)).append("\n");        sb.append("    headers: ").append(toIndentedString(headers)).append("\n");
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
