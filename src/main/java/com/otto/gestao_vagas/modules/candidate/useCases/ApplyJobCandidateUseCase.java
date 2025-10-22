package com.otto.gestao_vagas.modules.candidate.useCases;

import com.otto.gestao_vagas.exceptions.JobNotFoundException;
import com.otto.gestao_vagas.exceptions.UserNotFoundException;
import com.otto.gestao_vagas.modules.candidate.entity.ApplyJobEntity;
import com.otto.gestao_vagas.modules.candidate.repository.ApplyJobRepository;
import com.otto.gestao_vagas.modules.candidate.repository.CandidateRepository;
import com.otto.gestao_vagas.modules.company.repositories.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplyJobCandidateUseCase {
    private final JobRepository jobRepository;
    private final CandidateRepository candidateRepository;
    private final ApplyJobRepository applyJobRepository;

	public ApplyJobEntity execute(UUID idCandidate, UUID idJob){
		// Validar se o candidato existe
		var candidate = this.candidateRepository.findById(idCandidate);

		if(candidate.isEmpty()){
			throw new UserNotFoundException();
		}

		// Validar se a vaga existe
		var job = this.jobRepository.findById(idJob);

		if(job.isEmpty()) {
			throw new JobNotFoundException("Job Not found");
		}

		// Candidato se inscrever na vaga
		var applyJob = ApplyJobEntity.builder()
				.idCandidate(idCandidate)
				.idJob(idJob).build();

		applyJob = applyJobRepository.save(applyJob);
		return applyJob;
	}
}
