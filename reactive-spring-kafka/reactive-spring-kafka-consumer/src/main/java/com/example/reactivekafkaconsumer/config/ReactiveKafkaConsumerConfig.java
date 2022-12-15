package com.example.reactivekafkaconsumer.config;

import com.example.reactivekafkaconsumer.dto.BookingDTO;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaAdmin;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@Configuration
public class ReactiveKafkaConsumerConfig {

    @Autowired
    private KafkaAdmin kafkaAdmin;

    @Value(value = "${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean
    public AdminClient kafkaAdminClient() {
        return AdminClient.create(kafkaAdmin.getConfigurationProperties());
    }
    @Bean
    public ReceiverOptions<String, BookingDTO> kafkaReceiverOptions(@Value(value = "${CONSUMER_DTO_TOPIC}") String topic, KafkaProperties kafkaProperties) {
        ReceiverOptions<String, BookingDTO> basicReceiverOptions = ReceiverOptions.create(kafkaProperties.buildConsumerProperties());
        return basicReceiverOptions.subscription(Collections.singletonList(topic));
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, BookingDTO> reactiveKafkaConsumerTemplate(ReceiverOptions<String, BookingDTO> kafkaReceiverOptions) {
        return new ReactiveKafkaConsumerTemplate<String, BookingDTO>(kafkaReceiverOptions);
    }

    @Bean
    public HealthIndicator kafkaHealthIndicator(AdminClient kafkaAdminClient) {
        final DescribeClusterOptions options = new DescribeClusterOptions()
                .timeoutMs(1000);
        final ListTopicsOptions topicsOptions = new ListTopicsOptions().timeoutMs(1000);

        //final Collection<MessageListenerContainer> allListenerContainers = kafkaListenerEndpointRegistry.getAllListenerContainers();


        return new AbstractHealthIndicator() {

            @Override
            protected void doHealthCheck(Health.Builder builder) throws Exception {
                DescribeClusterResult clusterDescription = kafkaAdminClient.describeCluster(options);

                ListTopicsResult lt = kafkaAdminClient.listTopics(topicsOptions);

                ListConsumerGroupsResult ListConsumerGroupsResult = kafkaAdminClient.listConsumerGroups();
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
                        .withDetail("ListConsumerGroupsResult",getConsumerGrpOffsets(groupId))

                        .build();
            }
        };
    }
    private Map<TopicPartition, Long> getConsumerGrpOffsets(String groupId)
            throws ExecutionException, InterruptedException {
        ListConsumerGroupOffsetsResult info = kafkaAdminClient().listConsumerGroupOffsets(groupId);
        Map<TopicPartition, OffsetAndMetadata> topicPartitionOffsetAndMetadataMap = info.partitionsToOffsetAndMetadata().get();

        Map<TopicPartition, Long> groupOffset = new HashMap<>();
        for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : topicPartitionOffsetAndMetadataMap.entrySet()) {
            TopicPartition key = entry.getKey();
            OffsetAndMetadata metadata = entry.getValue();
            groupOffset.putIfAbsent(new TopicPartition(key.topic(), key.partition()), metadata.offset());
        }
        return groupOffset;
    }

}
