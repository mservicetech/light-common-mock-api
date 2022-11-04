package com.mservicetech.mock.api;


import com.mservicetech.mock.api.data.PathStore;
import com.networknt.server.StartupHookProvider;
import com.networknt.service.SingletonServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockApiStartupHook implements StartupHookProvider {
    private static Logger logger = LoggerFactory.getLogger(MockApiStartupHook.class);
    public static PathStore pathStore;

    @Override
    public void onStartup() {
        logger.info("MockApiStartupHook begins");
        pathStore = SingletonServiceFactory.getBean(PathStore.class);
        logger.info("MockApiStartupHook ends");
    }
}
