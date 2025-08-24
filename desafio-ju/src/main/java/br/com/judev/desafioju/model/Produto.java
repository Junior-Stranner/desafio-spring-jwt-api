package br.com.judev.desafioju.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;
    @NotBlank
    private String descricao;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal valor;

    @ManyToOne(optional = false)
    private Cliente cliente;

}
