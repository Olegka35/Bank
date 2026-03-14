package com.tarasov.bank.notification;


import com.tarasov.bank.notification.configuration.NoSecurityConfig;
import com.tarasov.bank.notification.dto.NotificationRequest;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.List;

import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasKey;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasValue;


@SpringBootTest(properties = {"spring.config.import=optional:configserver:"})
@Import(NoSecurityConfig.class)
@EmbeddedKafka(topics = {"transer-notifications", "cash-notifications", "account-notifications"})
public class NotificationConsumerTest {

    @Autowired
    private KafkaTemplate<String, NotificationRequest> kafkaTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    public void testKafkaNotificationConsume() throws InterruptedException {
        try (var consumerForTest = new DefaultKafkaConsumerFactory<>(
                KafkaTestUtils.consumerProps(embeddedKafkaBroker, "test-service", true),
                new StringDeserializer(),
                new JacksonJsonDeserializer<>(NotificationRequest.class)
        ).createConsumer()) {
            consumerForTest.subscribe(List.of("transer-notifications", "cash-notifications", "account-notifications"));

            NotificationRequest notification = new NotificationRequest("oleg", "Test message");
            kafkaTemplate.send("cash-notifications", "oleg", notification);

            var inputMessage = KafkaTestUtils.getSingleRecord(consumerForTest, "cash-notifications", Duration.ofSeconds(5));
            MatcherAssert.assertThat(inputMessage, hasKey("oleg"));
            MatcherAssert.assertThat(inputMessage, hasValue(notification));
        }
    }
}
