package com.mservicetech.mock.api.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mservicetech.mock.api.model.BuildResult;
import com.mservicetech.mock.api.model.PathConfig;
import com.mservicetech.mock.api.model.Response;
import com.mservicetech.mock.api.service.CaseProcessService;
import com.networknt.body.BodyHandler;
import com.networknt.config.Config;

import com.networknt.handler.LightHttpHandler;
import com.networknt.http.HttpMethod;
import com.networknt.http.HttpStatus;
import com.networknt.http.MediaType;
import com.networknt.service.SingletonServiceFactory;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData;
import io.undertow.util.Headers;
import io.undertow.util.HeaderMap;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
For more information on how to write business handlers, please check the link below.
https://doc.networknt.com/development/business-handler/rest/
*/
public class MockResponseCreatePostHandler implements LightHttpHandler {


    private static final ObjectMapper objectMapper = Config.getInstance().getMapper();

    private static CaseProcessService caseProcessService = SingletonServiceFactory.getBean(CaseProcessService.class);
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

        Map<String, Object> jsonMap = (Map<String, Object>) exchange.getAttachment(com.networknt.body.BodyHandler.REQUEST_BODY);

        String responsePath = (String)jsonMap.get("responsePath");
        FormData.FileItem fileItem = (FormData.FileItem)jsonMap.get("responseFile");
        logger.info("file size:"+ fileItem.getFileSize());
        Response response = new Response();
        response.setResponsePath(responsePath);
        InputStream inputStream = fileItem.getInputStream();
        response.setResponseBody( new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));
        IOUtils.closeQuietly(inputStream);

        BuildResult buildResult = caseProcessService.createResponse(response);

        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.setStatusCode(HttpStatus.OK.value());
        exchange.getResponseSender().send(objectMapper.writeValueAsString(buildResult));
    }
}
