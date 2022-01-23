package br.com.fretee.freteebackend.frete.service;

import br.com.fretee.freteebackend.exceptions.UsuarioNotFoundException;
import br.com.fretee.freteebackend.frete.api.FreteApi;
import br.com.fretee.freteebackend.frete.dto.FreteDTO;
import br.com.fretee.freteebackend.frete.dto.SolicitacaoServicoDTO;
import br.com.fretee.freteebackend.frete.entity.Frete;
import br.com.fretee.freteebackend.frete.enums.StatusFrete;
import br.com.fretee.freteebackend.frete.exceptions.InvalidFirebaseToken;
import br.com.fretee.freteebackend.frete.exceptions.SolicitacaoNotValidException;
import br.com.fretee.freteebackend.frete.exceptions.TimeBetweenSolicitacoesNotValidException;
import br.com.fretee.freteebackend.frete.repository.FreteRepository;
import br.com.fretee.freteebackend.usuarios.entity.Usuario;
import br.com.fretee.freteebackend.usuarios.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FreteService {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private FreteRepository freteRepository;
    @Autowired
    private NotificacaoService notificacaoService;

    public void solicitarServico(Principal principal, SolicitacaoServicoDTO solicitacaoServicoDTO) throws TimeBetweenSolicitacoesNotValidException, SolicitacaoNotValidException, UsuarioNotFoundException, InvalidFirebaseToken {
        //showSolicitacaoDeServico( solicitacaoServicoDTO);
        verificarTempoEntreSolicitacoes(solicitacaoServicoDTO);
        validarSolicitacaoServico(solicitacaoServicoDTO);

        Frete frete = buildFreteFromSolicitacaoServicoDTO(solicitacaoServicoDTO);
        Usuario contratante = usuarioService.findUsuarioByNomeUsuario(principal.getName());
        Usuario prestadorServico = usuarioService.findUsuarioByNomeUsuario(solicitacaoServicoDTO.getPrestadorServicoNomeUsuario());
        frete.setContratante(contratante);
        frete.setPrestadorServico(prestadorServico);
        frete.setStatus(StatusFrete.SOLICITANDO);
        frete = freteRepository.save(frete);

        notificacaoService.pushNotification("Solicitação de Serviço", "Verifique suas notificações", prestadorServico.getFirebaseToken());

        frete.setNotificacaoEnviadaEm(LocalDateTime.now());
        freteRepository.save(frete);
    }

    private void verificarTempoEntreSolicitacoes(SolicitacaoServicoDTO solicitacaoServicoDTO) {
        //TODO: verificar se o tempo minimo entre duas solicitacoes ja passou.
    }

    private void validarSolicitacaoServico(SolicitacaoServicoDTO solicitacaoServicoDTO) throws SolicitacaoNotValidException {
        //TODO: validar campos de solicitacaoServicoDTO
    }

    private Frete buildFreteFromSolicitacaoServicoDTO(SolicitacaoServicoDTO solicitacaoServicoDTO) {
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

    public List<SolicitacaoServicoDTO> getNotificacoes(String nomeUsuario) throws UsuarioNotFoundException {
        int usuarioId = usuarioService.findIdUsuarioByNomeUsuario(nomeUsuario);
        List<Frete> fretes = freteRepository.findByContratanteIdOrPrestadorServicoId(usuarioId);

        List<SolicitacaoServicoDTO> notificacoes = new ArrayList<>();
        for(Frete frete : fretes) {
            SolicitacaoServicoDTO notificacao = buildSolicitacaoServicoDTOFromFrete(frete);
            notificacao.setContratanteNomeUsuario(frete.getContratante().getNomeUsuario());
            notificacao.setPrestadorServicoNomeUsuario(frete.getPrestadorServico().getNomeUsuario());
            notificacao.setStatus(frete.getStatus().toString());
            notificacoes.add(notificacao);
        }

        return notificacoes;
    }

    private SolicitacaoServicoDTO buildSolicitacaoServicoDTOFromFrete(Frete frete) {
        var solicitacaoServicoDTO = new SolicitacaoServicoDTO();

        solicitacaoServicoDTO.setOrigem(frete.getOrigem());
        solicitacaoServicoDTO.setDestino(frete.getDestino());
        solicitacaoServicoDTO.setData(frete.getData());
        solicitacaoServicoDTO.setHora(frete.getHora());
        solicitacaoServicoDTO.setPreco(frete.getPreco());
        solicitacaoServicoDTO.setDescricaoCarga(frete.getDescricaoCarga());
        solicitacaoServicoDTO.setPrecisaAjudade(frete.getPrecisaAjudade());

        return solicitacaoServicoDTO;
    }

    private void showSolicitacaoDeServico(SolicitacaoServicoDTO solicitacaoServicoDTO) {
        System.out.println(solicitacaoServicoDTO.getOrigem());
        System.out.println(solicitacaoServicoDTO.getDestino());
        System.out.println(solicitacaoServicoDTO.getDescricaoCarga());
        System.out.println(solicitacaoServicoDTO.getData());
        System.out.println(solicitacaoServicoDTO.getHora());
        System.out.println(solicitacaoServicoDTO.getPreco());
        System.out.println(solicitacaoServicoDTO.getPrestadorServicoNomeUsuario());
    }
}
