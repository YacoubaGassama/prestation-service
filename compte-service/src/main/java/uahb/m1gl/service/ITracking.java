package uahb.m1gl.service;

import uahb.m1gl.model.Tracking;

public interface ITracking {
    Tracking findById(long id);
    Tracking save(Tracking tracking);
}
