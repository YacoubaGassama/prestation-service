package uahb.m1gl.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import uahb.m1gl.config.ConfigData;

import java.io.Serializable;
import java.util.function.BiConsumer;

@Slf4j
@Component
public class MessageHelper<K extends Serializable, V extends SpecificRecordBase> {

    private final KafkaProducer<K, V> kafkaProducer;
    private final ConfigData configData;

    public MessageHelper(KafkaProducer<K, V> kafkaProducer, ConfigData configData) {
        this.kafkaProducer = kafkaProducer;
        this.configData = configData;
    }


    public void send(String topicName, K key, V message){
        kafkaProducer.send(topicName, key, message,  throwable());
    }

    public BiConsumer<SendResult<K, V>, Throwable> throwable() {
        return new BiConsumer<SendResult<K, V>, Throwable>() {
            @Override
            public void accept(SendResult<K, V> kvSendResult, Throwable throwable) {
                if (null != throwable) {
                    // same flow as of onFailure
                    log.info("Error sending message to kafka: {}", throwable.getMessage());
                } else {
                    // onSuccess flow
                    log.info("Data send to kafka: {}, matadata (topic: {}, partition: {}, offset: {})",
                            kvSendResult.getProducerRecord().value(),
                            kvSendResult.getRecordMetadata().topic(),
                            kvSendResult.getRecordMetadata().partition(),
                            kvSendResult.getRecordMetadata().offset());
                }
            }
        };
    }
}

