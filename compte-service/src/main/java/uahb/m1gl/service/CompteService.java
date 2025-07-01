package uahb.m1gl.service;

import org.springframework.stereotype.Service;
import uahb.m1gl.model.Compte;
import uahb.m1gl.repository.CompteRepository;

@Service
public class CompteService implements ICompte{
    private final CompteRepository compteRepository;

    public CompteService(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    @Override
    public Compte save(Compte compte) {
        return compteRepository.save(compte);
    }

    @Override
    public Compte findByClientId(Long clientId) {
        return compteRepository.findByClientId(clientId);
    }
}
