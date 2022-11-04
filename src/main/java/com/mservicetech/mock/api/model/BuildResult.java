package com.mservicetech.mock.api.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BuildResult  {

    private String resultMessage;

    public BuildResult () {
    }

    @JsonProperty("resultMessage")
    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BuildResult BuildResult = (BuildResult) o;

        return Objects.equals(resultMessage, BuildResult.resultMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultMessage);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BuildResult {\n");
        sb.append("    resultMessage: ").append(toIndentedString(resultMessage)).append("\n");
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
