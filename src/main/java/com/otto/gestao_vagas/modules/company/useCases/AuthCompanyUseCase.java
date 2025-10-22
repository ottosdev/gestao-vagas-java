package com.otto.gestao_vagas.modules.company.useCases;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.otto.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import com.otto.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import com.otto.gestao_vagas.modules.company.repositories.CompanyRepository;
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
public class AuthCompanyUseCase {

    @Value("${security.token.secret}")
    private String SECRET_KEY;

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthCompanyResponseDTO execute(AuthCompanyDTO authCompanyDTO) throws AuthenticationException {
        var company = companyRepository.findByUsername(authCompanyDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario/Senha Incorretas"));

        var passwordMatched = passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());

        if (!passwordMatched) {
            throw new AuthenticationException();
        }

        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        var experesIn = Instant.now().plus(Duration.ofHours(2));

        var token = JWT.create().withIssuer("javagas")
                .withExpiresAt(experesIn)
                .withClaim("roles", Arrays.asList("COMPANY"))
                .withSubject(company.getId().toString()).sign(algorithm);

        var dto = AuthCompanyResponseDTO.builder().accessToken(token).expireIn(experesIn.toEpochMilli()).build();

        return dto;
    }


}
