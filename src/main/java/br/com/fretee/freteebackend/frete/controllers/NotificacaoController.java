package br.com.fretee.freteebackend.frete.controllers;

import br.com.fretee.freteebackend.usuarios.exceptions.UsuarioNotFoundException;
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
    public ResponseEntity<List<FreteNotificacao>> getNotificacoesUsuario(Principal principal) throws FreteNotFoundException, UsuarioNotFoundException {
        List<FreteNotificacao> notificacoes = freteService.getNotificacoes(principal.getName());
        return ResponseEntity.ok().body(notificacoes);
    }

    @GetMapping("/{id}/info")
    public ResponseEntity<SolicitacaoServicoDTO> getNotificacao(@PathVariable int id) throws FreteNotFoundException {
        SolicitacaoServicoDTO notificacoes = freteService.getNotificacaoInfo(id);
        return ResponseEntity.ok().body(notificacoes);
    }
}
