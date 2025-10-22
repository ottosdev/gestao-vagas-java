package com.otto.gestao_vagas.modules.candidate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProdileCandidateResponseDTO {
    @Schema(example = "kilua@123.")
    private String username;

    @Schema(example = "kilua@email.com")
    private String email;

    @Schema(example = "Desenvolvedor Java com 5 anos de experiência em desenvolvimento web e mobile.")
    private String description;

    private UUID id;

    @Schema(example = "Kilua")
    private String name;
}
