package com.otto.gestao_vagas.modules.candidate.entity;

import com.otto.gestao_vagas.modules.company.entities.JobEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(of = {"idCandidate", "idJob"})
@Entity(name = "tb_apply_job")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplyJobEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne()
    @JoinColumn(name = "candidate_id", insertable = false, updatable = false)
    private CandidateEntity candidate;
    @ManyToOne
    @JoinColumn(name = "job_id", insertable = false, updatable = false)
    private JobEntity job;
    @Column(name = "candidate_id", nullable = false)
    private UUID idCandidate;
    @Column(name = "job_id", nullable = false)
    private UUID idJob;
    @CreationTimestamp
    private LocalDateTime createdAt;

}
