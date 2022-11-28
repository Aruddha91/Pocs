package com.example.reactivekafkaconsumer.service;

import com.example.reactivekafkaconsumer.dto.BookingDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


@Service
public class ReactiveConsumerService implements CommandLineRunner {
    Logger log = LoggerFactory.getLogger(ReactiveConsumerService.class);

    private final ReactiveKafkaConsumerTemplate<String, BookingDTO> reactiveKafkaConsumerTemplate;

    public ReactiveConsumerService(ReactiveKafkaConsumerTemplate<String, BookingDTO> reactiveKafkaConsumerTemplate) {
        this.reactiveKafkaConsumerTemplate = reactiveKafkaConsumerTemplate;
    }

    private Flux<BookingDTO> consume() {
        return reactiveKafkaConsumerTemplate
                .receiveAutoAck()
                // .delayElements(Duration.ofSeconds(2L)) // BACKPRESSURE
                .doOnNext(consumerRecord -> log.info("received key={}, value={} : bookingNumber={}, externalBookingIdentifier={}," +
                                "totalBookedPackageQuantity ={} from topic={}, offset={}",
                        consumerRecord.key(),
                        consumerRecord.value(),
                        consumerRecord.value().getBookingNumber(),
                        consumerRecord.value().getExternalBookingIdentifier(),
                        consumerRecord.value().getTotalBookedPackageQuantity(),
                        consumerRecord.topic(),
                        consumerRecord.offset())
                )
                .map(ConsumerRecord::value)
                .doOnNext(order -> log.info("successfully consumed {}={}", BookingDTO.class.getSimpleName(), order))
                .doOnError(throwable -> log.error("something bad happened while consuming : {}", throwable.getMessage()));
    }

    @Override
    public void run(String... args) {
        // we have to trigger consumption
        consume().subscribe();
    }
}
