package com.otto.gestao_vagas.modules.candidate.useCases;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.otto.gestao_vagas.modules.candidate.dto.AuthCandidateRequestDTO;
import com.otto.gestao_vagas.modules.candidate.dto.AuthCandidateResponseDTO;
import com.otto.gestao_vagas.modules.candidate.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AuthCandidateUserCase {
    @Value("${security.token.secret.candidate}")
    private String SECRET_KEY;

    private final CandidateRepository candidateRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthCandidateResponseDTO execute(AuthCandidateRequestDTO authCandidateRequestDTO) throws AuthenticationException {
        var candidate = candidateRepository.findByUsername(authCandidateRequestDTO.username()).orElseThrow(() -> {
            throw new UsernameNotFoundException("Usuario/Senha Incorretas");
        });

        var passwordMatched = passwordEncoder.matches(authCandidateRequestDTO.password(), candidate.getPassword());

        if (!passwordMatched) {
            throw new AuthenticationException();
        }

        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        var expiresIn = Instant.now().plus(Duration.ofMinutes(10));
        var token = JWT.create().withIssuer("javagas")
                .withSubject(candidate.getId().toString())
                .withExpiresAt(expiresIn)
                .withClaim("roles", Arrays.asList("CANDIDATE")).sign(algorithm);

        var authResponse = AuthCandidateResponseDTO.builder().accessToken(token).expireIn(expiresIn.toEpochMilli()).build();

        return authResponse;

    }
}
