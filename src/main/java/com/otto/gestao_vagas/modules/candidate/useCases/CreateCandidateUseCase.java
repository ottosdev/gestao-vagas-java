package com.otto.gestao_vagas.modules.candidate.useCases;

import com.otto.gestao_vagas.exceptions.UserFoundException;
import com.otto.gestao_vagas.modules.candidate.entity.CandidateEntity;
import com.otto.gestao_vagas.modules.candidate.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCandidateUseCase {

    private final CandidateRepository candidateRepository;
    private final PasswordEncoder passwordEncoder;

    public CandidateEntity execute(CandidateEntity candidateEntity) {
        candidateRepository.findByUsernameOrEmail(candidateEntity.getUsername(), candidateEntity.getEmail()).ifPresent((user) -> {
            throw new UserFoundException();
        });

        var password = passwordEncoder.encode(candidateEntity.getPassword());
        candidateEntity.setPassword(password);

       return candidateRepository.save(candidateEntity);
    }
}
