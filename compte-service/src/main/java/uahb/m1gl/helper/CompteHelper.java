package uahb.m1gl.helper;


import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uahb.m1gl.dto.CompteCreateRequest;
import uahb.m1gl.dto.CompteCreateResponse;
import uahb.m1gl.dto.DepotCreateRequest;
import uahb.m1gl.dto.DepotCreateResponse;
import uahb.m1gl.event.KafkaEvent;
import uahb.m1gl.exception.CompteServiceException;
import uahb.m1gl.kafka.avro.model.CustomerCreateRequestAvroModel;
import uahb.m1gl.mapper.CompteServiceMapper;
import uahb.m1gl.messaging.CustomerKafkaListener;
import uahb.m1gl.messaging.KafkaService;
import uahb.m1gl.model.Compte;
import uahb.m1gl.model.Saga;
import uahb.m1gl.model.Transaction;
import uahb.m1gl.service.ICompte;
import uahb.m1gl.service.ISaga;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Component
public class CompteHelper {
    private final KafkaService kafkaService;
    private final ICompte iCompte;
    private final ModelMapper modelMapper;
    private final CompteServiceMapper compteServiceMapper;
    private final ISaga iSaga;
    private final CustomerKafkaListener customerResponseKafkaListener;

    public CompteHelper(KafkaService kafkaService, ICompte iCompte, ModelMapper modelMapper, CompteServiceMapper compteServiceMapper, ISaga iSaga, CustomerKafkaListener customerResponseKafkaListener) {
        this.kafkaService = kafkaService;
        this.iCompte = iCompte;
        this.modelMapper = modelMapper;
        this.compteServiceMapper = compteServiceMapper;
        this.iSaga = iSaga;
        this.customerResponseKafkaListener = customerResponseKafkaListener;
    }

    public ResponseEntity<Map<String, String>> createCompte(CompteCreateRequest compteCreateRequest){
        customerResponseKafkaListener.initCompteCreateRequest(compteCreateRequest);

        if(compteCreateRequest.getMontant().compareTo(BigDecimal.valueOf(10000)) < 0){
            throw new CompteServiceException("montant initial ["+compteCreateRequest.getMontant()+"] doit être >= 10000");
        }
        CustomerCreateRequestAvroModel customerCreateRequestAvroModel =
                compteServiceMapper.CustomerCreateRequestTocustomerCreateResponseAvroModel(compteCreateRequest
                        .getCustomerCreateRequest());

        Saga saga = new Saga();
        saga.setSagaId(UUID.randomUUID().toString());
        saga.setTel(compteCreateRequest.getCustomerCreateRequest().getTel());
        saga.setMessage("Création du compte initiée !!!");
        saga.setStatut("PENDING");
        saga.setClientId(0);
        saga.setCompte(null);
        saga = iSaga.save(saga);
        customerResponseKafkaListener.setDataSaga(saga);
        KafkaEvent<CustomerCreateRequestAvroModel> createCustumerEvent = new KafkaEvent<>(customerCreateRequestAvroModel);
        kafkaService.createCustomer(createCustumerEvent);
        String trackerUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/compte/tracker/{id}")
                .buildAndExpand(saga.getSagaId())
                .toUriString();

        // Réponse : code 201 + header Location + JSON avec l’URL
        URI location = URI.create(trackerUrl);
        Map<String, String> body = Map.of("trackerUrl", trackerUrl);

        return ResponseEntity.created(location).body(body);
    }

    public CompteCreateResponse getCompteByClientId(long clientId){
        Compte compte = iCompte.findByClientId(clientId);
        if(compte == null){
            throw new CompteServiceException("Compte avec le numéro client "+clientId+" introuvable !");
        }
        return modelMapper.map(compte, CompteCreateResponse.class);
    }

    public DepotCreateResponse depot(DepotCreateRequest depotCreateRequest){
        BigDecimal montant = BigDecimal.ONE;
        try {
            montant = BigDecimal.valueOf(Long.parseLong(depotCreateRequest.getMontant()));
        } catch (NumberFormatException e) {
            throw new CompteServiceException("Montant dépot invalide !");
        }
        if(montant.compareTo(BigDecimal.valueOf(5000)) < 0){
            throw new CompteServiceException("montant dépot ["+montant+"] doit être >= 5000");
        }
        Compte compte = iCompte.findByClientId(Long.parseLong(depotCreateRequest.getClientId()));
        if(compte == null){
            throw new CompteServiceException("Compte client ["+depotCreateRequest.getClientId()+"] introuvable!");
        }
        Transaction transaction = new Transaction();
        transaction.setCompte(compte);
        transaction.setDate(LocalDate.now());
        transaction.setType("DEPOT");
        transaction.setMontant(montant);
        transaction.setDemandeId(0);
        compte.getTransactions().add(transaction);
        compte.setSolde(compte.getSolde().add(montant));
        transaction.setCompte(compte);
        iCompte.save(compte);
        return compteServiceMapper.TransactionToDepotResponse(transaction);
    }


}
