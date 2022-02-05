package br.com.fretee.freteebackend.frete.controllers;

import br.com.fretee.freteebackend.frete.dto.AvaliacaoDTO;
import br.com.fretee.freteebackend.usuarios.exceptions.UsuarioNotFoundException;
import br.com.fretee.freteebackend.frete.dto.PrecoFreteDTO;
import br.com.fretee.freteebackend.frete.dto.SolicitacaoServicoDTO;
import br.com.fretee.freteebackend.frete.exceptions.*;
import br.com.fretee.freteebackend.frete.service.FreteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

@RestController
@RequestMapping("/api/frete")
@Slf4j
public class SolicitacaoServicoController {
    @Autowired
    private FreteService freteService;

    @PostMapping("/solicitacao")
    public ResponseEntity solicitarServico(Principal principal, @RequestBody SolicitacaoServicoDTO solicitacaoServicoDTO) throws SolicitacaoNotValidException, InvalidFirebaseToken, TimeBetweenSolicitacoesNotValidException, UsuarioNotFoundException, URISyntaxException {
        freteService.solicitarServico(principal, solicitacaoServicoDTO);
        return ResponseEntity.created(new URI("/api/frete/solicita-servico/solicitar")).build();
    }

    @PutMapping("/{freteId}/solicitacao/cancelar")
    public ResponseEntity cancelarSolicitacao(Principal principal, @PathVariable int freteId) throws CannotUpdateFreteStatusException, FreteNotFoundException, InvalidFirebaseToken, OnlyContratanteCanDoThisActionException {
        freteService.cancelarSolicitacao(principal, freteId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{freteId}/solicitacao/recusar")
    public ResponseEntity recusarSolicitacao(Principal principal, @PathVariable int freteId) throws CannotUpdateFreteStatusException, FreteNotFoundException, InvalidFirebaseToken, OnlyPrestadorServicoCanDoThisActionException {
        freteService.recusarSolicitacao(principal, freteId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{freteId}/preco/informar")
    public ResponseEntity informaPreco(Principal principal, @PathVariable int freteId, @RequestBody PrecoFreteDTO precoFreteDTO) throws CannotUpdateFreteStatusException, FreteNotFoundException, InvalidFirebaseToken, OnlyPrestadorServicoCanDoThisActionException {
        freteService.informarPrecoServico(principal, precoFreteDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{freteId}/preco/aceitar")
    public ResponseEntity aceitarPreco(Principal principal, @PathVariable int freteId) throws CannotUpdateFreteStatusException, FreteNotFoundException, InvalidFirebaseToken, OnlyContratanteCanDoThisActionException {
        freteService.aceitarPreco(principal, freteId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{freteId}/preco/recusar")
    public ResponseEntity recusarPreco(Principal principal, @PathVariable int freteId) throws CannotUpdateFreteStatusException, FreteNotFoundException, InvalidFirebaseToken, OnlyContratanteCanDoThisActionException {
        freteService.recusarPreco(principal, freteId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{freteId}/cancelar")
    public ResponseEntity cancelarServico(Principal principal, @PathVariable int freteId) throws CannotUpdateFreteStatusException, FreteNotFoundException, InvalidFirebaseToken, OnlyPrestadorServicoCanDoThisActionException, OnlyContratanteCanDoThisActionException {
        freteService.cancelarServico(principal, freteId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{freteId}/finalizar")
    public ResponseEntity finalizarServico(Principal principal, @PathVariable int freteId, @RequestBody AvaliacaoDTO avaliacaoDTO) throws CannotUpdateFreteStatusException, FreteNotFoundException, InvalidFirebaseToken, OnlyPrestadorServicoCanDoThisActionException, OnlyContratanteCanDoThisActionException, CannotUpdateNotaDoFreteException {
        freteService.finalizarServico(principal, freteId, avaliacaoDTO);
        return ResponseEntity.ok().build();
    }
}
