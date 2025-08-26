package br.com.judev.desafioju.service;

import br.com.judev.desafioju.dto.AtualizandoProdutoDTO;
import br.com.judev.desafioju.dto.ProdutoRequestDTO;
import br.com.judev.desafioju.dto.ProdutoResponseDTO;
import br.com.judev.desafioju.model.Cliente;
import br.com.judev.desafioju.model.Produto;
import br.com.judev.desafioju.repository.ClienteRepository;
import br.com.judev.desafioju.repository.ProdutoRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public List<Produto> listarProdutosDoCliente(String email) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        return produtoRepository.findByClienteId(cliente.getId());
    }


    public AtualizandoProdutoDTO atualizar(Cliente cliente, Long produtoId, ProdutoRequestDTO request) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!produto.getCliente().getId().equals(cliente.getId())) {
            throw new RuntimeException("Acesso negado: produto não pertence ao cliente logado");
        }

        produto.setNome(request.nome());
        produto.setDescricao(request.descricao());
        produto.setValor(request.valor());

        Produto atualizado = produtoRepository.save(produto);
        return new AtualizandoProdutoDTO(atualizado.getNome(), atualizado.getDescricao());
    }

    public void deletar(Cliente cliente, Long produtoId) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!produto.getCliente().getId().equals(cliente.getId())) {
            throw new RuntimeException("Acesso negado: produto não pertence ao cliente logado");
        }

        produtoRepository.delete(produto);
    }


}
