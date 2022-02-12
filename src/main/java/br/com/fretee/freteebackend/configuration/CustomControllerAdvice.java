package br.com.fretee.freteebackend.configuration;


import br.com.fretee.freteebackend.frete.exceptions.*;
import br.com.fretee.freteebackend.usuarios.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.net.URISyntaxException;

@ControllerAdvice
@Slf4j
public class CustomControllerAdvice {

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity usuarioNotFoundException(UsuarioNotFoundException ex ) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(PrestadorServicoNotFoundException.class)
    public ResponseEntity prestadorServicoNotFoundException(PrestadorServicoNotFoundException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(NomeUsuarioAlreadyInUseException.class)
    public ResponseEntity nomeUsuarioAlreadyInUseException(NomeUsuarioAlreadyInUseException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().header("error", "nome usuario ja esta em uso").build();
    }

    @ExceptionHandler(FreteNotFoundException.class)
    public ResponseEntity freteNotFoundException(FreteNotFoundException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(InvalidFirebaseToken.class)
    public ResponseEntity invalidFirebaseToken(InvalidFirebaseToken ex) {
        log.error(ex.getMessage());
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(CannotUpdateFreteStatusException.class)
    public ResponseEntity cannotUpdateFreteStatusException(CannotUpdateFreteStatusException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(OnlyContratanteCanDoThisActionException.class)
    public ResponseEntity onlyContratanteCanDoThisActionException(OnlyContratanteCanDoThisActionException ex ) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(OnlyPrestadorServicoCanDoThisActionException.class)
    public ResponseEntity onlyPrestadorServicoCanDoThisActionException(OnlyPrestadorServicoCanDoThisActionException ex ) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(UsuarioAlreadyJoinAsPrestadorServico.class)
    public ResponseEntity usuarioAlreadyJoinAsPrestadorServico(UsuarioAlreadyJoinAsPrestadorServico ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(VeiculoNotFoundException.class)
    public ResponseEntity veiculoNotFoundException(VeiculoNotFoundException ex ) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(CannotUpdateNotaDoFreteException.class)
    public ResponseEntity cannotUpdateNotaDoFreteException(CannotUpdateNotaDoFreteException ex ) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(UsuarioNaoEDonoDoVeiculoException.class)
    public ResponseEntity usuarioNaoEDonoDoVeiculoException(UsuarioNaoEDonoDoVeiculoException ex ) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().build();
    }

    //==================================== Java Exceptions ======================================
    @ExceptionHandler(IOException.class)
    public ResponseEntity iOException(IOException ex) {
        log.error("IOException exception");
        ex.printStackTrace();
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(URISyntaxException.class)
    public ResponseEntity uRISyntaxException(URISyntaxException ex) {
        log.error("URISyntaxException exception");
        ex.printStackTrace();
        return ResponseEntity.internalServerError().build();
    }


}
