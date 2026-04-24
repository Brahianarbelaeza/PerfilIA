package com.agentperfilia.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CvRequest {
    @NotBlank(message = "El ID del perfil es requerido")
    private String profileId;

    @NotBlank(message = "La oferta laboral es requerida")
    private String jobOffer;
}
