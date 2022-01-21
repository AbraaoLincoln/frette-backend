package br.com.fretee.freteebackend.frete;

import br.com.fretee.freteebackend.exceptions.UsuarioNotFoundException;
import br.com.fretee.freteebackend.frete.dto.SolicitacaoServicoDTO;
import br.com.fretee.freteebackend.frete.entity.Frete;
import br.com.fretee.freteebackend.frete.exceptions.SolicitacaoNotValidException;
import br.com.fretee.freteebackend.frete.exceptions.TimeBetweenSolicitacoesNotValidException;
import br.com.fretee.freteebackend.frete.service.FreteService;
import br.com.fretee.freteebackend.usuarios.entity.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/frete")
@Slf4j
public class FreteController {
    @Autowired
    private FreteService freteService;

    @PostMapping("/solicitar/{token}")
    public ResponseEntity solicitarServico(Principal principal, @RequestBody SolicitacaoServicoDTO solicitacaoServicoDTO, @PathVariable String token) {
        try{
            freteService.solicitarServico(principal, solicitacaoServicoDTO, token);
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
        }
    }
}
