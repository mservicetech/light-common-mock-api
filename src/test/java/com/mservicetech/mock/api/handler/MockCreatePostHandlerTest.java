
package com.mservicetech.mock.api.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mservicetech.mock.api.model.*;
import com.networknt.client.Http2Client;
import com.networknt.config.Config;
import com.networknt.exception.ClientException;
import com.networknt.openapi.OpenApiHandler;
import com.networknt.openapi.ResponseValidator;
import com.networknt.openapi.SchemaValidator;
import com.networknt.status.Status;
import com.networknt.utility.StringUtils;
import io.undertow.UndertowOptions;
import io.undertow.client.ClientConnection;
import io.undertow.client.ClientRequest;
import io.undertow.client.ClientResponse;
import io.undertow.util.HeaderValues;
import io.undertow.util.HttpString;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.IoUtils;
import org.xnio.OptionMap;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;


@Disabled
@ExtendWith(TestServer.class)
public class MockCreatePostHandlerTest {

    public static TestServer server = TestServer.getInstance();

    static final Logger logger = LoggerFactory.getLogger(MockCreatePostHandlerTest.class);
    static final boolean enableHttp2 = server.getServerConfig().isEnableHttp2();
    static final boolean enableHttps = server.getServerConfig().isEnableHttps();
    static final int httpPort = server.getServerConfig().getHttpPort();
    static final int httpsPort = server.getServerConfig().getHttpsPort();
    static final String url = enableHttps ? "https://localhost:" + httpsPort : "http://localhost:" + httpPort;
    static final String JSON_MEDIA_TYPE = "application/json";

    @Test
    public void testMockCreatePostHandlerTest() throws ClientException {

        final Http2Client client = Http2Client.getInstance();
        final CountDownLatch latch = new CountDownLatch(1);
        final ClientConnection connection;
        try {
            if(enableHttps) {
                connection = client.borrowConnection(new URI(url), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, enableHttp2 ? OptionMap.create(UndertowOptions.ENABLE_HTTP2, true): OptionMap.EMPTY).get();
            } else {
                connection = client.borrowConnection(new URI(url), Http2Client.WORKER, Http2Client.BUFFER_POOL, OptionMap.EMPTY).get();
            }
        } catch (Exception e) {
            throw new ClientException(e);
        }
        final AtomicReference<ClientResponse> reference = new AtomicReference<>();
        String requestUri = "/mock/create";
        String httpMethod = "post";
        try {
            ClientRequest request = new ClientRequest().setPath(requestUri).setMethod(Methods.POST);
            
            request.getRequestHeaders().put(Headers.CONTENT_TYPE, JSON_MEDIA_TYPE);
            request.getRequestHeaders().put(Headers.TRANSFER_ENCODING, "chunked");
            ObjectMapper objectMapper = Config.getInstance().getMapper();
            List<PathConfig> pathConfigs = new ArrayList<>();
            PathConfig pathConfig = new PathConfig();
            pathConfig.setPath("mesh/query");
            pathConfig.setMethod(PathConfig.MethodEnum.GET);
            pathConfig.setRequiredParam("branchCode,accountCode");
            Response defaultResponse = new Response();
            defaultResponse.setResponsePath("response/book1.json");
            defaultResponse.setStatusCode(200);
            pathConfig.setDefaultResponse(defaultResponse);
            List<Case> cases = new ArrayList<>();
            Case caseItem = new Case();
            Response caseRes = new Response();
            caseRes.setStatusCode(200);
            caseRes.setResponsePath("response/book2.json");
            List<Header> headers = new ArrayList<>();
            Header header = new Header();
            header.setHeaderKey("roleId");
            header.setHeaderValue("1111");
            headers.add(header);
            caseRes.setHeaders(headers);
            caseItem.setResponse(caseRes);
            List<Condition> conditions = new ArrayList<>();
            Condition condition = new Condition();
            condition.setName("branchCode");
            condition.setOperator(OperatorEum.EQUALS);
            condition.setValue("510");
            conditions.add(condition);
            caseItem.setConditions(conditions);
            cases.add(caseItem);
            pathConfig.setCases(cases);
            pathConfigs.add(pathConfig);
            String req = objectMapper.writeValueAsString(pathConfigs);
            System.out.println("req json:" + req);
            //customized header parameters 
            request.getRequestHeaders().put(new HttpString("host"), "localhost");
            connection.sendRequest(request, client.createClientCallback(reference, latch, req));
            
            latch.await();
        } catch (Exception e) {
            logger.error("Exception: ", e);
            throw new ClientException(e);
        } finally {
            client.returnConnection(connection);
        }
        String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
        int statusCode = reference.get().getResponseCode();
        assertEquals(statusCode, 200);
    }
}

