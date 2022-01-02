package br.com.fretee.freteebackend.usuarios.controllers;

import br.com.fretee.freteebackend.autenticacao.api.AutenticacaoApi;
import br.com.fretee.freteebackend.exceptions.NomeUsuarioAlreadyInUseException;
import br.com.fretee.freteebackend.exceptions.UsuarioNotFindException;
import br.com.fretee.freteebackend.usuarios.entity.NovoUsuario;
import br.com.fretee.freteebackend.usuarios.service.ImagemUsuarioService;
import br.com.fretee.freteebackend.usuarios.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/usuario")
@Slf4j
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ImagemUsuarioService imagemService;
    @Autowired
    private AutenticacaoApi autenticacao;

    @PostMapping
    public ResponseEntity addUsuario(NovoUsuario novoUsuario, @RequestParam MultipartFile foto) {
        log.info("criando usuario {}", novoUsuario.getNome());
        log.info("image path: {}", imagemService.getPath());

        try{
            var usuario = novoUsuario.toUsuario();
            usuario.setFoto(imagemService.saveImage(foto));
            usuario = usuarioService.addUsuario(usuario);

            return ResponseEntity.created(new URI("/api/usuario")).build();
        }catch (NomeUsuarioAlreadyInUseException | URISyntaxException ex) {
            return ResponseEntity.badRequest().header("error", "nome usuario invalido").build();
        }
    }
}
