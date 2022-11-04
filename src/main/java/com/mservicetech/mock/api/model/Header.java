package com.mservicetech.mock.api.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Header  {

    private String headerKey;
    private String headerValue;

    public Header () {
    }

    @JsonProperty("headerKey")
    public String getHeaderKey() {
        return headerKey;
    }

    public void setHeaderKey(String headerKey) {
        this.headerKey = headerKey;
    }

    @JsonProperty("headerValue")
    public String getHeaderValue() {
        return headerValue;
    }

    public void setHeaderValue(String headerValue) {
        this.headerValue = headerValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Header Header = (Header) o;

        return Objects.equals(headerKey, Header.headerKey) &&
               Objects.equals(headerValue, Header.headerValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(headerKey, headerValue);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Header {\n");
        sb.append("    headerKey: ").append(toIndentedString(headerKey)).append("\n");        sb.append("    headerValue: ").append(toIndentedString(headerValue)).append("\n");
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
