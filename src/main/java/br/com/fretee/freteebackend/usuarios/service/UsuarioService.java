package br.com.fretee.freteebackend.usuarios.service;

import br.com.fretee.freteebackend.configuration.JwtUtil;
import br.com.fretee.freteebackend.exceptions.NomeUsuarioAlreadyInUseException;
import br.com.fretee.freteebackend.exceptions.UsuarioNotFoundException;
import br.com.fretee.freteebackend.frete.api.FreteApi;
import br.com.fretee.freteebackend.usuarios.dto.UsuarioDTO;
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
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.*;

@Service
@Slf4j
public class UsuarioService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ImagemUsuarioService imagemService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PermissaoService permissaoService;
    @Autowired
    private FreteApi freteApi;

    public Usuario addUsuario(Usuario usuario, MultipartFile foto) throws NomeUsuarioAlreadyInUseException {
        //TODO: validar usuario

        boolean nomeUsuarioJaEstaEmUso = usuarioRepository.verificarSeNomeUsuarioJaEstaEmUso(usuario.getNomeUsuario());
        if(nomeUsuarioJaEstaEmUso) {
            log.error("Nome de usuario {} já está em uso", usuario.getNomeUsuario());
            throw new NomeUsuarioAlreadyInUseException();
        }

        if(foto != null) usuario.setFoto(imagemService.saveImage(foto));
        usuario.setSenha(bCryptPasswordEncoder.encode(usuario.getSenha()));
        usuario.setPermissoes(new ArrayList<>());
        permissaoService.addPermissaoDeUsuario(usuario);

        return usuarioRepository.save(usuario);
    }

    public Usuario addUsuarioTeste(Usuario usuario) throws NomeUsuarioAlreadyInUseException {
        //TODO: validar usuario

        boolean nomeUsuarioJaEstaEmUso = usuarioRepository.verificarSeNomeUsuarioJaEstaEmUso(usuario.getNomeUsuario());
        if(nomeUsuarioJaEstaEmUso) {
            log.error("Nome de usuario {} já está em uso", usuario.getNomeUsuario());
            throw new NomeUsuarioAlreadyInUseException();
        }

        usuario.setSenha(bCryptPasswordEncoder.encode(usuario.getSenha()));
        usuario.setPermissoes(new ArrayList<>());
        permissaoService.addPermissaoDeUsuario(usuario);

        return usuarioRepository.save(usuario);
    }

    public Usuario findUsuarioById(int usuarioId) throws UsuarioNotFoundException {
        var usuario = usuarioRepository.findById(usuarioId);

        if(usuario.isEmpty()) throw new UsuarioNotFoundException();

        return usuario.get();
    }

    public Usuario findUsuarioByNomeUsuario(String nomeUsuario) throws UsuarioNotFoundException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByNomeUsuario(nomeUsuario);
        if(usuarioOptional.isEmpty()) throw new UsuarioNotFoundException();
        return  usuarioOptional.get();
    }

    public UsuarioDTO getUsuarioInfo(Principal principal) throws UsuarioNotFoundException {
        Usuario usuario = findUsuarioByNomeUsuario(principal.getName());
        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
        usuarioDTO.setFretesRealizados(freteApi.getNumeroDeFretesRealizados(usuario.getId()));
        return usuarioDTO;
    }


    //============================= Metodo usado pelo spring security ==============================
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
