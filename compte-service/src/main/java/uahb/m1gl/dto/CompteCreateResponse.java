package uahb.m1gl.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompteCreateResponse {
    private String clientId;
    private BigDecimal solde;
    private String numeroCompte;
}
