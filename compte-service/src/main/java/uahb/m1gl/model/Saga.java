package uahb.m1gl.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Saga {
    @Id
    @Column(length = 50)
    private String sagaId;
    @Column(length = 50)
    private String message;
    @Column(length = 50)
    private String statut;
    @Column(length = 20)
    private String tel;
    private long clientId;
    @OneToOne(mappedBy = "saga")
    private Compte compte;
}
