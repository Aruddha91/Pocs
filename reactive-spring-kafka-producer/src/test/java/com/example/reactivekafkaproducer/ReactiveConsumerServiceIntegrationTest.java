package com.example.reactivekafkaproducer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@EmbeddedKafka(topics = {"${FAKE_PRODUCER_DTO_TOPIC}", "${FAKE_CONSUMER_DTO_TOPIC}"})
class ReactiveConsumerServiceIntegrationTest {


	@Test
	void reactiveConsumerService_receiveFakeConsumerDtoFromTopic_saveFakeConsumerDto() {
		// TODO
	}

}
