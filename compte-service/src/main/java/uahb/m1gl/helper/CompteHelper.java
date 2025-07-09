package uahb.m1gl.helper;


import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uahb.m1gl.dto.*;
import uahb.m1gl.event.KafkaEvent;
import uahb.m1gl.exception.CompteServiceException;
import uahb.m1gl.kafka.avro.model.CustomerCreateRequestAvroModel;
import uahb.m1gl.mapper.CompteServiceMapper;
import uahb.m1gl.messaging.CustomerKafkaListener;
import uahb.m1gl.messaging.KafkaService;
import uahb.m1gl.model.Compte;
import uahb.m1gl.model.Tracking;
import uahb.m1gl.model.Transaction;
import uahb.m1gl.service.ICompte;
import uahb.m1gl.service.ITracking;

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
    private final ITracking iTracking;
    private final CustomerKafkaListener customerResponseKafkaListener;

    public CompteHelper(KafkaService kafkaService, ICompte iCompte, ModelMapper modelMapper, CompteServiceMapper compteServiceMapper, ITracking iTracking, CustomerKafkaListener customerResponseKafkaListener) {
        this.kafkaService = kafkaService;
        this.iCompte = iCompte;
        this.modelMapper = modelMapper;
        this.compteServiceMapper = compteServiceMapper;
        this.iTracking = iTracking;
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

        Tracking tracking = new Tracking();
        tracking.setTrackingId(UUID.randomUUID().toString());
        tracking.setTel(compteCreateRequest.getCustomerCreateRequest().getTel());
        tracking.setMessage("Création du compte initiée !!!");
        tracking.setStatut("PENDING");
        tracking.setClientId(0);
        tracking.setCompte(null);
        tracking = iTracking.save(tracking);
        customerResponseKafkaListener.setDataSaga(tracking);
        KafkaEvent<CustomerCreateRequestAvroModel> createCustumerEvent = new KafkaEvent<>(customerCreateRequestAvroModel);
        kafkaService.createCustomer(createCustumerEvent);
        String trackerUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/compte/tracker/{id}")
                .buildAndExpand(tracking.getTrackingId())
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


    public TrackingResponse trackingResponse(long trackingId) {
        Tracking tracking = iTracking.findById(trackingId);
        return TrackingResponse.builder()
                .message(tracking.getMessage())
                .statut(tracking.getStatut())
                .clientId(tracking.getClientId())
                .build();
    }
}
