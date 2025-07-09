package uahb.m1gl.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tracking {
    @Id
    @Column(length = 50)
    private String trackingId;
    @Column(length = 50)
    private String message;
    @Column(length = 50)
    private String statut;
    @Column(length = 20)
    private String tel;
    private long clientId;
    @OneToOne(mappedBy = "tracking")
    private Compte compte;
}
