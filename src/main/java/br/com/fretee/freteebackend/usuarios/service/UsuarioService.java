package br.com.fretee.freteebackend.usuarios.service;

import br.com.fretee.freteebackend.exceptions.UsuarioNotFindException;
import br.com.fretee.freteebackend.usuarios.entity.Usuario;
import br.com.fretee.freteebackend.usuarios.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UsuarioService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Usuario saveUsurio(Usuario usuario) {
        //TODO: validar usuario
        usuario.setSenha(bCryptPasswordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public Usuario findUsuarioById(int usuarioId) throws UsuarioNotFindException {
        var usuario = usuarioRepository.findById(usuarioId);

        if(usuario.isEmpty()) throw new UsuarioNotFindException();

        return usuario.get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> user = usuarioRepository.findByNomeUsuario(username);

        if(user.isEmpty()) {
            log.error("Usuario não encontrado");
            throw new UsernameNotFoundException("Usuario não encontrado");
        }

        log.info("Usuario encontrado");
        List<SimpleGrantedAuthority> permissoes = new ArrayList<>();
        user.get().getPermissoes().forEach(p -> {
            permissoes.add(new SimpleGrantedAuthority(p.getNome()));
        });

        return new User(user.get().getNomeUsuario(), user.get().getSenha(), permissoes);
    }
}
