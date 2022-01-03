package br.com.fretee.freteebackend.usuarios.controllers;

import br.com.fretee.freteebackend.exceptions.NomeUsuarioAlreadyInUseException;
import br.com.fretee.freteebackend.exceptions.UsuarioNotFoundException;
import br.com.fretee.freteebackend.usuarios.dto.UsuarioDTO;
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

@RestController
@RequestMapping("/api/usuario")
@Slf4j
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ImagemUsuarioService imagemService;

    @PostMapping
    public ResponseEntity addUsuario(NovoUsuario novoUsuario, @RequestParam MultipartFile foto) {
        log.info("criando usuario {}", novoUsuario.getNomeCompleto());
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

    @GetMapping("/info")
    public ResponseEntity<UsuarioDTO> getUsuarioInfo(Principal principal) {
        try{
            return ResponseEntity.ok().body(usuarioService.getUsuarioInfo(principal));
        }catch (UsuarioNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/foto")
    public ResponseEntity getFotoUsurio(HttpServletResponse response, Principal principal) {
        try{
            var usuario = usuarioService.findUsuarioByNomeUsuario(principal.getName());
            var imageInputStream = imagemService.findImageAsInputStream(usuario.getFoto());
            imageInputStream.transferTo(response.getOutputStream());
            response.flushBuffer();

            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        } catch (UsuarioNotFoundException e) {
            log.error("Usuario não encontrado: {}", principal.getName());
            return ResponseEntity.badRequest().build();
        }
    }

//    @PostMapping("/{userId}/foto")
//    public ResponseEntity uploadFotoUsuario(HttpServletResponse response, @RequestParam MultipartFile foto, @PathVariable int userId) {
//        imagemService.saveImage(foto);
//        try{
//            var usuario = usuarioService.findUsuarioById(userId);
//            usuario.setFoto(imagemService.saveImage(foto));
//            usuarioService.saveUsurio(usuario);
//        }catch (UsuarioNotFindException e) {
//            response.setStatus(HttpStatus.BAD_REQUEST.value());
//        }
//
//        return new ResponseEntity(null);
//    }
//
//    @PatchMapping("/{userId}/foto")
//    public ResponseEntity atualizarFotoUsuario(HttpServletResponse response, @RequestParam MultipartFile foto, @PathVariable int userId) {
//        try{
//            var usuario = usuarioService.findUsuarioById(userId);
//            var fotoAtualId = usuario.getFoto();
//            usuario.setFoto(imagemService.saveImage(foto));
//            imagemService.deleteImagem(fotoAtualId);
//        }catch (UsuarioNotFindException e) {
//            response.setStatus(HttpStatus.BAD_REQUEST.value());
//        }
//
//        return new ResponseEntity(null);
//    }
}
