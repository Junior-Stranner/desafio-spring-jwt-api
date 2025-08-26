package br.com.judev.desafioju.controller;

import br.com.judev.desafioju.dto.AtualizandoProdutoDTO;
import br.com.judev.desafioju.dto.ProdutoRequestDTO;
import br.com.judev.desafioju.dto.ProdutoResponseDTO;
import br.com.judev.desafioju.model.Cliente;
import br.com.judev.desafioju.model.Produto;
import br.com.judev.desafioju.service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<AtualizandoProdutoDTO> atualizarProduto(
            @AuthenticationPrincipal Cliente cliente,
            @PathVariable Long id,
            @RequestBody ProdutoRequestDTO request) {

        AtualizandoProdutoDTO produtoAtualizado = produtoService.atualizar(cliente, id, request);
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

/**
 * Observações sobre autenticação neste controller:
 *
 * 1. @AuthenticationPrincipal
 *    - Permite injetar diretamente o usuário autenticado (Cliente) nos métodos do controller.
 *    - Facilita associar ações ao cliente correto, garantindo que ele só acesse ou modifique seus próprios produtos.
 *    - É a forma mais "high-level" e segura fornecida pelo Spring Security.
 *
 * 2. SecurityContextHolder + UserDetails
 *    - Forma mais "low-level" de obter o usuário autenticado.
 *    - SecurityContextHolder mantém o contexto de segurança da requisição atual.
 *    - UserDetails representa o usuário autenticado, fornecendo informações como nome, senha e roles.
 *    - Útil quando não se deseja usar @AuthenticationPrincipal ou em casos mais complexos de lógica de autenticação.
 *
 * Resumindo:
 * - Ambos os métodos garantem que o backend sabe quem está autenticado.
 * - Evitam que o cliente informe manualmente IDs de outros usuários, protegendo os endpoints contra acessos indevidos.
 */

