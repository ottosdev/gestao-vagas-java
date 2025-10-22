package com.otto.gestao_vagas.modules.candidate.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_candidate")
public class CandidateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "O campo [username] deve conter apenas letras, números e os caracteres especiais")
    private String username;
    @Length(min = 10, max = 100, message = "O campo [password] deve conter entre 10 e 100 caracteres")
    private String password;
    @Email(message = "O campo [email] deve conter um e-mail válido")
    private String email;
    private String description;
    private String curriculum;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

}
