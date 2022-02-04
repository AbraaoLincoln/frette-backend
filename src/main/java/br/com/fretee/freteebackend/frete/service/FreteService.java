package br.com.fretee.freteebackend.frete.service;

import br.com.fretee.freteebackend.frete.dto.FreteDTO;
import br.com.fretee.freteebackend.usuarios.exceptions.UsuarioNotFoundException;
import br.com.fretee.freteebackend.frete.dto.FreteNotificacao;
import br.com.fretee.freteebackend.frete.dto.PrecoFreteDTO;
import br.com.fretee.freteebackend.frete.dto.SolicitacaoServicoDTO;
import br.com.fretee.freteebackend.frete.entity.FinalizaServicoComoContratanteStrategy;
import br.com.fretee.freteebackend.frete.entity.FinalizaServicoComoPrestadorServicoStrategy;
import br.com.fretee.freteebackend.frete.entity.FinalizarServicoStrategy;
import br.com.fretee.freteebackend.frete.entity.Frete;
import br.com.fretee.freteebackend.frete.enums.StatusFrete;
import br.com.fretee.freteebackend.frete.exceptions.*;
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
import java.util.Optional;

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

        //notificacaoService.pushNotification("Solicitação de Serviço", "Verifique suas notificações", prestadorServico.getFirebaseToken());

        frete.setNotificacaoEnviadaEm(LocalDateTime.now());
        freteRepository.save(frete);
    }

    private void verificarTempoEntreSolicitacoes(SolicitacaoServicoDTO solicitacaoServicoDTO) {
        //TODO: verificar se o tempo minimo entre duas solicitacoes ja passou.
    }

    private void validarSolicitacaoServico(SolicitacaoServicoDTO solicitacaoServicoDTO) throws SolicitacaoNotValidException {
        //TODO: validar campos de solicitacaoServicoDTO
    }

    public void cancelarSolicitacao(Principal principal, int freteId) throws InvalidFirebaseToken, FreteNotFoundException, OnlyContratanteCanDoThisActionException, CannotUpdateFreteStatusException {
        final String notificacaoTitulo = "Solicitacao de Serviço Cancelada";
        atualizarStatusFreteComoContratante(principal, freteId, StatusFrete.SOLICITANDO, StatusFrete.SOLICITACAO_CANCELADA, notificacaoTitulo);
    }

    public void recusarSolicitacao(Principal principal, int freteId) throws InvalidFirebaseToken, FreteNotFoundException, OnlyPrestadorServicoCanDoThisActionException, CannotUpdateFreteStatusException {
        final String notificacaoTitulo = "Solicitacao de Serviço Recusada";
        atualizarStatusFreteComoPrestadorServico(principal, freteId, StatusFrete.SOLICITANDO, StatusFrete.SOLICITACAO_RECUSADA, notificacaoTitulo);
    }

    public void aceitarPreco(Principal principal, int freteId) throws InvalidFirebaseToken, FreteNotFoundException, OnlyContratanteCanDoThisActionException, CannotUpdateFreteStatusException {
        final String notificacaoTitulo = "Frete Agendado";
        atualizarStatusFreteComoContratante(principal, freteId, StatusFrete.PRECO_INFORMADO, StatusFrete.AGENDADO, notificacaoTitulo);
    }

    public void recusarPreco(Principal principal, int freteId) throws InvalidFirebaseToken, FreteNotFoundException, OnlyContratanteCanDoThisActionException, CannotUpdateFreteStatusException {
        final String notificacaoTitulo = "Solicitacao de Serviço Cancelada";
        atualizarStatusFreteComoContratante(principal, freteId, StatusFrete.PRECO_INFORMADO, StatusFrete.PRECO_RECUSADO, notificacaoTitulo);
    }

    public void cancelarServico(Principal principal, int freteId) throws InvalidFirebaseToken, FreteNotFoundException, OnlyContratanteCanDoThisActionException, CannotUpdateFreteStatusException, OnlyPrestadorServicoCanDoThisActionException {
        final String notificacaoTitulo = "Frete Cancelado";
        boolean usuarioLogadoEhContratante = freteRepository.existsFreteByIdQueUsuarioEContratante(principal.getName(), freteId);

        if(usuarioLogadoEhContratante) {
            atualizarStatusFreteComoContratante(principal, freteId, StatusFrete.AGENDADO, StatusFrete.CANCELADO, notificacaoTitulo);
        }else {
            atualizarStatusFreteComoPrestadorServico(principal, freteId, StatusFrete.AGENDADO, StatusFrete.CANCELADO, notificacaoTitulo);
        }
    }

    public void finalizarServico(Principal principal, int freteId) throws InvalidFirebaseToken, FreteNotFoundException, OnlyContratanteCanDoThisActionException, CannotUpdateFreteStatusException, OnlyPrestadorServicoCanDoThisActionException {
        FinalizarServicoStrategy finalizarServicoStrategy = null;
        Frete frete = findFreteById(freteId);
        boolean usuarioLogadoEhContratante = principal.getName().equals(frete.getContratante().getNomeUsuario());
        boolean usuarioLogadoEhPrestadorServico = principal.getName().equals(frete.getPrestadorServico().getNomeUsuario());

        if(usuarioLogadoEhContratante) finalizarServicoStrategy = new FinalizaServicoComoContratanteStrategy(frete, freteRepository, notificacaoService);
        else if(usuarioLogadoEhPrestadorServico) finalizarServicoStrategy = new FinalizaServicoComoPrestadorServicoStrategy(frete, freteRepository, notificacaoService);

        if(finalizarServicoStrategy != null) {
            finalizarServicoStrategy.atualizarFreteParaFinalizado();
        }else {
            throw new CannotUpdateFreteStatusException();
        }
    }

    private void atualizarStatusFreteComoContratante(Principal principal, int freteId, StatusFrete statusQueDeveEsta, StatusFrete novoStatus, String notificacaoTitulo) throws FreteNotFoundException, CannotUpdateFreteStatusException, OnlyContratanteCanDoThisActionException, InvalidFirebaseToken {
        Frete frete = findFreteById(freteId);
        if(!principal.getName().equals(frete.getContratante().getNomeUsuario())) throw new OnlyContratanteCanDoThisActionException("O usuario " + principal.getName() + " nao e o contratante do frete");
        if(frete.getStatus() != statusQueDeveEsta) throw new CannotUpdateFreteStatusException();
        frete = atualizarStatusFrete(frete, novoStatus);
        //notificar(frete, notificacaoTitulo, frete.getPrestadorServico().getFirebaseToken());
    }

    private void atualizarStatusFreteComoPrestadorServico(Principal principal, int freteId, StatusFrete statusQueDeveEsta, StatusFrete novoStatus, String notificacaoTitulo) throws FreteNotFoundException, CannotUpdateFreteStatusException, OnlyPrestadorServicoCanDoThisActionException, InvalidFirebaseToken {
        Frete frete = findFreteById(freteId);
        if(!principal.getName().equals(frete.getPrestadorServico().getNomeUsuario())) throw new OnlyPrestadorServicoCanDoThisActionException("O usuario " + principal.getName() + "nao e o prestador de servico do frete");
        if(frete.getStatus() != statusQueDeveEsta) throw new CannotUpdateFreteStatusException();
        frete = atualizarStatusFrete(frete, novoStatus);
        //notificar(frete, notificacaoTitulo, frete.getContratante().getFirebaseToken());
    }

    private Frete atualizarStatusFrete(Frete frete, StatusFrete novoStatus) throws InvalidFirebaseToken {
        frete.setStatus(novoStatus);
        return freteRepository.save(frete);
    }

    private void notificar(Frete frete, String notificacaoTitulo, String token) throws InvalidFirebaseToken {
        notificacaoService.pushNotification(notificacaoTitulo, "Verifique suas notificações", token);

        frete.setNotificacaoEnviadaEm(LocalDateTime.now());
        freteRepository.save(frete);
    }

    public void informarPrecoServico(Principal principal, PrecoFreteDTO precoDTO) throws InvalidFirebaseToken, FreteNotFoundException, OnlyPrestadorServicoCanDoThisActionException, CannotUpdateFreteStatusException {
        final String notificaoTitulo = "Preço do Serviço Informado";

        validarInformacaoPreco(precoDTO);
        Frete frete = findFreteById(precoDTO.getFreteId());

        if(!principal.getName().equals(frete.getPrestadorServico().getNomeUsuario())) throw new OnlyPrestadorServicoCanDoThisActionException("O usuario " + principal.getName() + "nao e o prestador de servico do frete");
        if(frete.getStatus() != StatusFrete.SOLICITANDO) throw new CannotUpdateFreteStatusException();

        frete.setPreco(precoDTO.getPreco());
        atualizarStatusFrete(frete, StatusFrete.PRECO_INFORMADO);
        //notificar(frete, notificaoTitulo, frete.getContratante().getFirebaseToken());
    }

    private void validarInformacaoPreco(PrecoFreteDTO preco){}

    public Frete findFreteById(int id) throws FreteNotFoundException{
        Optional<Frete> freteOptional = freteRepository.findById(id);
        if(freteOptional.isEmpty()) throw new FreteNotFoundException("O frete de id = " + id + "nao foi encontrado");
        return freteOptional.get();
    }

    public List<FreteNotificacao> getNotificacoes(String nomeUsuario) throws UsuarioNotFoundException, FreteNotFoundException {
        int usuarioId = usuarioService.findIdUsuarioByNomeUsuario(nomeUsuario);
        Optional<List<Frete>> fretesOptional = freteRepository.findFreteByContratanteIdOrPrestadorServicoId(usuarioId);
        if(fretesOptional.isEmpty()) throw new FreteNotFoundException("O fretes do usuario " + nomeUsuario + "nao foi encontrado");
        List<Frete> fretes = fretesOptional.get();

        List<FreteNotificacao> notificacoes = new ArrayList<>();
        fretes.forEach(frete -> {
            FreteNotificacao notificacao = new FreteNotificacao();
            notificacao.setId(frete.getId());
            notificacao.setStatus(frete.getStatus().toString());
            notificacao.setContratante(frete.getContratante().getNomeUsuario());
            notificacao.setPrestadorServico(frete.getPrestadorServico().getNomeUsuario());
            notificacoes.add(notificacao);
        });

        return notificacoes;
    }

    public SolicitacaoServicoDTO getNotificacaoInfo(int id) throws FreteNotFoundException {
        Optional<Frete> freteOptional = freteRepository.findById(id);
        if(freteOptional.isEmpty()) throw new FreteNotFoundException("O frete de id = " + id + "nao foi encontrado");
        Frete frete = freteOptional.get();

        SolicitacaoServicoDTO notificacao = buildSolicitacaoServicoDTOFromFrete(frete);
        notificacao.setContratanteNomeUsuario(frete.getContratante().getNomeUsuario());
        notificacao.setPrestadorServicoNomeUsuario(frete.getPrestadorServico().getNomeUsuario());
        notificacao.setStatus(frete.getStatus().toString());

        return notificacao;
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

    private FreteDTO buildFreteDTOFromFrete(Frete frete) {
        var freteDTO = new FreteDTO();

        freteDTO.setId(frete.getId());
        freteDTO.setOrigem(frete.getOrigem());
        freteDTO.setDestino(frete.getDestino());
        freteDTO.setData(frete.getData());
        freteDTO.setHora(frete.getHora());
        freteDTO.setPreco(frete.getPreco());
        freteDTO.setDescricaoCarga(frete.getDescricaoCarga());
        freteDTO.setPrecisaAjudade(frete.getPrecisaAjudade());
        freteDTO.setNotaContratanteRecebeu(frete.getNotaContratanteRecebeu());
        freteDTO.setNotaPrestadorServicoRecebeu(frete.getNotaPrestadorServicoRecebeu());

        return freteDTO;
    }

    private SolicitacaoServicoDTO buildSolicitacaoServicoDTOFromFrete(Frete frete) {
        var solicitacaoServicoDTO = new SolicitacaoServicoDTO();

        solicitacaoServicoDTO.setId(frete.getId());
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

    public List<FreteDTO> getFretesAgendados(Principal principal) throws UsuarioNotFoundException {
        int idUsuarioLogado = usuarioService.findIdUsuarioByNomeUsuario(principal.getName());
        List<Frete> fretes = freteRepository.findByContratanteIdOrPrestadorServicoIdAndStatus(idUsuarioLogado, StatusFrete.AGENDADO.name());
        System.out.println(StatusFrete.AGENDADO.name());
        System.out.println();
        List<FreteDTO> fretesDTO = new ArrayList<>();
        for(Frete frete : fretes) {
            FreteDTO freteDTO = buildFreteDTOFromFrete(frete);
            freteDTO.setContratanteNomeUsuario(frete.getContratante().getNomeUsuario());
            freteDTO.setPrestadorServicoNomeUsuario(frete.getPrestadorServico().getNomeUsuario());
            freteDTO.setStatus(frete.getStatus().toString());
            fretesDTO.add(freteDTO);
        }

        return fretesDTO;
    }
}
