package uahb.m1gl.messaging;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uahb.m1gl.KafkaConsumer;
import uahb.m1gl.dto.CompteCreateRequest;
import uahb.m1gl.kafka.avro.model.CustomerCreateResponseAvroModel;
import uahb.m1gl.kafka.avro.model.CustomerStatut;
import uahb.m1gl.model.Compte;
import uahb.m1gl.model.Saga;
import uahb.m1gl.model.Transaction;
import uahb.m1gl.service.CompteService;
import uahb.m1gl.service.ISaga;

import java.time.LocalDate;
import java.util.List;

@Component
public class CustomerKafkaListener implements KafkaConsumer<CustomerCreateResponseAvroModel> {

    private CompteCreateRequest compteCreateRequest;
    private final CompteService compteService;
    private Saga saga;
    private final ISaga iSaga;
    //private CustomerCreateResponseAvroModel customerCreateResponseAvroModel;

    public CustomerKafkaListener(CompteService compteService, ISaga iSaga) {
        this.compteService = compteService;
        this.iSaga = iSaga;
    }

    public void initCompteCreateRequest(CompteCreateRequest compteCreateRequest){
        this.compteCreateRequest = compteCreateRequest;
    }
    public void setDataSaga(Saga saga){
        this.saga = saga;
    }

    /*
    public CustomerCreateResponseAvroModel getStatut(){
        return customerCreateResponseAvroModel;
    }
    */
    @Override
    @KafkaListener(id = "${kafka-consumer-config.customer-group-id}", topics = "${topics.customer-create-topic-response-name}")
    public void receive(@Payload CustomerCreateResponseAvroModel message,
                        @Header(KafkaHeaders.RECEIVED_KEY) String key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset) {
        getData(message);
    }

    @Transactional
    public void getData(CustomerCreateResponseAvroModel customerCreateResponseAvroModel){
        if(customerCreateResponseAvroModel.getCustomerStatut().equals(CustomerStatut.CREATED)){
            // créate compte
            Compte compte = new Compte();
            compte.setClientId(Long.parseLong(customerCreateResponseAvroModel.getClientId()));
            compte.setNumeroCompte("C-"+String.format("%08d", Integer.parseInt(customerCreateResponseAvroModel.getClientId())));
            compte.setSolde(compteCreateRequest.getMontant());
            Transaction transaction = new Transaction();
            transaction.setCompte(compte);
            transaction.setDate(LocalDate.now());
            transaction.setType("DEPOT");
            transaction.setMontant(compteCreateRequest.getMontant());
            transaction.setDemandeId(0);
            compte.setTransactions(List.of(transaction));

            saga.setMessage("Compte crée !!!");
            saga.setStatut("CREATED");
            saga.setClientId(Long.parseLong(customerCreateResponseAvroModel.getClientId()));
            saga.setCompte(compte);
            compte.setSaga(saga);
            compteService.save(compte);
        }else {
            saga.setMessage("Compte existant avec ce numero !!!");
            saga.setStatut("FAIL");
            saga.setClientId(Long.parseLong(customerCreateResponseAvroModel.getClientId()));
            iSaga.save(saga);
        }
    }
}
