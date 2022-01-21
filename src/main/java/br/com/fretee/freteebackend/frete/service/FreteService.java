package br.com.fretee.freteebackend.frete.service;

import br.com.fretee.freteebackend.exceptions.UsuarioNotFoundException;
import br.com.fretee.freteebackend.frete.api.FreteApi;
import br.com.fretee.freteebackend.frete.dto.FreteDTO;
import br.com.fretee.freteebackend.frete.dto.SolicitacaoServicoDTO;
import br.com.fretee.freteebackend.frete.entity.Frete;
import br.com.fretee.freteebackend.frete.exceptions.SolicitacaoNotValidException;
import br.com.fretee.freteebackend.frete.exceptions.TimeBetweenSolicitacoesNotValidException;
import br.com.fretee.freteebackend.frete.repository.FreteRepository;
import br.com.fretee.freteebackend.usuarios.entity.Usuario;
import br.com.fretee.freteebackend.usuarios.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@Slf4j
public class FreteService {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private FreteRepository freteRepository;
    @Autowired
    private FirebaseMessagingService firebaseMessagingService;

    public void solicitarServico(Principal principal, SolicitacaoServicoDTO solicitacaoServicoDTO, String firebaseToken) throws TimeBetweenSolicitacoesNotValidException, SolicitacaoNotValidException, UsuarioNotFoundException {

        verificarTempoEntreSolicitacoes(solicitacaoServicoDTO);
        validarSolicitacaoServico(solicitacaoServicoDTO);

        Frete frete = buildFromSolicitacaoServicoDTO(solicitacaoServicoDTO);
        Usuario contratante = usuarioService.findUsuarioByNomeUsuario(principal.getName());
        Usuario prestadorServico = usuarioService.findUsuarioByNomeUsuario(solicitacaoServicoDTO.getPrestadorServicoNomeUsuario());
        frete.setContratante(contratante);
        frete.setPrestadorServico(prestadorServico);

        freteRepository.save(frete);
        //TODO: salvar o notificacao
        firebaseMessagingService.sendNotification("Solicitação de Serviço", "Verifique suas notificações", firebaseToken);
    }

    private void verificarTempoEntreSolicitacoes(SolicitacaoServicoDTO solicitacaoServicoDTO) {
        //TODO: verificar se o tempo minimo entre duas solicitacoes ja passou.
    }

    private void validarSolicitacaoServico(SolicitacaoServicoDTO solicitacaoServicoDTO) throws SolicitacaoNotValidException {
        //TODO: validar campos de solicitacaoServicoDTO
    }

    private Frete buildFromSolicitacaoServicoDTO(SolicitacaoServicoDTO solicitacaoServicoDTO) {
        var frete = new Frete();

        frete.setOrigem(solicitacaoServicoDTO.getOrigem());
        frete.setDestino(solicitacaoServicoDTO.getDestino());
        frete.setData(solicitacaoServicoDTO.getData());
        frete.setHora(solicitacaoServicoDTO.getHora());
        frete.setPreco(solicitacaoServicoDTO.getPreco());
        frete.setDescricaoCarga(solicitacaoServicoDTO.getDescricaoCarga());
        frete.setPrecisaAjudade(solicitacaoServicoDTO.getPrecisaAjudade());

        return frete;
    }
}
