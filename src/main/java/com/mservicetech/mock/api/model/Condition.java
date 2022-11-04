package com.mservicetech.mock.api.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Condition  {

    private String name;
    private String jsonPath;
    private String value;
    private OperatorEum operator;

    public Condition () {
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("jsonPath")
    public String getJsonPath() {
        return jsonPath;
    }

    public void setJsonPath(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @JsonProperty("operator")
    public OperatorEum getOperator() {
        return operator;
    }

    public void setOperator(OperatorEum operator) {
        this.operator = operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Condition Condition = (Condition) o;

        return Objects.equals(name, Condition.name) &&
               Objects.equals(jsonPath, Condition.jsonPath) &&
               Objects.equals(value, Condition.value) &&
               Objects.equals(operator, Condition.operator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, jsonPath, value, operator);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Condition {\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");        sb.append("    jsonPath: ").append(toIndentedString(jsonPath)).append("\n");        sb.append("    value: ").append(toIndentedString(value)).append("\n");        sb.append("    operator: ").append(toIndentedString(operator)).append("\n");
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
