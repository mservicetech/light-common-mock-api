package com.mservicetech.mock.api.service;

import com.mservicetech.mock.api.model.BuildResult;
import com.mservicetech.mock.api.model.Case;
import com.mservicetech.mock.api.model.PathConfig;
import com.mservicetech.mock.api.model.Response;
import com.networknt.exception.ApiException;
import com.networknt.http.HttpMethod;

import java.util.Deque;
import java.util.List;
import java.util.Map;

public interface CaseProcessService {

    Case processConditions(Map<String, Deque<String>> params, String body, List<Case> cases);

    BuildResult processCreationConfig (List<PathConfig> pathConfigs) throws ApiException;

    PathConfig getPathConfig(String path, HttpMethod method) throws ApiException;

    BuildResult createResponse(Response response) throws ApiException;
}
