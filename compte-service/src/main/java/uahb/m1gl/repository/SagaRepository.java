package uahb.m1gl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uahb.m1gl.model.Saga;

@Repository
public interface SagaRepository extends JpaRepository<Saga, Long> {

}
