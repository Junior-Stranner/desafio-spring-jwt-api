package br.com.judev.desafioju.repository;

import br.com.judev.desafioju.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoReósitory extends JpaRepository<Produto, Long> {
    List<Produto> findByClienteId(Long clienteId);
    Optional<Produto> findByIdAndClienteId(Long id, Long clienteId);
}
