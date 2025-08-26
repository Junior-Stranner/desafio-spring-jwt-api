package br.com.judev.desafioju.controller;

import br.com.judev.desafioju.dto.ProdutoRequestDTO;
import br.com.judev.desafioju.dto.ProdutoResponseDTO;
import br.com.judev.desafioju.model.Cliente;
import br.com.judev.desafioju.model.Produto;
import br.com.judev.desafioju.service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Controlador responsável por gerenciar endpoints de produtos.
 * Permite criar, listar, atualizar e deletar produtos de um cliente autenticado.
 */
@RestController
@RequestMapping("/api/v1/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criarProduto(
            @AuthenticationPrincipal Cliente cliente,
            @RequestBody ProdutoRequestDTO request) {

        ProdutoResponseDTO produtoCriado = produtoService.criar(cliente.getId(), request);
        return ResponseEntity.status(201).body(produtoCriado);
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listarProdutos() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Cliente cliente = (Cliente) authentication.getPrincipal();

        List<Produto> produtos = produtoService.listarProdutosDoCliente(cliente.getEmail());
        return ResponseEntity.ok(produtos);
    }



    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(
            @AuthenticationPrincipal Cliente cliente,
            @PathVariable Long id,
            @RequestBody ProdutoRequestDTO request) {

        ProdutoResponseDTO produtoAtualizado = produtoService.atualizar(cliente, id, request);
        return ResponseEntity.ok(produtoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(
            @AuthenticationPrincipal Cliente cliente,
            @PathVariable Long id) {

        produtoService.deletar(cliente, id);
        return ResponseEntity.noContent().build();
    }
}
