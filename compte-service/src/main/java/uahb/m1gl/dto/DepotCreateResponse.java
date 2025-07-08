package uahb.m1gl.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepotCreateResponse {
    private long clientId;
    private LocalDate date;
    private BigDecimal montant;
    private String type;
    private String compteNo;
}
