package com.example.reactivekafkaproducer.config;

import com.example.reactivekafkaproducer.dto.BookingDTO;
import com.example.reactivekafkaproducer.dto.OrderDTO;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;

import java.util.Map;

@Configuration
public class ReactiveKafkaProducerConfig {
    @Bean
    public ReactiveKafkaProducerTemplate<String, BookingDTO> reactiveKafkaProducerTemplate(
            KafkaProperties properties) {
        Map<String, Object> props = properties.buildProducerProperties();
        return new ReactiveKafkaProducerTemplate<String, BookingDTO>(SenderOptions.create(props));
    }
}
