package br.com.fretee.freteebackend.frete.controllers;

import br.com.fretee.freteebackend.exceptions.UsuarioNotFoundException;
import br.com.fretee.freteebackend.frete.dto.SolicitacaoServicoDTO;
import br.com.fretee.freteebackend.frete.exceptions.InvalidFirebaseToken;
import br.com.fretee.freteebackend.frete.exceptions.SolicitacaoNotValidException;
import br.com.fretee.freteebackend.frete.exceptions.TimeBetweenSolicitacoesNotValidException;
import br.com.fretee.freteebackend.frete.service.FreteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/frete/solicita-servico")
@Slf4j
public class SolicitacaoServicoController {
    @Autowired
    private FreteService freteService;

    @PostMapping("/solicitar")
    public ResponseEntity solicitarServico(Principal principal, @RequestBody SolicitacaoServicoDTO solicitacaoServicoDTO) {
        try{
            freteService.solicitarServico(principal, solicitacaoServicoDTO);
            return ResponseEntity.ok().build();
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
        }
    }
}
