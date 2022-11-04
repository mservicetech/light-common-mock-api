package com.mservicetech.mock.api.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mservicetech.mock.api.MockApiStartupHook;
import com.mservicetech.mock.api.model.Case;
import com.mservicetech.mock.api.model.PathConfig;
import com.mservicetech.mock.api.model.PathObject;
import com.mservicetech.mock.api.model.Response;
import com.mservicetech.mock.api.service.CaseProcessService;
import com.networknt.body.BodyHandler;
import com.networknt.config.Config;
import com.networknt.handler.LightHttpHandler;
import com.networknt.http.HttpMethod;
import com.networknt.http.HttpStatus;
import com.networknt.http.MediaType;
import com.networknt.service.SingletonServiceFactory;
import com.networknt.status.Status;
import com.networknt.utility.StringUtils;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


public class MappingPathHandler implements LightHttpHandler {

    private static final ObjectMapper objectMapper = Config.getInstance().getMapper();

    private static CaseProcessService caseProcessService = SingletonServiceFactory.getBean(CaseProcessService.class);

    private static final String INVALID_REQUEST_PATH = "ERR10007";
    private static final String NO_CONENT_DEFINED = "ERR30000";
    private static final String MISSING_QUERY_PARAMES = "ERR11000";

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        HttpMethod httpMethod = HttpMethod.resolve(exchange.getRequestMethod().toString());
        String path = exchange.getRequestPath();
        Map<String, Deque<String>> queryParameters = exchange.getQueryParameters();
        HeaderMap requestHeaders = exchange.getResponseHeaders();
        PathObject pathObject = null;
        if (!MockApiStartupHook.pathStore.hasPath(path, httpMethod)) {
            pathNotFound(exchange, path);
        }
        pathObject = MockApiStartupHook.pathStore.getPath(path, httpMethod);
        Map<String, Deque<String>> pathParameters = MockApiStartupHook.pathStore.getPathParams();
        //Merge all parameters for case/conditions
        Map<String, Deque<String>> params = new HashMap<>(queryParameters);
        params.putAll(pathParameters);
        if (requestHeaders.size()>0) {
            requestHeaders.iterator().forEachRemaining(r->
                    params.put(r.getHeaderName().toString(), Arrays.stream(r.toArray()).collect(Collectors.toCollection(ArrayDeque::new))));
        }
        if (HttpMethod.GET.equals(httpMethod) && pathObject.getRequired()!=null) {
            List<String> requiredParams = Arrays.asList(pathObject.getRequired().split(","));
            requiredParams.stream().forEach(p->{
                if (queryParameters.get(p)==null) {
                    try {
                        missParameters(exchange, path, p);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
         String body = null;
        if (HttpMethod.POST.equals(httpMethod) || HttpMethod.PUT.equals(httpMethod)) {
            body = objectMapper.writeValueAsString(exchange.getAttachment(BodyHandler.REQUEST_BODY));
            logger.info("request body:" + body);
        }
        Case caseMatch = caseProcessService.processConditions(params, body, pathObject.getCases());
        if (caseMatch == null) {
            responseProcess(path, pathObject.getDefaultResponse(), exchange);
        } else {
            responseProcess(path, caseMatch.getResponse(), exchange);
        }
    }

    private void responseProcess(String path, Response response, HttpServerExchange exchange) throws Exception {
        if (response.getResponseBody()!=null && !StringUtils.isEmpty(response.getResponseBody())) {
            exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            exchange.setStatusCode(response.getStatusCode()==null?200:response.getStatusCode());
            exchange.getResponseSender().send(response.getResponseBody());
        } else if(response.getResponsePath()!=null && !StringUtils.isEmpty(response.getResponsePath())) {
            String responseBody = MockApiStartupHook.pathStore.getResponse(response.getResponsePath());
            if (responseBody==null) {
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(response.getResponsePath());
                responseBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }
            exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            exchange.setStatusCode(response.getStatusCode()==null?200:response.getStatusCode());
            exchange.getResponseSender().send(responseBody);

        } else {
            noResponseContent(exchange, path);
        }
    }

    private void pathNotFound(HttpServerExchange exchange, String path) throws Exception{
        Status status = new Status(INVALID_REQUEST_PATH, path);
        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        setExchangeStatus(exchange, status);
        exchange.getResponseSender().send(objectMapper.writeValueAsString(status));
    }

    private void noResponseContent(HttpServerExchange exchange, String path) throws Exception{
        Status status = new Status(NO_CONENT_DEFINED, path);
        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        setExchangeStatus(exchange, status);
        exchange.getResponseSender().send(objectMapper.writeValueAsString(status));
    }

    private void missParameters(HttpServerExchange exchange, String path, String param) throws Exception{
        Status status = new Status(MISSING_QUERY_PARAMES, param, path);
        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        setExchangeStatus(exchange, status);
        exchange.getResponseSender().send(objectMapper.writeValueAsString(status));
    }

}
