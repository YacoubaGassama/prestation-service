package uahb.m1gl.service;

import org.springframework.stereotype.Service;
import uahb.m1gl.model.Saga;
import uahb.m1gl.repository.SagaRepository;

@Service
public class SagaService implements ISaga{
    private final SagaRepository sagaRepository;

    public SagaService(SagaRepository sagaRepository) {
        this.sagaRepository = sagaRepository;
    }

    @Override
    public Saga findById(long id) {
        return sagaRepository.findById(id).orElse(null);
    }

    @Override
    public Saga save(Saga saga) {
        return sagaRepository.save(saga);
    }
}
