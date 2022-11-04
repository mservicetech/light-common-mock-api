
package com.mservicetech.mock.api.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mservicetech.mock.api.model.Response;
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
import io.undertow.server.handlers.form.FormData;
import io.undertow.util.HeaderValues;
import io.undertow.util.HttpString;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.IoUtils;
import org.xnio.OptionMap;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;


@Disabled
@ExtendWith(TestServer.class)
public class MockResponseCreatePostHandlerTest {

    public static TestServer server = TestServer.getInstance();

    static final Logger logger = LoggerFactory.getLogger(MockResponseCreatePostHandlerTest.class);
    static final boolean enableHttp2 = server.getServerConfig().isEnableHttp2();
    static final boolean enableHttps = server.getServerConfig().isEnableHttps();
    static final int httpPort = server.getServerConfig().getHttpPort();
    static final int httpsPort = server.getServerConfig().getHttpsPort();
    static final String url = enableHttps ? "https://localhost:" + httpsPort : "http://localhost:" + httpPort;
    static final String JSON_MEDIA_TYPE = "application/json";
    static final String FORM_DATA_TYPE = "multipart/form-data";

    @Test
    public void testMockResponseCreatePostHandlerTest() throws ClientException {

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
        String requestUri = "/mock/response/create";
        String httpMethod = "post";
        try {
            ClientRequest request = new ClientRequest().setPath(requestUri).setMethod(Methods.POST);
            ObjectMapper objectMapper = Config.getInstance().getMapper();

            request.getRequestHeaders().put(Headers.CONTENT_TYPE, FORM_DATA_TYPE);
            request.getRequestHeaders().put(Headers.TRANSFER_ENCODING, "chunked");
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("test.json");

            HashMap<String, Object> requestMap = new HashMap<>();
            requestMap.put("responsePath", "response/test.json");
            requestMap.put("responseFile", IOUtils.toByteArray(resourceAsStream));
            request.getRequestHeaders().put(new HttpString("host"), "localhost");
            connection.sendRequest(request, client.byteBufferClientCallback(reference, latch, ByteBuffer.wrap(SerializationUtils.serialize(requestMap))));

            latch.await();
        } catch (Exception e) {
            logger.error("Exception: ", e);
            throw new ClientException(e);
        } finally {
            client.returnConnection(connection);
        }
        String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
        Optional<HeaderValues> contentTypeName = Optional.ofNullable(reference.get().getResponseHeaders().get(Headers.CONTENT_TYPE));
        int statusCode = reference.get().getResponseCode();
        Status status;
        //assertEquals(statusCode, 200);

   }
}

