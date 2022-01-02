package br.com.fretee.freteebackend.usuarios.service;

import br.com.fretee.freteebackend.configuration.JwtUtil;
import br.com.fretee.freteebackend.exceptions.UsuarioNotFindException;
import br.com.fretee.freteebackend.usuarios.entity.Permissao;
import br.com.fretee.freteebackend.usuarios.entity.Usuario;
import br.com.fretee.freteebackend.usuarios.repository.UsuarioRepository;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Service
@Slf4j
public class UsuarioService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

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
