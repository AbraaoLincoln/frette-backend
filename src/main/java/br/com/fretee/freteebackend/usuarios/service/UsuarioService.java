package br.com.fretee.freteebackend.usuarios.service;

import br.com.fretee.freteebackend.configuration.JwtUtil;
import br.com.fretee.freteebackend.exceptions.NomeUsuarioAlreadyInUseException;
import br.com.fretee.freteebackend.exceptions.UsuarioNotFindException;
import br.com.fretee.freteebackend.usuarios.entity.Permissao;
import br.com.fretee.freteebackend.usuarios.entity.Usuario;
import br.com.fretee.freteebackend.usuarios.enums.Permissoes;
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

import java.util.*;

@Service
@Slf4j
public class UsuarioService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public Usuario addUsuario(Usuario usuario) throws NomeUsuarioAlreadyInUseException {
        //TODO: validar usuario

        boolean nomeUsuarioJaEstaEmUso = usuarioRepository.verificarSeNomeUsuarioJaEstaEmUso(usuario.getNomeUsuario());
        if(nomeUsuarioJaEstaEmUso) {
            log.error("Nome de usuario {} já está em uso", usuario.getNomeUsuario());
            throw new NomeUsuarioAlreadyInUseException();
        }

        usuario.setSenha(bCryptPasswordEncoder.encode(usuario.getSenha()));
        usuario.setPermissoes(new ArrayList<>());
        Permissao permissaoUsuario = new Permissao();
        permissaoUsuario.setId(Permissoes.USUARIO.getValue());
        usuario.getPermissoes().add(permissaoUsuario);
        return usuarioRepository.save(usuario);
    }

    public Usuario findUsuarioById(int usuarioId) throws UsuarioNotFindException {
        var usuario = usuarioRepository.findById(usuarioId);

        if(usuario.isEmpty()) throw new UsuarioNotFindException();

        return usuario.get();
    }

    public Usuario findUsuarioByNomeUsuario(String nomeUsuario) throws UsuarioNotFindException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByNomeUsuario(nomeUsuario);
        if(usuarioOptional.isEmpty()) throw new UsuarioNotFindException();
        return  usuarioOptional.get();
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
