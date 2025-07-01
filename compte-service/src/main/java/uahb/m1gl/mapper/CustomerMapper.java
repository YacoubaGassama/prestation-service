package uahb.m1gl.mapper;


import org.springframework.stereotype.Component;
import uahb.m1gl.kafka.avro.model.CustomerCreateRequestAvroModel;

@Component
public class CustomerMapper {

    public Customer customerCreateRequestAvromModelToCustomer(CustomerCreateRequestAvroModel customerCreateRequestAvroModel){
        return  Customer.builder()
                .id(0)
                .tel(customerCreateRequestAvroModel.getTel())
                .nom(customerCreateRequestAvroModel.getNom())
                .prenom(customerCreateRequestAvroModel.getPrenom())
                .dateNaissance(customerCreateRequestAvroModel.getDateNaissance())
                .build();
    }
}
