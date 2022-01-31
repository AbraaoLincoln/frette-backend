package br.com.fretee.freteebackend.usuarios.service;

import br.com.fretee.freteebackend.configuration.JwtUtil;
import br.com.fretee.freteebackend.usuarios.exceptions.NomeUsuarioAlreadyInUseException;
import br.com.fretee.freteebackend.usuarios.exceptions.PrestadorServicoNotFoundException;
import br.com.fretee.freteebackend.usuarios.exceptions.UsuarioNotFoundException;
import br.com.fretee.freteebackend.frete.api.FreteApi;
import br.com.fretee.freteebackend.usuarios.dto.InfoPrestadorServico;
import br.com.fretee.freteebackend.usuarios.dto.UsuarioDTO;
import br.com.fretee.freteebackend.usuarios.entity.Localizacao;
import br.com.fretee.freteebackend.usuarios.entity.PrestadorServico;
import br.com.fretee.freteebackend.usuarios.entity.Usuario;
import br.com.fretee.freteebackend.usuarios.helpers.DistanceCalculator;
import br.com.fretee.freteebackend.usuarios.repository.LocalizacaoRepository;
import br.com.fretee.freteebackend.usuarios.repository.PrestadorServicoRepository;
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
    @Autowired
    private DistanceCalculator distanceCalculator;
    @Autowired
    private PrestadorServicoRepository prestadorServicoRepository;
    @Autowired
    private LocalizacaoRepository localizacaoRepository;

    public Usuario addUsuario(Usuario usuario, MultipartFile foto) throws NomeUsuarioAlreadyInUseException {
        //TODO: validar usuario

        boolean nomeUsuarioJaEstaEmUso = usuarioRepository.verificarSeNomeUsuarioJaEstaEmUso(usuario.getNomeUsuario());
        if(nomeUsuarioJaEstaEmUso) {
            throw new NomeUsuarioAlreadyInUseException("Nome de usuario " + usuario.getNomeUsuario() + " já está em uso");
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
            throw new NomeUsuarioAlreadyInUseException("Nome de usuario " + usuario.getNomeUsuario() + " já está em uso");
        }

        usuario.setSenha(bCryptPasswordEncoder.encode(usuario.getSenha()));
        usuario.setPermissoes(new ArrayList<>());
        permissaoService.addPermissaoDeUsuario(usuario);

        return usuarioRepository.save(usuario);
    }

    public Usuario findUsuarioById(int usuarioId) throws UsuarioNotFoundException {
        var usuario = usuarioRepository.findById(usuarioId);

        if(usuario.isEmpty()) throw new UsuarioNotFoundException("O usuario de id = " + usuarioId + "nao foi encontrado");

        return usuario.get();
    }

    public Usuario findUsuarioByNomeUsuario(String nomeUsuario) throws UsuarioNotFoundException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByNomeUsuario(nomeUsuario);
        if(usuarioOptional.isEmpty()) throw new UsuarioNotFoundException("O usuario " + nomeUsuario + "nao foi encontrado");
        return  usuarioOptional.get();
    }

    public UsuarioDTO getUsuarioInfo(Principal principal) throws UsuarioNotFoundException {
        Usuario usuario = findUsuarioByNomeUsuario(principal.getName());
        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
        usuarioDTO.setFretesRealizados(freteApi.getNumeroDeFretesRealizados(usuario.getId()));
        return usuarioDTO;
    }

    public UsuarioDTO getUsuarioInfo(Principal principal, String nomeUsuarioParaEncontrarInfo) throws UsuarioNotFoundException {
        Usuario usuarioLogado = findUsuarioByNomeUsuario(principal.getName());
        Usuario usuario = findUsuarioByNomeUsuario(nomeUsuarioParaEncontrarInfo);
        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);

        Localizacao localizacaoUsuarioEncontrado = usuario.getLocalizacao();
        Localizacao localizacaoUsuarioLogado = usuario.getLocalizacao();
        if(localizacaoUsuarioEncontrado != null && localizacaoUsuarioLogado != null) {
            double distancia = distanceCalculator.calculateDistanceInKilometer(localizacaoUsuarioEncontrado.getLatitude(), localizacaoUsuarioEncontrado.getLongitude(), localizacaoUsuarioLogado.getLatitude(), localizacaoUsuarioLogado.getLongitude());
            usuarioDTO.setDistancia(distanceCalculator.formatarDouble(distancia, 1));
        }else {
            usuarioDTO.setDistancia(0);
        }

        Optional<PrestadorServico> ps = prestadorServicoRepository.findByUsuarioId(usuario.getId());
        if(ps.isPresent()) {
            InfoPrestadorServico infops = new InfoPrestadorServico(ps.get());
            usuarioDTO.setPrestadorServico(infops);
        }
        return usuarioDTO;
    }

    public void atualizarFirebaseToken(String nomeUsuario, String token) throws UsuarioNotFoundException {
        Usuario usuario = findUsuarioByNomeUsuario(nomeUsuario);
        usuario.setFirebaseToken(token);
        usuarioRepository.save(usuario);
    }

    public Integer findIdUsuarioByNomeUsuario(String nomeUsuario) throws UsuarioNotFoundException {
        Integer id = usuarioRepository.findIdUsuarioByNomeUsuario(nomeUsuario);
        if(id == null) throw new UsuarioNotFoundException("O usuario " + nomeUsuario + "nao foi encontrado");
        return id;
    }

    public String findNomeUsuarioByUsuarioId(int id) throws UsuarioNotFoundException {
        String nomeUsuario = usuarioRepository.findNomeUsuarioByUsuarioId(id);
        if(nomeUsuario == null) throw new UsuarioNotFoundException("O usuario de id = " + id + "nao foi encontrado");
        return nomeUsuario;
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void atualizarLocalizacao(Principal principal, Localizacao localizacao) throws UsuarioNotFoundException, PrestadorServicoNotFoundException {
        Usuario usuario = findUsuarioByNomeUsuario(principal.getName());
        Localizacao usuarioLocalizacao = usuario.getLocalizacao();

        if(usuarioLocalizacao != null) {
            usuario.getLocalizacao().setLatitude(localizacao.getLatitude());
            usuario.getLocalizacao().setLongitude(localizacao.getLongitude());
            localizacaoRepository.save(usuario.getLocalizacao());
        }else {
            Localizacao loc = new Localizacao();
            loc.setLongitude(localizacao.getLongitude());
            loc.setLatitude(localizacao.getLatitude());
            loc = localizacaoRepository.save(loc);
            usuario.setLocalizacao(loc);
            usuarioRepository.save(usuario);
        }

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
