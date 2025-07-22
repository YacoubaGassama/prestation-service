package uahb.m1gl.controller;



import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uahb.m1gl.dto.*;
import uahb.m1gl.helper.CompteHelper;
import uahb.m1gl.messaging.CustomerKafkaListener;

import java.util.Map;

//@CrossOrigin
@RestController
@RequestMapping("/api/compte")
public class CompteController {

    private final CompteHelper compteHelper;

    private final ModelMapper modelMapper;

    public CompteController(CompteHelper compteHelper, ModelMapper modelMapper) {
        this.compteHelper = compteHelper;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createCompte(@RequestBody CompteCreateRequest compteCreateRequest) throws InterruptedException {
        return compteHelper.createCompte(compteCreateRequest);
    }

    @PostMapping("/depot")
    public DepotCreateResponse createDepot(@RequestBody DepotCreateRequest depotCreateRequest) throws InterruptedException {
        return compteHelper.depot(depotCreateRequest);
    }

    @GetMapping("/{clientId}")
    public CompteCreateResponse getCompte(@PathVariable long clientId){
        return compteHelper.getCompteByClientId(clientId);
    }
    @GetMapping("/tracker/{trackingId}")
    public @ResponseBody TrackingResponse trackingResponse(@PathVariable String trackingId){
        return compteHelper.trackingResponse(trackingId);
    }
}
