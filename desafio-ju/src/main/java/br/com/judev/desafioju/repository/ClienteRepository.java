package br.com.judev.desafioju.repository;

import br.com.judev.desafioju.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByEmail(String email);
    boolean existsByEmail(String email);
}
