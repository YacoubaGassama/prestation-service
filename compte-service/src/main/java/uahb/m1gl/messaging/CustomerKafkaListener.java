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
import uahb.m1gl.model.Tracking;
import uahb.m1gl.model.Transaction;
import uahb.m1gl.service.CompteService;
import uahb.m1gl.service.ITracking;

import java.time.LocalDate;
import java.util.List;

@Component
public class CustomerKafkaListener implements KafkaConsumer<CustomerCreateResponseAvroModel> {

    private CompteCreateRequest compteCreateRequest;
    private final CompteService compteService;
    private Tracking tracking;
    private final ITracking iTracking;
    //private CustomerCreateResponseAvroModel customerCreateResponseAvroModel;

    public CustomerKafkaListener(CompteService compteService, ITracking iTracking) {
        this.compteService = compteService;
        this.iTracking = iTracking;
    }

    public void initCompteCreateRequest(CompteCreateRequest compteCreateRequest){
        this.compteCreateRequest = compteCreateRequest;
    }
    public void setDataSaga(Tracking tracking){
        this.tracking = tracking;
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

            tracking.setMessage("Compte crée !!!");
            tracking.setStatut("CREATED");
            tracking.setClientId(Long.parseLong(customerCreateResponseAvroModel.getClientId()));
            tracking.setCompte(compte);
            compte.setTracking(tracking);
            compteService.save(compte);
        }else {
            tracking.setMessage("Compte existant avec ce numero !!!");
            tracking.setStatut("FAIL");
            tracking.setClientId(Long.parseLong(customerCreateResponseAvroModel.getClientId()));
            iTracking.save(tracking);
        }
    }
}
