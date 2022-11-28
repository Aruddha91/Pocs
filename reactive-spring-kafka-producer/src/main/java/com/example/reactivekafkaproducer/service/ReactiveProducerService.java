package com.example.reactivekafkaproducer.service;

import com.example.reactivekafkaproducer.dto.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;

@Service
public class ReactiveProducerService  {

    private final Logger log = LoggerFactory.getLogger(ReactiveProducerService.class);

    private final ReactiveKafkaProducerTemplate<String, OrderDTO> reactiveKafkaProducerTemplate;

    @Value("${PRODUCER_DTO_TOPIC}")
    private String topic;

    public ReactiveProducerService(ReactiveKafkaProducerTemplate<String, OrderDTO> reactiveKafkaProducerTemplate) {
        this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
    }

    public void send(OrderDTO order) {
        log.info("send to topic={}, {}={},", topic, OrderDTO.class.getSimpleName(), order);
        reactiveKafkaProducerTemplate.send(topic, order)
                    .doOnSuccess(senderResult -> log.info("sent {} offset : {}", order, senderResult.recordMetadata().offset()))
                    .subscribe();

    }

    /*@Override
    public void run(String... args) throws Exception {
        *//*OrderDTO dummyOrder = new OrderDTO();
        dummyOrder.setOrderId("dummy");
        send(dummyOrder);*//*
        Map<String, Object> props = new KafkaProperties().buildProducerProperties();
        new ReactiveKafkaProducerTemplate<String, OrderDTO>(SenderOptions.create(props));
    }*/
}