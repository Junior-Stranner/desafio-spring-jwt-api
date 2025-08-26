package br.com.judev.desafioju.service;

import br.com.judev.desafioju.dto.AuthRequest;
import br.com.judev.desafioju.dto.AuthResponse;
import br.com.judev.desafioju.dto.ClienteRegisterRequest;
import br.com.judev.desafioju.dto.ClienteRegisterResponse;
import br.com.judev.desafioju.model.Cliente;
import br.com.judev.desafioju.repository.ClienteRepository;
import br.com.judev.desafioju.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    Logger logger = LoggerFactory.getLogger(ClienteService.class);

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder encoder;
    private final JwtService  jwtService;

    public ClienteService(ClienteRepository clienteRepository, PasswordEncoder encoder, JwtService jwtService) {
        this.clienteRepository = clienteRepository;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    public ClienteRegisterResponse registrar(ClienteRegisterRequest request){
        if(clienteRepository.existsByEmail(request.email())){
            throw new IllegalArgumentException("E-mail já cadastrado");
        }
        Cliente c = new Cliente();
        c.setNome(request.nome());
        c.setEmail(request.email());
        c.setSenha(encoder.encode(request.senha()));
        Cliente salvo = clienteRepository.save(c);
        return new ClienteRegisterResponse(salvo.getId(), salvo.getNome(), salvo.getEmail(), "Cliente registrado om sucesso");
    }

    public AuthResponse login(AuthRequest request) {
        Cliente cliente = clienteRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("Cliente não encontrado pelo email"));

        if (cliente == null && request.senha().isBlank()) {
            throw new UsernameNotFoundException("Cliente inexistente no Banco de Dados");
        }
        if (!encoder.matches(request.senha(), cliente.getSenha())) {
            throw new BadCredentialsException("Senha Inválida");
        }

        String token = jwtService.generateToken(cliente);
        return new AuthResponse(token, "Login realizado com sucesso!");
    }

    public Cliente findClienteByEmail(String email) {
        return clienteRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Cliente inexistente"));
    }
}
