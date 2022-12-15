package com.example.reactivekafkaproducer.config;

import com.example.reactivekafkaproducer.dto.BookingDTO;
import org.apache.kafka.clients.admin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;

import java.util.Map;

import java.util.Map;

@Configuration
public class ReactiveKafkaProducerConfig {

    @Autowired
    private KafkaAdmin kafkaAdmin;

    @Bean
    public AdminClient kafkaAdminClient() {
        return AdminClient.create(kafkaAdmin.getConfigurationProperties());
    }
    @Bean
    public ReactiveKafkaProducerTemplate<String, BookingDTO> reactiveKafkaProducerTemplate(
            KafkaProperties properties) {
        Map<String, Object> props = properties.buildProducerProperties();
        return new ReactiveKafkaProducerTemplate<String, BookingDTO>(SenderOptions.create(props));
    }

    @Bean
    public HealthIndicator kafkaHealthIndicator(AdminClient kafkaAdminClient) {
        final DescribeClusterOptions options = new DescribeClusterOptions()
                .timeoutMs(1000);
        final ListTopicsOptions listTopicsOptions = new ListTopicsOptions().timeoutMs(1000);
        return new AbstractHealthIndicator() {

            @Override
            protected void doHealthCheck(Health.Builder builder) throws Exception {
                DescribeClusterResult clusterDescription = kafkaAdminClient.describeCluster(options);
                kafkaAdminClient.metrics().values();

                ListTopicsResult lt = kafkaAdminClient.listTopics(listTopicsOptions);

                // In order to trip health indicator DOWN retrieve data from one of
                // future objects otherwise indicator is UP even when Kafka is down!!!
                // When Kafka is not connected future.get() throws an exception which
                // in turn sets the indicator DOWN.
                clusterDescription.clusterId().get();
                // or clusterDescription.nodes().get().size()
                // or clusterDescription.controller().get();

                builder.up().build();


                // Alternatively directly use data from future in health detail.
                builder.up()
                        .withDetail("clusterId", clusterDescription.clusterId().get())
                        .withDetail("nodeCount", clusterDescription.nodes().get().size())
                        .withDetail("topics",lt.names().get())

                        .build();
            }
        };
    }
}
