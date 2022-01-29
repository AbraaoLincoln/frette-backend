package br.com.fretee.freteebackend.frete.entity;

import br.com.fretee.freteebackend.frete.exceptions.CannotUpdateFreteStatusException;
import br.com.fretee.freteebackend.frete.exceptions.InvalidFirebaseToken;

public interface FinalizarServicoStrategy {
    public Frete atualizarFreteParaFinalizado() throws CannotUpdateFreteStatusException, InvalidFirebaseToken;
}
