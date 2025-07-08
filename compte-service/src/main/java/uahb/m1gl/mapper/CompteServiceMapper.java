package uahb.m1gl.mapper;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import uahb.m1gl.dto.CustomerCreateRequest;
import uahb.m1gl.dto.DepotCreateResponse;
import uahb.m1gl.kafka.avro.model.CustomerCreateRequestAvroModel;
import uahb.m1gl.kafka.avro.model.PaiementCreateRequestAvroModel;
import uahb.m1gl.model.Transaction;

import java.time.LocalDate;

@Component
public class CompteServiceMapper {

    public CustomerCreateRequestAvroModel CustomerCreateRequestTocustomerCreateResponseAvroModel(CustomerCreateRequest customerCreateRequest){
        return  CustomerCreateRequestAvroModel.newBuilder()
                .setTel(customerCreateRequest.getTel())
                .setNom(customerCreateRequest.getNom())
                .setPrenom(customerCreateRequest.getPrenom())
                .setDateNaissance(customerCreateRequest.getDateNaissance())
                .build();
    }
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    public DepotCreateResponse TransactionToDepotResponse(Transaction transaction){
        return DepotCreateResponse.builder()
                .date(transaction.getDate())
                .compteNo(transaction.getCompte().getNumeroCompte())
                .type(transaction.getType())
                .clientId(transaction.getCompte().getClientId())
                .montant(transaction.getMontant())
                .build();
    }

    public Transaction PaiementCreateRequestAvroModelToTransaction(PaiementCreateRequestAvroModel paiementCreateRequestAvroModel){
        return Transaction.builder()
                .date(LocalDate.now())
                .type("PAIEMENT")
                .compte(null)
                .montant(paiementCreateRequestAvroModel.getMontant())
                .demandeId(Long.parseLong(paiementCreateRequestAvroModel.getDemandeId()))
                .build();
    }

}
