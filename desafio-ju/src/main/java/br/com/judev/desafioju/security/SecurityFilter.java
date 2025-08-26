package br.com.judev.desafioju.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/**
 * Filtro de segurança responsável por validar tokens JWT em cada requisição.
 * <p>
 * Funciona interceptando todas as requisições (exceto login e registro) e
 * verificando se o header Authorization contém um token válido.
 * Se o token for válido, autentica o usuário no contexto de segurança do Spring.
 */
public class SecurityFilter extends OncePerRequestFilter {

    private final JwtService tokenService;
    private final UserDetailsService userDetailsService;

    public SecurityFilter(JwtService tokenService, UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Filtro de segurança responsável por validar tokens JWT em cada requisição.
     * <p>
     * Funciona interceptando todas as requisições (exceto login e registro) e
     * verificando se o header Authorization contém um token válido.
     * Se o token for válido, autentica o usuário no contexto de segurança do Spring.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().equals("/api/v1/clientes/login") ||
                request.getServletPath().equals("/api/v1/clientes/register")) {
            filterChain.doFilter(request, response);
            return;
        }
        var token = this.recoverToken(request);

        if (token == null) {
            // Token ausente → retorna 401
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token ausente");
            return;
        }

        String login = tokenService.validateToken(token);
        if (login == null) {
            // Token inválido → retorna 401
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
            return;
        }
        UserDetails client = userDetailsService.loadUserByUsername(login);
        var authentication = new UsernamePasswordAuthenticationToken(client, null, client.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);

        filterChain.doFilter(request, response);
    }

    /**
     * Recupera o token JWT do header Authorization.
     * Espera o formato: "Bearer <token>"
     *
     * @param request Objeto HttpServletRequest da requisição.
     * @return O token JWT sem o prefixo "Bearer ", ou null se não existir.
     */
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        return authHeader.substring(7);
    }
}
