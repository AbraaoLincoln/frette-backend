package br.com.fretee.freteebackend.frete.controllers;

import br.com.fretee.freteebackend.exceptions.UsuarioNotFoundException;
import br.com.fretee.freteebackend.frete.dto.FreteNotificacao;
import br.com.fretee.freteebackend.frete.dto.SolicitacaoServicoDTO;
import br.com.fretee.freteebackend.frete.exceptions.FreteNotFoundException;
import br.com.fretee.freteebackend.frete.service.FreteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/frete/notificacao")
@Slf4j
public class NotificacaoController {
    @Autowired
    private FreteService freteService;

    @GetMapping
    public ResponseEntity<List<FreteNotificacao>> getNotificacoesUsuario(Principal principal) {
        try {
            List<FreteNotificacao> notificacoes = freteService.getNotificacoes(principal.getName());
            return ResponseEntity.ok().body(notificacoes);
        } catch (UsuarioNotFoundException e) {
            log.error("Usuario {} nao encontrado", principal.getName());
            return ResponseEntity.badRequest().build();
        } catch (FreteNotFoundException e) {
            log.error("frete nao encontrado");
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/info")
    public ResponseEntity<SolicitacaoServicoDTO> getNotificacao(@PathVariable int id) {
        try {
            SolicitacaoServicoDTO notificacoes = freteService.getNotificacaoInfo(id);
            return ResponseEntity.ok().body(notificacoes);
        } catch (FreteNotFoundException e) {
            log.error("frete {} nao encontrado", id);
            return ResponseEntity.badRequest().build();
        }
    }
}
