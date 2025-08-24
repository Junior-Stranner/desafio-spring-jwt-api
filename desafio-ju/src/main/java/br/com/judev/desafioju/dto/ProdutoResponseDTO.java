package br.com.judev.desafioju.dto;

import java.math.BigDecimal;

public record ProdutoResponseDTO(Long id, String nome, String descricao, BigDecimal valor) {}

