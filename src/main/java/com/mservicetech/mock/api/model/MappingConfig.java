package com.mservicetech.mock.api.model;

import java.util.HashMap;
import java.util.Map;

public class MappingConfig {

    public static final String CONFIG_NAME = "mapping-config";

    private Map<String, Map<String, PathObject>> pathMap = new HashMap<>();

    public MappingConfig() {
        //intentionally empty
    }

    public Map<String, Map<String, PathObject>> getPathMap() {
        return pathMap;
    }

    public void setPathMap(Map<String, Map<String, PathObject>> pathMap) {
        this.pathMap = pathMap;
    }
}
