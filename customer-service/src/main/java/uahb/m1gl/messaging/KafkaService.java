package uahb.m1gl.messaging;

import org.springframework.stereotype.Component;
import uahb.m1gl.config.ConfigData;
import uahb.m1gl.event.KafkaEvent;
import uahb.m1gl.kafka.avro.model.CustomerCreateRequestAvroModel;
import uahb.m1gl.kafka.avro.model.CustomerCreateResponseAvroModel;
import uahb.m1gl.service.MessageHelper;
@Component
public class KafkaService {
    private final MessageHelper<String, CustomerCreateResponseAvroModel> messageHelper;
    private final ConfigData configData;

    public KafkaService(MessageHelper<String, CustomerCreateResponseAvroModel> messageHelper, ConfigData configData) {
        this.messageHelper = messageHelper;
        this.configData = configData;
    }

    public void createCustomerResponse(KafkaEvent<CustomerCreateResponseAvroModel> kafkaEvent){
        messageHelper.send(configData.getCustomerCreateTopicResponseName(),
                kafkaEvent.getEventId().toString(),
                kafkaEvent.getData());
    }
}
