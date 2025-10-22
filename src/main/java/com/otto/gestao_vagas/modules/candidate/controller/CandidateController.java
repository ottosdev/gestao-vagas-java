package com.otto.gestao_vagas.modules.candidate.controller;

import com.otto.gestao_vagas.exceptions.UserFoundException;
import com.otto.gestao_vagas.modules.candidate.dto.ProdileCandidateResponseDTO;
import com.otto.gestao_vagas.modules.candidate.entity.CandidateEntity;
import com.otto.gestao_vagas.modules.candidate.useCases.ApplyJobCandidateUseCase;
import com.otto.gestao_vagas.modules.candidate.useCases.CreateCandidateUseCase;
import com.otto.gestao_vagas.modules.candidate.useCases.ListAllJobsByFilterUseCase;
import com.otto.gestao_vagas.modules.candidate.useCases.ProfileCandidateUserCase;
import com.otto.gestao_vagas.modules.company.entities.JobEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/candidate")
@RequiredArgsConstructor
@Tag(name = "Candidato", description = "Informacoes do candidato")
public class CandidateController {

	private final CreateCandidateUseCase createCandidateUseCase;
	private final ProfileCandidateUserCase profileCandidateUserCase;
	private final ListAllJobsByFilterUseCase listAllJobsByFilterUseCase;
	private final ApplyJobCandidateUseCase applyJobCandidateUseCase;

	@PostMapping("/")
	public ResponseEntity<Object> create(@Valid @RequestBody CandidateEntity candidateEntity) {
		try {
			var result = createCandidateUseCase.execute(candidateEntity);
			return ResponseEntity.ok().body(result);
		} catch (UserFoundException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/")
	@PreAuthorize("hasRole('CANDIDATE')")
	@Operation(summary = "Perfil do candidato", description = "Perfil do candidato")
	@ApiResponses({@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ProdileCandidateResponseDTO.class))}),
			@ApiResponse(responseCode = "400", description = "Candidato não encontrado")}
	)
	@SecurityRequirement(name = "jwt_auth")
	public ResponseEntity<Object> getCandidate(HttpServletRequest request) {
		var idCandidate = UUID.fromString(request.getAttribute("candidate_id").toString());
		try {
			var result = profileCandidateUserCase.execute(idCandidate);
			return ResponseEntity.ok().body(result);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/job")
	@PreAuthorize("hasRole('CANDIDATE')")
	@Operation(summary = "Listagem de vagas por filtro", description = "Listagem de vagas por filtro")
	@ApiResponses(@ApiResponse(responseCode = "200", content = {@Content(array =
					@ArraySchema(schema = @Schema(implementation = JobEntity.class)))}, description = "Listagem de vagas por filtro"))
	@SecurityRequirement(name = "jwt_auth")
	public ResponseEntity<List<JobEntity>> getJobByFilter(@RequestParam String filter, HttpServletRequest request) {
		return ResponseEntity.ok().body(listAllJobsByFilterUseCase.execute(filter));
	}

	@PostMapping("/job/apply")
	@PreAuthorize("hasRole('CANDIDATE')")
	@Operation(summary = "Inscrição do candidato para uma vaga", description = "Essa função é responsável por realizar a inscrição do candidato em uma vaga.")
	@SecurityRequirement(name = "jwt_auth")
	public ResponseEntity<Object> applyJob(HttpServletRequest request, @RequestBody UUID idJob){

		var idCandidate = request.getAttribute("candidate_id");

		try{
			var result = this.applyJobCandidateUseCase.execute(UUID.fromString(idCandidate.toString()), idJob);
			return ResponseEntity.ok().body(result);
		}catch(Exception e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
