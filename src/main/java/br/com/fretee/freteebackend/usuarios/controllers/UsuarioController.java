package br.com.fretee.freteebackend.usuarios.controllers;

import br.com.fretee.freteebackend.autenticacao.api.AutenticacaoApi;
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

        var usuario = novoUsuario.toUsuario();
        usuario.setFoto(imagemService.saveImage(foto));
        usuario = usuarioService.saveUsurio(usuario);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/foto")
    public ResponseEntity getFotoUsurio(HttpServletResponse response, @PathVariable int userId) {
        try{
            var usuario = usuarioService.findUsuarioById(userId);
            var imageInputStream = imagemService.findImageAsInputStream(usuario.getFoto());
            imageInputStream.transferTo(response.getOutputStream());
            response.flushBuffer();
        }catch (UsuarioNotFindException | IOException e) {
            ResponseEntity.badRequest().build();
        }

        return new ResponseEntity(null);
    }

    @PostMapping("/{userId}/foto")
    public ResponseEntity uploadFotoUsuario(HttpServletResponse response, @RequestParam MultipartFile foto, @PathVariable int userId) {
        imagemService.saveImage(foto);
        try{
            var usuario = usuarioService.findUsuarioById(userId);
            usuario.setFoto(imagemService.saveImage(foto));
            usuarioService.saveUsurio(usuario);
        }catch (UsuarioNotFindException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }

        return new ResponseEntity(null);
    }

    @PatchMapping("/{userId}/foto")
    public ResponseEntity atualizarFotoUsuario(HttpServletResponse response, @RequestParam MultipartFile foto, @PathVariable int userId) {
        try{
            var usuario = usuarioService.findUsuarioById(userId);
            var fotoAtualId = usuario.getFoto();
            usuario.setFoto(imagemService.saveImage(foto));
            imagemService.deleteImagem(fotoAtualId);
        }catch (UsuarioNotFindException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }

        return new ResponseEntity(null);
    }
}
