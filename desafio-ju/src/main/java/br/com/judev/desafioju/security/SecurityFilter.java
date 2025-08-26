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
 */
public class SecurityFilter extends OncePerRequestFilter {

    private final JwtService tokenService;
    private final UserDetailsService userDetailsService;

    public SecurityFilter(JwtService tokenService, UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        String method = request.getMethod();

        // Log para debug
        System.out.println("=== SECURITY FILTER DEBUG ===");
        System.out.println("Path: " + path);
        System.out.println("Method: " + method);

        if (path.equals("/api/v1/clientes/login") || path.equals("/api/v1/clientes/register")) {
            System.out.println("Endpoint público - passando sem verificação");
            filterChain.doFilter(request, response);
            return;
        }

        String token = this.recoverToken(request);
        System.out.println("Token recuperado: " + (token != null ? "SIM" : "NÃO"));

        if (token == null) {
            System.out.println("Token ausente - retornando 401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Token ausente\",\"status\":401}");
            return;
        }

        try {
            String login = tokenService.validateToken(token);
            System.out.println("Token validado - login: " + login);

            if (login == null || login.isEmpty()) {
                System.out.println("Token inválido - retornando 401");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Token inválido\",\"status\":401}");
                return;
            }

            UserDetails client = userDetailsService.loadUserByUsername(login);
            System.out.println("Usuário encontrado: " + client.getUsername());

            var authentication = new UsernamePasswordAuthenticationToken(client, null, client.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Autenticação configurada com sucesso");

        } catch (Exception e) {
            System.out.println("Erro na validação: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Erro de autenticação: " + e.getMessage() + "\",\"status\":401}");
            return;
        }

        System.out.println("Continuando para próximo filtro...");
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authHeader);

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }
}