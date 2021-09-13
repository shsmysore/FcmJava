package io.github.shsmysore.fcmjava.client.tests.integration;

import io.github.shsmysore.fcmjava.client.FcmClient;
import io.github.shsmysore.fcmjava.exceptions.FcmAuthenticationException;
import io.github.shsmysore.fcmjava.exceptions.FcmBadRequestException;
import io.github.shsmysore.fcmjava.exceptions.FcmGeneralException;
import io.github.shsmysore.fcmjava.exceptions.FcmRetryAfterException;
import io.github.shsmysore.fcmjava.http.options.IFcmClientSettings;
import io.github.shsmysore.fcmjava.model.enums.ErrorCodeEnum;
import io.github.shsmysore.fcmjava.model.options.FcmMessageOptions;
import io.github.shsmysore.fcmjava.requests.notification.NotificationPayload;
import io.github.shsmysore.fcmjava.requests.notification.NotificationUnicastMessage;
import io.github.shsmysore.fcmjava.responses.FcmMessageResponse;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.nio.charset.Charset;

@Ignore
public class FcmClientJerseyIntegrationTest extends JerseyTest {
    private RestResource restResource;
    private FcmClient fcmClient;

    @Before
    public void setup() {
        fcmClient = new FcmClient(new IFcmClientSettings() {
            @Override
            public String getFcmUrl() {
                return "http://localhost:" + getPort() + "/send";
            }

            @Override
            public String getApiKey() {
                return "key";
            }
        });
    }

    @Test
    public void testSuccess() throws Exception {
        setResponse(200, "success.json");
        FcmMessageResponse response = sendSimpleMessage();

        Assert.assertEquals(123456789, response.getMulticastId());
        Assert.assertEquals(1, response.getNumberOfSuccess());
        Assert.assertEquals(0, response.getNumberOfCanonicalIds());
        Assert.assertEquals(0, response.getNumberOfFailure());
        Assert.assertEquals(1, response.getResults().size());
        Assert.assertEquals("message_1", response.getResults().get(0).getMessageId());
    }

    @Test(expected = FcmRetryAfterException.class)
    public void test500_withRetryAfter() throws Exception {
        restResource.response = Response.status(500).header("Retry-After", 10).build();

        try {
            sendSimpleMessage();
        } catch (FcmRetryAfterException e) {
            Assert.assertEquals(10, e.getRetryDelay().getSeconds());
            throw e;
        }
    }

    @Test(expected = FcmGeneralException.class)
    public void test500_withoutRetryAfter() throws Exception {
        restResource.response = Response.status(500).build();

        try {
            sendSimpleMessage();
        } catch(FcmGeneralException e) {
            Assert.assertEquals(500, e.getHttpStatusCode());
            throw e;
        }
    }

    @Test(expected = FcmBadRequestException.class)
    public void testBadRequest() throws Exception {
        restResource.response = Response.status(400).build();
        sendSimpleMessage();
    }

    @Test(expected = FcmAuthenticationException.class)
    public void testUnauthorized() throws Exception {
        restResource.response = Response.status(401).build();
        sendSimpleMessage();
    }

    @Test
    public void testMissingRegistrationToken() throws Exception {
        testError("MissingRegistration", ErrorCodeEnum.MissingRegistration);
    }

    @Test
    public void testInvalidRegistration() throws Exception {
        testError("InvalidRegistration", ErrorCodeEnum.InvalidRegistration);
    }
    @Test
    public void testNotRegistered() throws Exception {
        testError("NotRegistered", ErrorCodeEnum.NotRegistered);
    }
    @Test
    public void testInvalidPackageName() throws Exception {
        testError("InvalidPackageName", ErrorCodeEnum.InvalidPackageName);
    }
    @Test
    public void testMismatchSenderId() throws Exception {
        testError("MismatchSenderId", ErrorCodeEnum.MismatchSenderId);
    }
    @Test(expected = FcmBadRequestException.class)
    public void testInvalidParameters() throws Exception {
        testError(400,"InvalidParameters", ErrorCodeEnum.InvalidParameters);
    }
    @Test
    public void testMessageTooBig() throws Exception {
        testError("MessageTooBig", ErrorCodeEnum.MessageTooBig);
    }
    @Test
    public void testInvalidDataKey() throws Exception {
        testError("InvalidDataKey", ErrorCodeEnum.InvalidDataKey);
    }
    @Test
    public void testInvalidTtl() throws Exception {
        testError("InvalidTtl", ErrorCodeEnum.InvalidTtl);
    }

    @Test
    public void testUnavailable() throws Exception {
        testError("Unavailable", ErrorCodeEnum.Unavailable);
    }

    @Test
    public void testInternalServerError() throws Exception {
        testError("InternalServerError", ErrorCodeEnum.InternalServerError);
    }
    @Test
    public void testDeviceMessageRateExceeded() throws Exception {
        testError("DeviceMessageRateExceeded", ErrorCodeEnum.DeviceMessageRateExceeded);
    }
    @Test
    public void testTopicsMessageRateExceeded() throws Exception {
        testError("TopicsMessageRateExceeded", ErrorCodeEnum.TopicsMessageRateExceeded);
    }
    @Test
    public void testInvalidApnsCredential() throws Exception {
        testError("InvalidApnsCredential", ErrorCodeEnum.InvalidApnsCredential);
    }

    @Test
    public void testCanonicalRegistrationId() throws Exception {
        setResponse(200, "canonical.json");
        FcmMessageResponse response = sendSimpleMessage();

        Assert.assertEquals(1, response.getNumberOfSuccess());
        Assert.assertEquals(1, response.getNumberOfCanonicalIds());
        Assert.assertEquals(0, response.getNumberOfFailure());
        Assert.assertEquals(1, response.getResults().size());
        Assert.assertEquals("new_token", response.getResults().get(0).getCanonicalRegistrationId());
    }

    public void testError(String errorType, ErrorCodeEnum errorCodeEnum) throws Exception {
        testError(200, errorType, errorCodeEnum);
    }
    public void testError(int statusCode, String errorType, ErrorCodeEnum errorCodeEnum) throws Exception {
        InputStream body = this.getClass().getResourceAsStream("/error.json");
        String template = IOUtils.toString(body, Charset.defaultCharset());
        String replaced = template.replace("${error}", errorType);

        restResource.response = Response.status(statusCode).entity(replaced).build();

        FcmMessageResponse response = sendSimpleMessage();
        Assert.assertEquals(1, response.getResults().size());
        Assert.assertEquals(errorCodeEnum, response.getResults().get(0).getErrorCode());
    }

    private void setResponse(int status, String pathToBody) throws Exception {
        InputStream body = this.getClass().getResourceAsStream("/" + pathToBody);
        if(body == null) {
            Assert.fail("Could not find " + pathToBody);
        }
        restResource.response = Response.status(status).entity(body).build();
    }

    private FcmMessageResponse sendSimpleMessage() {
        NotificationPayload payload = NotificationPayload.builder()
                .setBody("body")
                .setTitle("title")
                .build();
        FcmMessageOptions options = FcmMessageOptions.builder()
                .build();
        return fcmClient.send(new NotificationUnicastMessage(options, "device", payload));
    }

    @Override
    protected Application configure() {
        restResource = new RestResource();
        forceSet(TestProperties.CONTAINER_PORT, "0");

        return new ResourceConfig()
                .register(restResource);
    }

    @Path("send")
    public static class RestResource {
        public Response response;

        @POST
        public Response send() throws Exception {
            return response;
        }
    }
}
