package producer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ReactiveProducer {
    private static final Logger log = LoggerFactory.getLogger(ReactiveProducer.class.getName());

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC = "reactive-test-topic";

    private static KafkaSender<Integer, String> sender;
    private static DateTimeFormatter dateFormat;

    public static void handlePublishRequest(String bootstrapServers, String topic, int count, CountDownLatch latch) throws InterruptedException {

        createKafkaSender(bootstrapServers);
        sendMessages(topic, count, latch);
        latch.await(10, TimeUnit.SECONDS);
        close();
    }

    private static void createKafkaSender(String bootstrapServers) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "sample-producer");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        SenderOptions<Integer, String> senderOptions = SenderOptions.create(props);

        sender = KafkaSender.create(senderOptions);
        dateFormat = DateTimeFormatter.ofPattern("HH:mm:ss:SSS z dd MMM yyyy");
    }


    private static void sendMessages(String topic, int count, CountDownLatch latch) throws InterruptedException {
        log.info("going to publish messages to kafka");
        sender.send(Flux.range(1, count)
                        .map(i -> SenderRecord.create(new ProducerRecord<>(topic, i, "Message_" + i), i)))
                .doOnError(e -> log.error("Send failed", e))
                .subscribe(r -> {
                    RecordMetadata metadata = r.recordMetadata();
                    Instant timestamp = Instant.ofEpochMilli(metadata.timestamp());
                    log.info("Message {} sent successfully, topic-partition={}-{} offset={}, timestamp : {}\n",
                            r.correlationMetadata(),
                            metadata.topic(),
                            metadata.partition(),
                            metadata.offset(),
                            dateFormat.format(timestamp.atZone(ZoneId.systemDefault())));
                    latch.countDown();
                });
    }



    private static void close() {
        sender.close();
    }

    public static void main(String[] args) throws Exception {
        int count = 20;
        CountDownLatch latch = new CountDownLatch(count);
        log.info("create reactiveproducer :");
        ReactiveProducer.handlePublishRequest(BOOTSTRAP_SERVERS, TOPIC, count, latch);


    }
}
