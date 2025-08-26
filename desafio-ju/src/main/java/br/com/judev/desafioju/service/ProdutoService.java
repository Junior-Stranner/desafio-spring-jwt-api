package br.com.judev.desafioju.service;

import br.com.judev.desafioju.dto.ProdutoRequestDTO;
import br.com.judev.desafioju.dto.ProdutoResponseDTO;
import br.com.judev.desafioju.model.Cliente;
import br.com.judev.desafioju.model.Produto;
import br.com.judev.desafioju.repository.ClienteRepository;
import br.com.judev.desafioju.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Serviço que encapsula a lógica de negócio dos produtos.
 * Cuida de persistência, validações e regras específicas de cliente/produto.
 */
@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;

    public ProdutoService(ProdutoRepository produtoRepository, ClienteRepository clienteRepository) {
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
    }

    public ProdutoResponseDTO criar(Long clienteId, ProdutoRequestDTO request) {
        Cliente dono = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        Produto p = new Produto();
        p.setNome(request.nome());
        p.setDescricao(request.descricao());
        p.setValor(request.valor());
        p.setCliente(dono);

        Produto produtoSalvo = produtoRepository.save(p);

        return new ProdutoResponseDTO(produtoSalvo.getId(), produtoSalvo.getNome(), produtoSalvo.getDescricao(), produtoSalvo.getValor());
    }

    public List<Produto> listar(Cliente dono) {
        return produtoRepository.findByClienteId(dono.getId());
    }

    public ProdutoResponseDTO atualizar(Cliente dono, Long id, ProdutoRequestDTO request) {
        Produto p = produtoRepository.findByIdAndClienteId(id, dono.getId())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        p.setNome(request.nome());
        p.setDescricao(request.descricao());
        p.setValor(request.valor());
        Produto produtoAtualizado = produtoRepository.save(p);

        return new ProdutoResponseDTO(produtoAtualizado.getId(), p.getNome(), p.getDescricao(), p.getValor());
    }

    public void deletar(Cliente dono, Long id) {
        Produto p = produtoRepository.findByIdAndClienteId(id, dono.getId())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        produtoRepository.delete(p);
    }

}
