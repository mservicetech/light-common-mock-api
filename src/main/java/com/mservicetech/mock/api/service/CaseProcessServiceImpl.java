package com.mservicetech.mock.api.service;

import com.jayway.jsonpath.JsonPath;
import com.mservicetech.mock.api.MockApiStartupHook;
import com.mservicetech.mock.api.model.*;
import com.networknt.exception.ApiException;
import com.networknt.http.HttpMethod;
import com.networknt.status.Status;

import java.util.Deque;
import java.util.List;
import java.util.Map;

public class CaseProcessServiceImpl implements CaseProcessService{

    @Override
    public Case processConditions(Map<String, Deque<String>> params, String body, List<Case> cases) {
        if (cases == null || cases.size()<1) return null;
        boolean match = false;

        for (Case caseItem: cases) {
            for (Condition condition: caseItem.getConditions()) {
                if (condition.getName()!=null) {
                    if (params.get(condition.getName())!=null) {
                        Deque<String> values = params.get(condition.getName());
                        if (OperatorEum.EQUALS.equals(condition.getOperator())) {
                            match = values.getFirst().equalsIgnoreCase(condition.getValue());
                        } else if (OperatorEum.CONTAINS.equals(condition.getOperator())) {
                            match = values.contains(condition.getValue());
                        }
                    }
                    if (!match) break;
                } else if (condition.getJsonPath()!=null && body != null) {
                    List<String> jsonPathResult = JsonPath.read(body, condition.getJsonPath());
                    if (jsonPathResult != null && jsonPathResult.size()>0) {
                        if (OperatorEum.EQUALS.equals(condition.getOperator())) {
                            match = jsonPathResult.get(0).equalsIgnoreCase(condition.getValue());
                        } else if (OperatorEum.CONTAINS.equals(condition.getOperator())) {
                            match = jsonPathResult.contains(condition.getValue());
                        }

                    }
                    if (!match) break;
                }

            }

            if (match) return caseItem;
        }

        return null;
    }

    @Override
    public BuildResult processCreationConfig(List<PathConfig> pathConfigs) throws ApiException {
        pathConfigs.stream().forEach(m->MockApiStartupHook.pathStore.addPathObject(m.getPath(), m.getMethod().name(), convertToObject(m)));
        BuildResult result = new BuildResult();
        result.setResultMessage("Path config created");
        return result;
    }

    @Override
    public PathConfig getPathConfig(String path, HttpMethod method) throws ApiException {
        PathObject pathObject = MockApiStartupHook.pathStore.getPath(path, method);
        if (pathObject != null) {
            return convertToConfig(pathObject, path, method.name());
        } else {
            Status status = new Status("ERR30001", path);
            throw new ApiException(status);
        }
    }

    @Override
    public BuildResult createResponse(Response response) throws ApiException {
        MockApiStartupHook.pathStore.createResponse(response.getResponsePath(), response.getResponseBody());
        BuildResult result = new BuildResult();
        result.setResultMessage("Response created");
        return result;
    }

    private PathObject convertToObject(PathConfig pathConfig) {
        PathObject pathObject = new PathObject();
        pathObject.setDefaultResponse(pathConfig.getDefaultResponse());
        pathObject.setRequired(pathConfig.getRequiredParam());
        pathObject.setCases(pathConfig.getCases());
        return pathObject;
    }

    private PathConfig convertToConfig(PathObject pathObject, String path, String method) {
        PathConfig pathConfig = new PathConfig();
        pathConfig.setPath(path);
        pathConfig.setMethod(PathConfig.MethodEnum.fromValue(method));
        pathConfig.setDefaultResponse(pathObject.getDefaultResponse());
        pathConfig.setRequiredParam(pathObject.getRequired());
        pathConfig.setCases(pathObject.getCases());
        return pathConfig;
    }
}
