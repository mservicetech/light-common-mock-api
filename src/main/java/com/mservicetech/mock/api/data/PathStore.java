package com.mservicetech.mock.api.data;

import com.mservicetech.mock.api.model.PathObject;
import com.networknt.http.HttpMethod;

import java.util.Deque;
import java.util.Map;

public interface PathStore {

    boolean hasPath(String path, HttpMethod method);

    PathObject getPath(String path, HttpMethod method);

    Map<String, Deque<String>> getPathParams();

    void addPathObject(String path, String method, PathObject pathObject);

    void createResponse(String path, String content);

    String getResponse(String path);

}
