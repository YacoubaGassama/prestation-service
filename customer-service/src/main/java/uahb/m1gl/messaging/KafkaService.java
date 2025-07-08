package uahb.m1gl.messaging;

import org.springframework.stereotype.Component;
import uahb.m1gl.config.ConfigData;
import uahb.m1gl.kafka.avro.model.CustomerCreateRequestAvroModel;
import uahb.m1gl.service.MessageHelper;
@Component
public class KafkaService {
    private final MessageHelper<String, CustomerCreateRequestAvroModel> messageHelper;
    private final ConfigData configData;

    public KafkaService(MessageHelper<String, CustomerCreateRequestAvroModel> messageHelper, ConfigData configData) {
        this.messageHelper = messageHelper;
        this.configData = configData;
    }

}
