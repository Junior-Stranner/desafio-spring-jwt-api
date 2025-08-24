package br.com.judev.desafioju.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.processing.Pattern;

import java.util.List;

@Table(name = "clientes")
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;
    @Email(message = "E-mail inválido")
 //   @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Formato de e-mail inválido")
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    private String senhaHash;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Produto> produtos;


}

