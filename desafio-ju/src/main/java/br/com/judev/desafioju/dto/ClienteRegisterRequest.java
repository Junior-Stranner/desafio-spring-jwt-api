package br.com.judev.desafioju.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRegisterRequest(
        @NotBlank String nome,
        @Email String email,
        @Size(min=6) String senha
) {}
