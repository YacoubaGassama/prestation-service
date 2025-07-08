package uahb.m1gl.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerCreateRequest {
    @NotNull
    @NotBlank(message = "Nom obligatoire !")
    private String nom;
    @NotNull
    @NotBlank(message = "Nom obligatoire !")
    private String prenom;
    @NotNull(message = "Date de naissance obligatoire !")
    private LocalDate dateNaissance;
    @NotNull
    @NotBlank(message = "Tel obligatoire !")
    private String tel;
}
