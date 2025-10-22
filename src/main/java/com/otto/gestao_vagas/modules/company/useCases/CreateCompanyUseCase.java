package com.otto.gestao_vagas.modules.company.useCases;

import com.otto.gestao_vagas.modules.company.entities.CompanyEntity;
import com.otto.gestao_vagas.modules.company.repositories.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCompanyUseCase {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    public CompanyEntity execute(CompanyEntity companyEntity) {
        companyRepository.findByUsernameOrEmail(companyEntity.getUsername(), companyEntity.getEmail())
                        .ifPresent((company) -> {
                            throw new RuntimeException("Companhia já existe!");
                        });
        var password = passwordEncoder.encode(companyEntity.getPassword());
        companyEntity.setPassword(password);

        return companyRepository.save(companyEntity);
    }
}
