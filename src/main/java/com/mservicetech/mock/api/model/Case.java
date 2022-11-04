package com.mservicetech.mock.api.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Case  {

    private Response response;
    private java.util.List<Condition> conditions;

    public Case () {
    }

    @JsonProperty("response")
    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @JsonProperty("conditions")
    public java.util.List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(java.util.List<Condition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Case Case = (Case) o;

        return Objects.equals(response, Case.response) &&
               Objects.equals(conditions, Case.conditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(response, conditions);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Case {\n");
        sb.append("    response: ").append(toIndentedString(response)).append("\n");        sb.append("    conditions: ").append(toIndentedString(conditions)).append("\n");
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
