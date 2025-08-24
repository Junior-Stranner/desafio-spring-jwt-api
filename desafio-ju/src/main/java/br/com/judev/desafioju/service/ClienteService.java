package br.com.judev.desafioju.service;

import br.com.judev.desafioju.dto.ClienteRegisterRequest;
import br.com.judev.desafioju.dto.ClienteRegisterResponse;
import br.com.judev.desafioju.model.Cliente;
import br.com.judev.desafioju.repository.ClienteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder encoder;

    public ClienteService(ClienteRepository clienteRepository, PasswordEncoder encoder) {
        this.clienteRepository = clienteRepository;
        this.encoder = encoder;
    }

    public ClienteRegisterResponse registrar(ClienteRegisterRequest request){
        if(clienteRepository.existsByEmail(request.email())){
            throw new IllegalArgumentException("E-mail já cadastrado");
        }
        Cliente c = new Cliente();
        c.setNome(request.nome());
        c.setEmail(request.email());
        c.setSenha(encoder.encode(request.senha()));
        Cliente saved = clienteRepository.save(c);

        return new ClienteRegisterResponse(saved.getId(), saved.getNome(), saved.getEmail(), "Cliente registrado om sucesso");
    }

    public Cliente findClienteByEmail(String email) {
        return clienteRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário inexistente"));
    }
}
