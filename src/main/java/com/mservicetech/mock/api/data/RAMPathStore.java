package com.mservicetech.mock.api.data;

import com.mservicetech.mock.api.model.MappingConfig;
import com.mservicetech.mock.api.model.PathObject;
import com.networknt.config.Config;
import com.networknt.http.HttpMethod;
import io.undertow.util.HttpString;
import io.undertow.util.PathTemplateMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class RAMPathStore implements PathStore{

    private static Logger logger = LoggerFactory.getLogger(PathStore.class);

    private Map<String, Map<String, PathObject>> pathMap = new HashMap<>();

    private  static  Map<HttpString, PathTemplateMatcher<String>> methodToMatcherMap = new HashMap<>();
    private  Map<String, String> responseMap = new HashMap<>();

    private Map<String, Deque<String>> pathParameters;

    public RAMPathStore() {
        MappingConfig mappingConfig = (MappingConfig) Config.getInstance().getJsonObjectConfig(MappingConfig.CONFIG_NAME,
                MappingConfig.class);
        pathMap = mappingConfig.getPathMap();
        initPaths();
        pathParameters = new TreeMap<>();
    }

    public void initPaths() {
        if (pathMap !=null && pathMap.size()>0) {
            pathMap.forEach((k,v)->v.forEach((m,c) -> addPathChain(k, m)));
        }
    }

    public void addPathChain(String path, String method) {
        HttpString httpMethodStr = new HttpString(method);
        // use a random integer as the id for a given path
        Integer randInt = new Random().nextInt();
        PathTemplateMatcher<String> pathTemplateMatcher = methodToMatcherMap.containsKey(httpMethodStr)
                ? methodToMatcherMap.get(httpMethodStr)
                : new PathTemplateMatcher<>();

        if(pathTemplateMatcher.get(path) == null) { pathTemplateMatcher.add(path, randInt.toString()); }
        methodToMatcherMap.put(httpMethodStr, pathTemplateMatcher);
    }

    @Override
    public boolean hasPath(String path, HttpMethod method) {
        HttpString httpMethodStr = new HttpString(method.name());
        if (methodToMatcherMap.containsKey(httpMethodStr)) {
            PathTemplateMatcher<String> pathTemplateMatcher = methodToMatcherMap.get(httpMethodStr);
            PathTemplateMatcher.PathMatchResult<String> result = pathTemplateMatcher
                    .match(path);
            if (result!=null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public PathObject getPath(String path, HttpMethod method) {
        HttpString httpMethodStr = new HttpString(method.name());
        if (methodToMatcherMap.containsKey(httpMethodStr)) {
            PathTemplateMatcher<String> pathTemplateMatcher = methodToMatcherMap.get(httpMethodStr);
            PathTemplateMatcher.PathMatchResult<String> result = pathTemplateMatcher
                    .match(path);
            if (result!=null) {
                logger.info("Matched template:" + result.getMatchedTemplate());
                pathParameters.clear();
                for (Map.Entry<String, String> entry:result.getParameters().entrySet()) {
                    addPathParam(entry.getKey(), entry.getValue());
                }
                return pathMap.get(result.getMatchedTemplate()).get(method.name());
            }
        }

        return null;
    }

    @Override
    public Map<String, Deque<String>> getPathParams() {
        return pathParameters;
    }

    @Override
    public void addPathObject(String path, String method, PathObject pathObject) {
        if (pathMap.get(path) == null) {
            Map<String, PathObject> pathObjectMap = new HashMap<>();
            pathObjectMap.put(method, pathObject);
            pathMap.put(path, pathObjectMap);
        } else {
            pathMap.get(path).put(method, pathObject);
        }
        addPathChain(path, method);
    }

    @Override
    public void createResponse(String path, String content) {
        responseMap.put(path, content);
    }

    @Override
    public String getResponse(String path) {
        return responseMap.get(path);
    }

    private void addPathParam(String name, String param) {
        if (this.pathParameters == null) {
            this.pathParameters = new TreeMap();
        }

        Deque<String> list = (Deque)this.pathParameters.get(name);
        if (list == null) {
            this.pathParameters.put(name, list = new ArrayDeque(2));
        }

        ((Deque)list).add(param);
    }
}
