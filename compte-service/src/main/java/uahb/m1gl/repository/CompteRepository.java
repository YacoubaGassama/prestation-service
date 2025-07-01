package uahb.m1gl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uahb.m1gl.model.Compte;

@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {
 Compte findByClientId (Long clientId);
}
