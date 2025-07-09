package uahb.m1gl.service;

import org.springframework.stereotype.Service;
import uahb.m1gl.model.Tracking;
import uahb.m1gl.repository.TrackingRepository;

@Service
public class TrackingService implements ITracking {
    private final TrackingRepository trackingRepository;

    public TrackingService(TrackingRepository trackingRepository) {
        this.trackingRepository = trackingRepository;
    }

    @Override
    public Tracking findById(long id) {
        return trackingRepository.findById(id).orElse(null);
    }

    @Override
    public Tracking save(Tracking tracking) {
        return trackingRepository.save(tracking);
    }
}
