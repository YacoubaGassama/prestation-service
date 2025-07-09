package uahb.m1gl.dto;

import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uahb.m1gl.model.Compte;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrackingResponse {
    private String message;
    @Column(length = 50)
    private String statut;
    private long clientId;

}
