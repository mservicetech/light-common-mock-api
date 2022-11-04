package com.mservicetech.mock.api.handler;


import com.mservicetech.mock.api.model.MappingConfig;
import com.mservicetech.mock.api.model.PathObject;
import com.networknt.config.Config;
import com.networknt.http.HttpMethod;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class MappingConfigTest {

    private static MappingConfig mappingConfig;


    @BeforeAll
    public static void setUp() {
        mappingConfig = (MappingConfig) Config.getInstance().getJsonObjectConfig(MappingConfig.CONFIG_NAME, MappingConfig.class);
    }

    @Test
    public void testConfigValue() {
        assertEquals(mappingConfig.getPathMap().size(), 3);
    }

    @Test
    public void testConfigDetail(){
        PathObject pathObject = mappingConfig.getPathMap().get("/mesh/core").get(HttpMethod.GET.name());
        assertEquals(pathObject.getDefaultResponse().getResponsePath(), "response/core3.json");
    }

}
