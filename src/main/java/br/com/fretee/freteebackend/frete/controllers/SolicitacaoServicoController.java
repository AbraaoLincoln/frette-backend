package br.com.fretee.freteebackend.frete.controllers;

import br.com.fretee.freteebackend.exceptions.UsuarioNotFoundException;
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
    public ResponseEntity solicitarServico(Principal principal, @RequestBody SolicitacaoServicoDTO solicitacaoServicoDTO) {
        try{
            freteService.solicitarServico(principal, solicitacaoServicoDTO);
            return ResponseEntity.created(new URI("/api/frete/solicita-servico/solicitar")).build();
        } catch (UsuarioNotFoundException e) {
            log.error("Usuario {} ou {} nao encontrado", principal.getName(), solicitacaoServicoDTO.getPrestadorServicoNomeUsuario());
            return ResponseEntity.badRequest().build();
        } catch (SolicitacaoNotValidException e) {
            log.error("Solicitacao de servico nao e valida");
            return ResponseEntity.badRequest().build();
        } catch (TimeBetweenSolicitacoesNotValidException e) {
            log.error("Tempo perminito entre solicitacoes ainda nao passou");
            return ResponseEntity.badRequest().build();
        } catch (InvalidFirebaseToken e) {
            log.error("firebase token invalido");
            return ResponseEntity.internalServerError().build();
        } catch (URISyntaxException e) {
            log.error("URISyntaxException");
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{freteId}/solicitacao/cancelar")
    public ResponseEntity cancelarSolicitacao(Principal principal, @PathVariable int freteId) {

        try{
            freteService.cancelarSolicitacao(principal, freteId);
            return ResponseEntity.ok().build();
        } catch (FreteNotFoundException e) {
            log.error("Frete de id ={} nao encontrado", freteId);
            return ResponseEntity.badRequest().build();
        } catch (InvalidFirebaseToken e) {
            log.error("Firebase token invalido");
            return ResponseEntity.internalServerError().build();
        }  catch (CannotUpdateFreteStatusException e) {
            log.error("O frete nao pode ter seu status atualizado", principal.getName(), freteId);
            return ResponseEntity.internalServerError().build();
        } catch (OnlyContratanteCanDoThisActionException e) {
            log.error("O usuario {} nao e contratante no frete de id = {}", principal.getName(), freteId);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{freteId}/preco/informar")
    public ResponseEntity informaPreco(Principal principal, @PathVariable int freteId, @RequestBody PrecoFreteDTO precoFreteDTO) {

        try{
            freteService.informarPrecoServico(principal, precoFreteDTO);
            return ResponseEntity.ok().build();
        } catch (FreteNotFoundException e) {
            log.error("Frete de id ={} nao encontrado", freteId);
            return ResponseEntity.badRequest().build();
        } catch (InvalidFirebaseToken e) {
            log.error("Firebase token invalido");
            return ResponseEntity.internalServerError().build();
        } catch (OnlyPrestadorServicoCanDoThisActionException e) {
            log.error("O usuario {} nao e prestador de servico no frete de id = {}", principal.getName(), freteId);
            return ResponseEntity.badRequest().build();
        } catch (CannotUpdateFreteStatusException e) {
            log.error("O frete nao pode ter seu status atualizado", principal.getName(), freteId);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{freteId}/preco/aceitar")
    public ResponseEntity aceitarPreco(Principal principal, @PathVariable int freteId) {

        try{
            freteService.aceitarPreco(principal, freteId);
            return ResponseEntity.ok().build();
        } catch (FreteNotFoundException e) {
            log.error("Frete de id ={} nao encontrado", freteId);
            return ResponseEntity.badRequest().build();
        } catch (InvalidFirebaseToken e) {
            log.error("Firebase token invalido");
            return ResponseEntity.internalServerError().build();
        }  catch (CannotUpdateFreteStatusException e) {
            log.error("O frete nao pode ter seu status atualizado", principal.getName(), freteId);
            return ResponseEntity.internalServerError().build();
        } catch (OnlyContratanteCanDoThisActionException e) {
            log.error("O usuario {} nao e contratante no frete de id = {}", principal.getName(), freteId);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{freteId}/preco/recusar")
    public ResponseEntity recusarPreco(Principal principal, @PathVariable int freteId) {

        try{
            freteService.recusarPreco(principal, freteId);
            return ResponseEntity.ok().build();
        } catch (FreteNotFoundException e) {
            log.error("Frete de id ={} nao encontrado", freteId);
            return ResponseEntity.badRequest().build();
        } catch (InvalidFirebaseToken e) {
            log.error("Firebase token invalido");
            return ResponseEntity.internalServerError().build();
        }  catch (CannotUpdateFreteStatusException e) {
            log.error("O frete nao pode ter seu status atualizado", principal.getName(), freteId);
            return ResponseEntity.internalServerError().build();
        } catch (OnlyContratanteCanDoThisActionException e) {
            log.error("O usuario {} nao e contratante no frete de id = {}", principal.getName(), freteId);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{freteId}/cancelar")
    public ResponseEntity cancelarServico(Principal principal, @PathVariable int freteId) {

        try{
            freteService.cancelarServico(principal, freteId);
            return ResponseEntity.ok().build();
        } catch (FreteNotFoundException e) {
            log.error("Frete de id ={} nao encontrado", freteId);
            return ResponseEntity.badRequest().build();
        } catch (InvalidFirebaseToken e) {
            log.error("Firebase token invalido");
            return ResponseEntity.internalServerError().build();
        }  catch (CannotUpdateFreteStatusException e) {
            log.error("O frete nao pode ter seu status atualizado", principal.getName(), freteId);
            return ResponseEntity.internalServerError().build();
        } catch (OnlyContratanteCanDoThisActionException e) {
            log.error("O usuario {} nao e contratante no frete de id = {}", principal.getName(), freteId);
            return ResponseEntity.badRequest().build();
        } catch (OnlyPrestadorServicoCanDoThisActionException e) {
            log.error("O usuario {} nao e prestador de servico no frete de id = {}", principal.getName(), freteId);
            return ResponseEntity.badRequest().build();
        }
    }
}
