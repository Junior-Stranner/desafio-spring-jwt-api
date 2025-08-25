package br.com.judev.desafioju.controller;

import br.com.judev.desafioju.dto.AuthRequest;
import br.com.judev.desafioju.dto.AuthResponse;
import br.com.judev.desafioju.dto.ClienteRegisterRequest;
import br.com.judev.desafioju.dto.ClienteRegisterResponse;
import br.com.judev.desafioju.model.Cliente;
import br.com.judev.desafioju.repository.ClienteRepository;
import br.com.judev.desafioju.security.JwtService;
import br.com.judev.desafioju.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping("/register")
    public ResponseEntity<ClienteRegisterResponse> register(@Valid @RequestBody ClienteRegisterRequest request) {
        ClienteRegisterResponse response = clienteService.registrar(request);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = clienteService.login(request);
        return ResponseEntity.ok(response);
    }
}
