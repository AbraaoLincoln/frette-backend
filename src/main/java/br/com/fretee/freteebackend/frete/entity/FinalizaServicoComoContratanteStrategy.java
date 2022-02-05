package br.com.fretee.freteebackend.frete.entity;

import br.com.fretee.freteebackend.frete.enums.StatusFrete;
import br.com.fretee.freteebackend.frete.exceptions.CannotUpdateFreteStatusException;
import br.com.fretee.freteebackend.frete.exceptions.InvalidFirebaseToken;
import br.com.fretee.freteebackend.frete.repository.FreteRepository;
import br.com.fretee.freteebackend.frete.service.NotificacaoService;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class FinalizaServicoComoContratanteStrategy implements FinalizarServicoStrategy{
    private Frete frete;
    private FreteRepository freteRepository;
    private NotificacaoService notificacaoService;

    @Override
    public Frete atualizarFreteParaFinalizado() throws CannotUpdateFreteStatusException, InvalidFirebaseToken {
        if(!(frete.getStatus() == StatusFrete.AGENDADO || frete.getStatus() == StatusFrete.PRESTADOR_SERVICO_FINALIZOU)){
            throw new CannotUpdateFreteStatusException();
        }

        if(frete.getStatus() == StatusFrete.AGENDADO) {
            frete.setStatus(StatusFrete.CONTRATANTE_FINALIZOU);
        }else if(frete.getStatus() == StatusFrete.PRESTADOR_SERVICO_FINALIZOU) {
            frete.setStatus(StatusFrete.FINALIZADO);
        }

        //frete = freteRepository.save(frete);

        return notificar();
    }

    private Frete notificar() throws InvalidFirebaseToken {
        //notificacaoService.pushNotification("Frete concluido", "Verifique suas notificações", frete.getPrestadorServico().getFirebaseToken());

        frete.setNotificacaoEnviadaEm(LocalDateTime.now());
        //return freteRepository.save(frete);
        return frete;
    }
}
