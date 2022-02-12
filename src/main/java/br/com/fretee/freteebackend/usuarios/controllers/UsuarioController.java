package br.com.fretee.freteebackend.usuarios.controllers;

import br.com.fretee.freteebackend.usuarios.exceptions.NomeUsuarioAlreadyInUseException;
import br.com.fretee.freteebackend.usuarios.exceptions.PrestadorServicoNotFoundException;
import br.com.fretee.freteebackend.usuarios.exceptions.UsuarioNotFoundException;
import br.com.fretee.freteebackend.usuarios.dto.UsuarioDTO;
import br.com.fretee.freteebackend.usuarios.entity.Localizacao;
import br.com.fretee.freteebackend.usuarios.entity.NovoUsuario;
import br.com.fretee.freteebackend.usuarios.service.ImagemUsuarioService;
import br.com.fretee.freteebackend.usuarios.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuario")
@Slf4j
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ImagemUsuarioService imagemService;

    @PostMapping
    public ResponseEntity addUsuario(NovoUsuario novoUsuario, @RequestParam MultipartFile foto) throws NomeUsuarioAlreadyInUseException, URISyntaxException {
        log.info("criando usuario {}", novoUsuario.getNomeCompleto());
        log.info("image path: {}", imagemService.getPath());

        var usuario = novoUsuario.toUsuario();
        usuario = usuarioService.addUsuario(usuario, foto);

        return ResponseEntity.created(new URI("/api/usuario")).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizarUsuario(@PathVariable int id, NovoUsuario novoUsuario, @RequestParam Optional<MultipartFile> foto) throws NomeUsuarioAlreadyInUseException, UsuarioNotFoundException {
        log.info("atualizando informacoes(foto incluida) do usuario de id = {}", id);

        usuarioService.atualizarUsuario(id, novoUsuario, foto.orElseGet(() -> null));

        return ResponseEntity.ok().build();
    }


    @GetMapping("/info")
    public ResponseEntity<UsuarioDTO> getUsuarioInfo(Principal principal) throws UsuarioNotFoundException {
        return ResponseEntity.ok().body(usuarioService.getUsuarioInfo(principal));
    }

    @GetMapping("/{nomeUsuario}/info")
    public ResponseEntity<UsuarioDTO> getUsuarioInfoParaNotificacao(Principal principal, @PathVariable String nomeUsuario ) throws UsuarioNotFoundException {
        return ResponseEntity.ok().body(usuarioService.getUsuarioInfo(principal, nomeUsuario));
    }

    @GetMapping("/foto")
    public ResponseEntity getFotoUsurio(HttpServletResponse response, Principal principal) throws UsuarioNotFoundException, IOException {
        var usuario = usuarioService.findUsuarioByNomeUsuario(principal.getName());
        var imageInputStream = imagemService.findImageAsInputStream(usuario.getFoto());
        imageInputStream.transferTo(response.getOutputStream());
        response.flushBuffer();

        return ResponseEntity.ok().build();
    }

    @PutMapping("/firebase/token")
    public ResponseEntity atualizarFirebaseToken(Principal principal, @RequestParam String token) throws UsuarioNotFoundException {
        System.out.println(token);
        usuarioService.atualizarFirebaseToken(principal.getName(), token);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/localizacao")
    public ResponseEntity atualizarLocalizacao(Principal principal, @RequestBody Localizacao localizacao) throws PrestadorServicoNotFoundException, UsuarioNotFoundException {
        usuarioService.atualizarLocalizacao(principal, localizacao);
        return ResponseEntity.ok().build();
    }

}
