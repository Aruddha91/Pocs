package com.example.reactivekafkaconsumer.config;

import com.example.reactivekafkaconsumer.dto.BookingDTO;
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
    public ReceiverOptions<String, BookingDTO> kafkaReceiverOptions(@Value(value = "${CONSUMER_DTO_TOPIC}") String topic, KafkaProperties kafkaProperties) {
        ReceiverOptions<String, BookingDTO> basicReceiverOptions = ReceiverOptions.create(kafkaProperties.buildConsumerProperties());
        return basicReceiverOptions.subscription(Collections.singletonList(topic));
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, BookingDTO> reactiveKafkaConsumerTemplate(ReceiverOptions<String, BookingDTO> kafkaReceiverOptions) {
        return new ReactiveKafkaConsumerTemplate<String, BookingDTO>(kafkaReceiverOptions);
    }
}
