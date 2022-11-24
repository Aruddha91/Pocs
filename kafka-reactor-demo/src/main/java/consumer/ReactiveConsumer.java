package consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOffset;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ReactiveConsumer {

    private static final Logger log = LoggerFactory.getLogger(ReactiveConsumer.class.getName());

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC = "reactive-test-topic";

    private static DateTimeFormatter dateFormat;

    private static KafkaReceiver<Integer,String> receiver;


    public static Disposable handleReceiveMessage(String bootstrapServers, String topic, CountDownLatch latch){
        createKafkaReceiver(bootstrapServers, topic);
        return consumeMessages(latch);
    }

    private static void createKafkaReceiver(String bootstrapServers, String topic) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "sample-consumer");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "sample-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        ReceiverOptions<Integer, String> receiverOptions = ReceiverOptions.create(props);
        ReceiverOptions<Integer, String> options = receiverOptions.subscription(Collections.singleton(topic))
                .addAssignListener(partitions -> log.debug("onPartitionsAssigned {}", partitions))
                .addRevokeListener(partitions -> log.debug("onPartitionsRevoked {}", partitions));
        dateFormat = DateTimeFormatter.ofPattern("HH:mm:ss:SSS z dd MMM yyyy");
        receiver = KafkaReceiver.create(receiverOptions);
    }

    private static Disposable consumeMessages(CountDownLatch latch) {

        return receiver.receive().subscribe(record -> {
            ReceiverOffset offset = record.receiverOffset();
            Instant timestamp = Instant.ofEpochMilli(record.timestamp());
            log.info("Received message: topic-partition={} offset={} timeStamp : {} key={} value={}\n",
                    offset.topicPartition(),
                    offset.offset(),
                    dateFormat.format(timestamp.atZone(ZoneId.systemDefault())),
                    record.key(),
                    record.value());
            offset.acknowledge();
            latch.countDown();
        });
    }

    public static void main(String[] args) throws Exception {
        int count = 20;
        CountDownLatch latch = new CountDownLatch(count);
        Disposable disposable = ReactiveConsumer.handleReceiveMessage(BOOTSTRAP_SERVERS, TOPIC, latch);

        latch.await(10, TimeUnit.SECONDS);
        disposable.dispose();
    }
}
