package uahb.m1gl.controller;



import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import uahb.m1gl.dto.CompteCreateRequest;
import uahb.m1gl.dto.DepotCreateRequest;
import uahb.m1gl.helper.CompteHelper;
import uahb.m1gl.messaging.CustomerKafkaListener;

//@CrossOrigin
@RestController
@RequestMapping("/api/compte")
public class CompteController {

    private final CompteHelper compteHelper;

    private final CustomerKafkaListener customerResponseKafkaListener;
    private final ModelMapper modelMapper;

    public CompteController(CompteHelper compteHelper, CustomerKafkaListener customerResponseKafkaListener, ModelMapper modelMapper) {
        this.compteHelper = compteHelper;
        this.customerResponseKafkaListener = customerResponseKafkaListener;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public void createCompte(@RequestBody CompteCreateRequest compteCreateRequest) throws InterruptedException {
        customerResponseKafkaListener.initCompteCreateRequest(compteCreateRequest);
        compteHelper.createCompte(compteCreateRequest);
    }

    @PostMapping("/depot")
    public DepotCreateResponse createDepot(@RequestBody DepotCreateRequest depotCreateRequest) throws InterruptedException {
        return compteHelper.depot(depotCreateRequest);
    }

    @GetMapping("/{clientId}")
    public CompteCreateResponse getCompte(@PathVariable long clientId){
        return compteHelper.getCompteByClientId(clientId);
    }
}
