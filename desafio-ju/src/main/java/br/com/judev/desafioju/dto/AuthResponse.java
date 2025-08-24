package br.com.judev.desafioju.dto;

public record AuthResponse(String token, String tokenType, long expiresInMs) {
}
