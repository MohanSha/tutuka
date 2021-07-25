package com.tutuka.redis;


import com.tutuka.pubsubclient.PubSubClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.util.backoff.BackOffExecution;
import org.springframework.util.backoff.ExponentialBackOff;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PubSubConsumerTest {

    private PubSubConsumer pubSubConsumer;

    @Mock
    private PubSubClient pubSubClient;

    @Mock
    private ListOperations<Object, Object> redisClient;


    private static String REDIS_QUEUE_NAME = "test-redis-queue";

    @BeforeEach
    void setUp() {
        pubSubConsumer = new PubSubConsumer(redisClient, pubSubClient, REDIS_QUEUE_NAME);
    }

    @Test
    @DisplayName("Test consume success")
    void testConsumeSuccess() {
        // given
        when(redisClient.rightPop("test-redis-queue")).thenReturn("{\n  \"CHANNEL\": \"TEST1\",\n  \"PAYLOAD\": {\n    \"response_code\": \"0000\",\n    \"merchant_category_code\": \"6969\",\n    \"system_time\": \"1612280213\",\n    \"terminal_id\": \"112233445566\",\n    \"capture_mode\": \"ECOM\",\n    \"request_time\": \"1612255011\",\n    \"merchant\": \"APPLE.COM/BILL         ITUNES.COM    IRL\",\n    \"balance\": 0,\n    \"amount\": 123,\n    \"tracking_number\": \"123123123123123\",\n    \"transaction_time\": \"1612273011\",\n    \"reference\": \"ref.334d\",\n    \"transaction_id\": \"613455\",\n    \"type\": \"deduct authorisation\",\n    \"campaign\": \"CID123456-65432-1234456\"\n  }\n}");

        // when
        pubSubConsumer.consume();

        // then
        verify(pubSubClient).publish(anyString(), anyString());
    }

    @Test
    @DisplayName("Test consume failed when message is having missing PAYLOAD")
    void testConsumeFailedWhenMessageIsHavingMissingPayload() {
        // given
        when(redisClient.rightPop("test-redis-queue")).thenReturn("{\n  \"CHANNEL\": \"TEST1\"}");

        // when
        pubSubConsumer.consume();

        // then
        verify(pubSubClient, never()).publish(anyString(), anyString());
    }

    @Test
    @DisplayName("Test consume failed when message is having missing CHANNEL")
    void testConsumeFailedWhenMessageIsHavingMissingChannel() {
        // given
        when(redisClient.rightPop("test-redis-queue")).thenReturn("{\"PAYLOAD\": {\n    \"response_code\": \"0000\",\n    \"merchant_category_code\": \"6969\",\n    \"system_time\": \"1612280213\",\n    \"terminal_id\": \"112233445566\",\n    \"capture_mode\": \"ECOM\",\n    \"request_time\": \"1612255011\",\n    \"merchant\": \"APPLE.COM/BILL         ITUNES.COM    IRL\",\n    \"balance\": 0,\n    \"amount\": 123,\n    \"tracking_number\": \"123123123123123\",\n    \"transaction_time\": \"1612273011\",\n    \"reference\": \"ref.334d\",\n    \"transaction_id\": \"613455\",\n    \"type\": \"deduct authorisation\",\n    \"campaign\": \"CID123456-65432-1234456\"\n  }\n}");

        // when
        pubSubConsumer.consume();

        // then
        verify(pubSubClient, never()).publish(anyString(), anyString());
    }

    @Test
    @DisplayName("Test consume when failed to connect to publish server multiple times then retry and succeed after exponentialBackOff")
    void testConsumeWhenFailedToPublishToServer() throws NoSuchFieldException, IllegalAccessException {
        // given
        when(redisClient.rightPop("test-redis-queue")).thenReturn("{\n  \"CHANNEL\": \"TEST1\",\n  \"PAYLOAD\": {\n    \"response_code\": \"0000\",\n    \"merchant_category_code\": \"6969\",\n    \"system_time\": \"1612280213\",\n    \"terminal_id\": \"112233445566\",\n    \"capture_mode\": \"ECOM\",\n    \"request_time\": \"1612255011\",\n    \"merchant\": \"APPLE.COM/BILL         ITUNES.COM    IRL\",\n    \"balance\": 0,\n    \"amount\": 123,\n    \"tracking_number\": \"123123123123123\",\n    \"transaction_time\": \"1612273011\",\n    \"reference\": \"ref.334d\",\n    \"transaction_id\": \"613455\",\n    \"type\": \"deduct authorisation\",\n    \"campaign\": \"CID123456-65432-1234456\"\n  }\n}");


        ExponentialBackOff mockExponentialBackOff = mock(ExponentialBackOff.class);
        BackOffExecution mockBackOffExecution = mock(BackOffExecution.class);
        when(mockExponentialBackOff.start()).thenReturn(mockBackOffExecution);

        when(mockBackOffExecution.nextBackOff()).thenReturn(100L);

        Field exponentialBackOffPrivateField = pubSubConsumer.getClass().getDeclaredField("exponentialBackOff");
        exponentialBackOffPrivateField.setAccessible(true);
        exponentialBackOffPrivateField.set(pubSubConsumer, mockExponentialBackOff);

        Mockito.doThrow(new RuntimeException("Error connecting to Publish Server"))
                .doThrow(new RuntimeException("Error connecting to Publish Server"))
                .doThrow(new RuntimeException("Error connecting to Publish Server"))
                .doThrow(new RuntimeException("Error connecting to Publish Server"))
                .doThrow(new RuntimeException("Error connecting to Publish Server"))
                .doThrow(new RuntimeException("Error connecting to Publish Server"))
                .doThrow(new RuntimeException("Error connecting to Publish Server"))
                .doThrow(new RuntimeException("Error connecting to Publish Server"))
                .doThrow(new RuntimeException("Error connecting to Publish Server"))
                .doThrow(new RuntimeException("Error connecting to Publish Server"))
                .doReturn("success")
                .when(pubSubClient).publish(anyString(), anyString());

        // when
        pubSubConsumer.consume();

        // then
        verify(pubSubClient, times(10)).publish(anyString(), anyString());
        verify(redisClient, times(10)).size("test-redis-queue");
        verify(redisClient, times(1)).leftPush(anyString(), anyString());
    }

    @Test
    @DisplayName("Test consume failed when error to connect or read from redis queue")
    void testConsumeWhenFailedToGetMessageFromQueue() {
        // given
        when(redisClient.rightPop("test-redis-queue")).thenThrow(new RuntimeException("Error connecting to redis queue"));

        // when
        pubSubConsumer.consume();

        // then
        verify(pubSubClient, never()).publish(anyString(), anyString());
    }
}