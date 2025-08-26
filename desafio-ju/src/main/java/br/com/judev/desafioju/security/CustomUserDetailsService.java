package br.com.judev.desafioju.security;

import br.com.judev.desafioju.model.Cliente;
import br.com.judev.desafioju.repository.ClienteRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
/**
 * Serviço personalizado para carregar detalhes de autenticação do usuário.
 * <p>
 * Implementa a interface UserDetailsService do Spring Security para que
 * o framework possa buscar um usuário pelo email e verificar credenciais.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ClienteRepository clienteRepository;

    public CustomUserDetailsService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Carrega um usuário pelo seu email.
     *
     * @param email Email do cliente a ser autenticado
     * @return UserDetails do cliente, usado pelo Spring Security
     * @throws UsernameNotFoundException Se o cliente não for encontrado no banco
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return clienteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente não encontrado"));
    }
}
