package uahb.m1gl.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import uahb.m1gl.KafkaConsumer;
import uahb.m1gl.config.ConfigData;
import uahb.m1gl.kafka.avro.model.CustomerCreateRequestAvroModel;
import uahb.m1gl.kafka.avro.model.CustomerCreateResponseAvroModel;
import uahb.m1gl.mapper.CustomerMapper;
import uahb.m1gl.service.CustomerImpl;
import uahb.m1gl.service.MessageHelper;

@Component
@Slf4j
public class CustomerRequestKafkaListener implements KafkaConsumer<CustomerCreateRequestAvroModel> {
    private final CustomerImpl customer;
    private final CustomerMapper customerMapper;
    private final MessageHelper<String, CustomerCreateResponseAvroModel> messageHelper;
    private final ConfigData configData;
    public CustomerRequestKafkaListener(CustomerImpl customer, CustomerMapper customerMapper, MessageHelper<String, CustomerCreateResponseAvroModel> messageHelper, ConfigData configData) {
        this.customer = customer;
        this.customerMapper = customerMapper;
        this.messageHelper = messageHelper;
        this.configData = configData;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.customer-group-id}", topics = "${topics.customer-create-topic-request-name}")
    public void receive(@Payload CustomerCreateRequestAvroModel message,
                        @Header(KafkaHeaders.RECEIVED_KEY) String key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset) {
        log.info("Data {}, key {}, partition {}, offset {}", message, key, partition, offset);
        createCustomer(message);
    }
}
