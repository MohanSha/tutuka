package com.tutuka.pubsubclient;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PubSubClientTest {

    private PubSubClient pubSubClient;

    private final static String WIREMOCK_FILES_PATH = "src/test/resources/wiremock/";

    private final static int WIREMOCK_PORT = 7719;

    private static WireMockServer WIREMOCK_SERVER;

    @BeforeAll
    static void beforeAll() {
        WireMockConfiguration wireMockConfiguration = new WireMockConfiguration()
                .port(WIREMOCK_PORT)
                .usingFilesUnderClasspath(WIREMOCK_FILES_PATH);
        WIREMOCK_SERVER = new WireMockServer(wireMockConfiguration);
        WIREMOCK_SERVER.start();
    }

    @BeforeEach
    void setUp() {
        pubSubClient = new PubSubClient("http://localhost:7719");
    }

    @Test
    @DisplayName("Test Payload is success to published to channel")
    void testPublishPayLoadToChannelSuccess() {
        // given
        String payload = "{\n  \"CHANNEL\": \"TEST1\",\n  \"PAYLOAD\": {\n    \"response_code\": \"0000\",\n    \"merchant_category_code\": \"6969\",\n    \"system_time\": \"1612280213\",\n    \"terminal_id\": \"112233445566\",\n    \"capture_mode\": \"ECOM\",\n    \"request_time\": \"1612255011\",\n    \"merchant\": \"APPLE.COM/BILL         ITUNES.COM    IRL\",\n    \"balance\": 0,\n    \"amount\": 123,\n    \"tracking_number\": \"123123123123123\",\n    \"transaction_time\": \"1612273011\",\n    \"reference\": \"ref.334d\",\n    \"transaction_id\": \"613455\",\n    \"type\": \"deduct authorisation\",\n    \"campaign\": \"CID123456-65432-1234456\"\n  }\n}";

        String channel = "TEST1";

        // when
        String response = pubSubClient.publish(payload, channel);

        // then
        assertEquals("{\"status\":\"ok\"}", response);
    }

    @Test
    @DisplayName("Test Payload is failed to published to channel")
    void testPublishPayLoadToChannelFailed() {
        // given
        String payload = "{\n  \"CHANNEL\": \"TEST1\",\n  \"PAYLOAD\": {\n    \"response_code\": \"0000\",\n    \"merchant_category_code\": \"6969\",\n    \"system_time\": \"1612280213\",\n    \"terminal_id\": \"112233445566\",\n    \"capture_mode\": \"ECOM\",\n    \"request_time\": \"1612255011\",\n    \"merchant\": \"APPLE.COM/BILL         ITUNES.COM    IRL\",\n    \"balance\": 0,\n    \"amount\": 123,\n    \"tracking_number\": \"123123123123123\",\n    \"transaction_time\": \"1612273011\",\n    \"reference\": \"ref.334d\",\n    \"transaction_id\": \"613455\",\n    \"type\": \"deduct authorisation\",\n    \"campaign\": \"CID123456-65432-1234456\"\n  }\n}";

        String channel = "TEST_ERROR_CHANNEL";

        // when
        String response = pubSubClient.publish(payload, channel);

        // then
        assertEquals("", response);
    }

    @AfterAll
    static void afterAll() {
        WIREMOCK_SERVER.stop();
    }
}
