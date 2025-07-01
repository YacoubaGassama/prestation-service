package uahb.m1gl.service;

import uahb.m1gl.model.Compte;

public interface ICompte {
    Compte save (Compte compte);
    Compte findByClientId (Long clientId);

}
