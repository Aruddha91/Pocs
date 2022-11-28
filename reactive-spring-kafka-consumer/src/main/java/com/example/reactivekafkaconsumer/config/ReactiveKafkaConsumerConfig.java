package com.example.reactivekafkaconsumer.config;

import com.example.reactivekafkaconsumer.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;

@Configuration
public class ReactiveKafkaConsumerConfig {
    @Bean
    public ReceiverOptions<String, OrderDTO> kafkaReceiverOptions(@Value(value = "${CONSUMER_DTO_TOPIC}") String topic, KafkaProperties kafkaProperties) {
        ReceiverOptions<String, OrderDTO> basicReceiverOptions = ReceiverOptions.create(kafkaProperties.buildConsumerProperties());
        return basicReceiverOptions.subscription(Collections.singletonList(topic));
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, OrderDTO> reactiveKafkaConsumerTemplate(ReceiverOptions<String, OrderDTO> kafkaReceiverOptions) {
        return new ReactiveKafkaConsumerTemplate<String, OrderDTO>(kafkaReceiverOptions);
    }
}
