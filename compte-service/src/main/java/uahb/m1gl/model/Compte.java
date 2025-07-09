package uahb.m1gl.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private long id;
    private LocalDate dateCreation;
    private BigDecimal solde;
    private  long clientId;
    @Column(length = 30)
    private  String numeroCompte;
    @OneToMany(mappedBy = "comptes" ,cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<Transaction> transactions;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "saga_id")
    private Saga saga;
}
