package com.otto.gestao_vagas.modules.company.useCases;

import com.otto.gestao_vagas.exceptions.CompanyNotFoundException;
import com.otto.gestao_vagas.modules.company.dto.CreateJobDTO;
import com.otto.gestao_vagas.modules.company.entities.JobEntity;
import com.otto.gestao_vagas.modules.company.repositories.CompanyRepository;
import com.otto.gestao_vagas.modules.company.repositories.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateJobUseCase {

    private final JobRepository jobRepository;
	private final CompanyRepository companyRepository;

    public JobEntity execute(CreateJobDTO dto, UUID companyId){
		var company = companyRepository.findById(companyId).orElseThrow(() ->  {
			throw new CompanyNotFoundException();
		});
       var jobEntity = JobEntity.builder()
                .benefits(dto.getBenefits())
                .description(dto.getDescription())
                .level(dto.getLevel())
                .companyId(UUID.fromString(companyId.toString())).build();
        return jobRepository.save(jobEntity);
    }
}
