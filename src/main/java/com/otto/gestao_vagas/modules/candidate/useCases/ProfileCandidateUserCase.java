package com.otto.gestao_vagas.modules.candidate.useCases;

import com.otto.gestao_vagas.modules.candidate.dto.ProdileCandidateResponseDTO;
import com.otto.gestao_vagas.modules.candidate.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileCandidateUserCase {

    private final CandidateRepository candidateRepository;

    public ProdileCandidateResponseDTO execute(UUID candidateId) {
        var candidate = candidateRepository.findById(candidateId).orElseThrow(() -> {
            throw new UsernameNotFoundException("Usuario não encontrado");
        });

        var dto = ProdileCandidateResponseDTO.builder()
                .id(candidate.getId())
                .name(candidate.getName())
                .username(candidate.getUsername())
                .email(candidate.getEmail())
                .description(candidate.getDescription()).build();

        return dto;
    }
}
