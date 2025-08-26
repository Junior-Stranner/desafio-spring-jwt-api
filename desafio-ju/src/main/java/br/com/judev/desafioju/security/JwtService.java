package br.com.judev.desafioju.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.Instant;
/**
 * Serviço responsável pela geração e validação de tokens JWT.
 * <p>
 * Este serviço utiliza a biblioteca Auth0 para criar e validar tokens
 * baseados no email do usuário como subject.
 */
@Service
public class JwtService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Gera um token JWT para o usuário especificado.
     *
     * @param userDetails Objeto UserDetails do usuário que será autenticado.
     * @return Uma String contendo o token JWT gerado.
     * @throws RuntimeException Se ocorrer algum erro na criação do token.
     */
    public String generateToken(UserDetails userDetails) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(userDetails.getUsername())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token", exception);
        }
    }

    public Instant generateExpirationDate() {
        return Instant.now().plusSeconds(expiration);
    }

    /**
     * Valida um token JWT e retorna o subject (usuário) se for válido.
     *
     * @param token Token JWT a ser validado.
     * @return O email do usuário se o token for válido; null se inválido.
     */
    public String validateToken(String token) {
        try {
            var algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}
