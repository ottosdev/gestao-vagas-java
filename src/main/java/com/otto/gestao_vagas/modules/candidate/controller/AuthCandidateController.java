package com.otto.gestao_vagas.modules.candidate.controller;

import com.otto.gestao_vagas.modules.candidate.dto.AuthCandidateRequestDTO;
import com.otto.gestao_vagas.modules.candidate.useCases.AuthCandidateUserCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/candidate")
@RequiredArgsConstructor
public class AuthCandidateController {
    private final AuthCandidateUserCase authCandidateUserCase;
    @PostMapping("/auth")
    public ResponseEntity<Object> authCandidate(@RequestBody AuthCandidateRequestDTO authCandidateRequestDTO) {
        try {
            var token = authCandidateUserCase.execute(authCandidateRequestDTO);
            return ResponseEntity.ok().body(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
