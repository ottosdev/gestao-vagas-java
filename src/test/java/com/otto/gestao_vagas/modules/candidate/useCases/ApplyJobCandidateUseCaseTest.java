package com.otto.gestao_vagas.modules.candidate.useCases;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import com.otto.gestao_vagas.exceptions.JobNotFoundException;
import com.otto.gestao_vagas.exceptions.UserNotFoundException;
import com.otto.gestao_vagas.modules.candidate.entity.ApplyJobEntity;
import com.otto.gestao_vagas.modules.candidate.entity.CandidateEntity;
import com.otto.gestao_vagas.modules.candidate.repository.ApplyJobRepository;
import com.otto.gestao_vagas.modules.candidate.repository.CandidateRepository;
import com.otto.gestao_vagas.modules.company.entities.JobEntity;
import com.otto.gestao_vagas.modules.company.repositories.JobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class ApplyJobCandidateUseCaseTest {

	@InjectMocks
	private ApplyJobCandidateUseCase applyJobCandidateUseCase;

	@Mock
	private CandidateRepository candidateRepository;

	@Mock
	private JobRepository jobRepository;

	@Mock
	private ApplyJobRepository applyJobRepository;

	@Test
	@DisplayName("Should not be able to apply for a job if candidate does not found")
	public void should_not_be_apply_jon_with_candidate_not_found() {
		try{
			applyJobCandidateUseCase.execute(null, null);
		} catch (Exception e){
			assertThat(e).isInstanceOf(UserNotFoundException.class);
		}
	}

	@Test
	@DisplayName("Deve lançar UserNotFoundException quando o candidato não existir")
	void should_throw_when_candidate_not_found() {
		UUID idCandidate = UUID.randomUUID();
		UUID idJob = UUID.randomUUID();

		when(candidateRepository.findById(idCandidate)).thenReturn(Optional.empty());

		UserNotFoundException ex = assertThrows(
				UserNotFoundException.class,
				() -> applyJobCandidateUseCase.execute(idCandidate, idJob)
		);
		assertNotNull(ex);
		verify(candidateRepository).findById(idCandidate);
		verifyNoMoreInteractions(candidateRepository);
		verifyNoInteractions(jobRepository, applyJobRepository);
	}

	@Test
	@DisplayName("Deve lançar JobNotFoundException quando a vaga não existir")
	void should_throw_when_job_not_found() {
		UUID idCandidate = UUID.randomUUID();
		UUID idJob = UUID.randomUUID();

		CandidateEntity candidate = new CandidateEntity();
		candidate.setId(idCandidate);

		when(candidateRepository.findById(idCandidate)).thenReturn(Optional.of(candidate));
		when(jobRepository.findById(idJob)).thenReturn(Optional.empty());

		JobNotFoundException ex = assertThrows(
				JobNotFoundException.class,
				() -> applyJobCandidateUseCase.execute(idCandidate, idJob)
		);
		assertNotNull(ex);

		verify(candidateRepository).findById(idCandidate);
		verify(jobRepository).findById(idJob);
		verifyNoInteractions(applyJobRepository);
	}

	@Test
	public void should_be_able_to_create_a_new_apply_job() {
		var idCandidate = UUID.randomUUID();
		var idJob = UUID.randomUUID();

		var applyJob = ApplyJobEntity.builder().idCandidate(idCandidate)
				.idJob(idJob).build();

		var applyJobCreated = ApplyJobEntity.builder().id(UUID.randomUUID()).build();

		when(candidateRepository.findById(idCandidate)).thenReturn(Optional.of(new CandidateEntity()));
		when(jobRepository.findById(idJob)).thenReturn(Optional.of(new JobEntity()));

		when(applyJobRepository.save(applyJob)).thenReturn(applyJobCreated);

		var result = applyJobCandidateUseCase.execute(idCandidate, idJob);

		assertThat(result).hasFieldOrProperty("id");
		assertNotNull(result.getId());

	}

}
