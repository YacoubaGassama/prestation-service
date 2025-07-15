package uahb.m1gl.service;

import uahb.m1gl.model.Saga;

public interface ISaga {
    Saga findById(long id);
    Saga save(Saga saga);
}
