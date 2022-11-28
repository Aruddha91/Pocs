package com.example.reactivekafkaproducer.controller;

import com.example.reactivekafkaproducer.dto.OrderDTO;
import com.example.reactivekafkaproducer.service.ReactiveProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReactiveKafkaController {
    private final Logger log = LoggerFactory.getLogger(ReactiveKafkaController.class);
    private final com.example.reactivekafkaproducer.service.ReactiveProducerService producer;

    public ReactiveKafkaController(ReactiveProducerService producer) {
        this.producer = producer;
    }

    @PostMapping("/reactive-publish")
    public void writeMessageToTopic(@RequestBody OrderDTO order){
        log.info("in reactive controller...");
        //FakeProducerDTO dto = new FakeProducerDTO(id);

        this.producer.send(order);
    }



    /*@GetMapping("/reactive-publish")
    public void writeMessageToTopic(@RequestParam("id") String id){
        log.info("in reactive controller...");
        //FakeProducerDTO dto = new FakeProducerDTO(id);

        this.producer.send(id);
    }*/

}