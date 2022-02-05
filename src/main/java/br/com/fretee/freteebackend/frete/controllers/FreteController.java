package br.com.fretee.freteebackend.frete.controllers;

import br.com.fretee.freteebackend.frete.dto.AvaliacaoDTO;
import br.com.fretee.freteebackend.frete.dto.FreteDTO;
import br.com.fretee.freteebackend.frete.dto.FreteNotificacao;
import br.com.fretee.freteebackend.frete.dto.SolicitacaoServicoDTO;
import br.com.fretee.freteebackend.frete.exceptions.CannotUpdateNotaDoFreteException;
import br.com.fretee.freteebackend.frete.exceptions.FreteNotFoundException;
import br.com.fretee.freteebackend.frete.service.FreteService;
import br.com.fretee.freteebackend.usuarios.exceptions.UsuarioNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/frete")
public class FreteController {
    @Autowired
    private FreteService freteService;

    @GetMapping("/notificacao")
    public ResponseEntity<List<FreteNotificacao>> getNotificacoesUsuario(Principal principal) throws FreteNotFoundException, UsuarioNotFoundException {
        List<FreteNotificacao> notificacoes = freteService.getNotificacoes(principal.getName());
        return ResponseEntity.ok().body(notificacoes);
    }

    @GetMapping("/notificacao/{id}/info")
    public ResponseEntity<SolicitacaoServicoDTO> getNotificacao(@PathVariable int id) throws FreteNotFoundException {
        SolicitacaoServicoDTO notificacoes = freteService.getNotificacaoInfo(id);
        return ResponseEntity.ok().body(notificacoes);
    }

    @GetMapping("/agendados")
    public ResponseEntity<List<FreteDTO>> getFretesAgendados(Principal principal) throws UsuarioNotFoundException {
        return ResponseEntity.ok().body(freteService.getFretesAgendados(principal));
    }

//    @PutMapping("/{freteId}/avaliacao")
//    public ResponseEntity atualizarNotaFrete(Principal principal, @PathVariable int freteId, @RequestBody AvaliacaoDTO avaliacaoDTO) throws FreteNotFoundException, CannotUpdateNotaDoFreteException {
//        freteService.atualizarNotaFrete(principal, freteId, avaliacaoDTO.getNota());
//        return ResponseEntity.ok().build();
//    }
}
