package uahb.m1gl.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import uahb.m1gl.KafkaConsumer;
import uahb.m1gl.config.ConfigData;
import uahb.m1gl.event.KafkaEvent;
import uahb.m1gl.kafka.avro.model.CustomerCreateRequestAvroModel;
import uahb.m1gl.kafka.avro.model.CustomerCreateResponseAvroModel;
import uahb.m1gl.kafka.avro.model.CustomerStatut;
import uahb.m1gl.mapper.CustomerMapper;
import uahb.m1gl.model.Customer;
import uahb.m1gl.service.CustomerImpl;
import uahb.m1gl.service.ICustomer;
import uahb.m1gl.service.MessageHelper;

import java.util.List;

@Component
@Slf4j
public class CustomerRequestKafkaListener /*implements KafkaConsumer<CustomerCreateRequestAvroModel*/ {
    private final ICustomer iCustomer;
    private final CustomerMapper customerMapper;
    private final MessageHelper<String, CustomerCreateResponseAvroModel> messageHelper;
    private final ConfigData configData;


    public CustomerRequestKafkaListener( ICustomer iCustomer, CustomerMapper customerMapper, MessageHelper<String, CustomerCreateResponseAvroModel> messageHelper, ConfigData configData) {
        this.iCustomer = iCustomer;
        this.customerMapper = customerMapper;
        this.messageHelper = messageHelper;
        this.configData = configData;
    }

    //@Override
    @KafkaListener(id = "${kafka-consumer-config.customer-group-id}", topics = "${topics.customer-create-topic-request-name}")
    public void receive(@Payload List<CustomerCreateRequestAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) String key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset) {

        log.info("Data {}, key {}, partition {}, offset {}", messages, key, partition, offset);
        for (CustomerCreateRequestAvroModel message : messages) {
            createCustomer(message);
        }
    }

    private void createCustomer(CustomerCreateRequestAvroModel customerCreateRequestAvroModel) {
        Customer customer = iCustomer.findByTel(customerCreateRequestAvroModel.getTel());
        var customerCreateResponseAvroModel = new CustomerCreateResponseAvroModel();

        if(customer == null){
             var customerToSave = customerMapper.customerCreateRequestAvromModelToCustomer(customerCreateRequestAvroModel);
             iCustomer.save(customerToSave);
             customerCreateResponseAvroModel.setClientId(customerToSave.getId()+"");
             customerCreateResponseAvroModel.setCustomerStatut(CustomerStatut.CREATED);
             customerCreateResponseAvroModel.setMessage("Client créé !!!");
        }else {
            customerCreateResponseAvroModel.setClientId(customer.getId()+"");
            customerCreateResponseAvroModel.setCustomerStatut(CustomerStatut.EXIST);
            customerCreateResponseAvroModel.setMessage("Un client avec ce numéro de téléphone existe déjà  !!!");
        }
        KafkaEvent<CustomerCreateResponseAvroModel> kafkaEvent = new KafkaEvent<>(customerCreateResponseAvroModel);
        messageHelper.send(configData.getCustomerCreateTopicResponseName(),
                kafkaEvent.getEventId().toString(),
                kafkaEvent.getData());
    }

}
