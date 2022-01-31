package br.com.fretee.freteebackend.autenticacao.api;

import br.com.fretee.freteebackend.autenticacao.exceptions.CredenciasInvalidasException;

public interface AutenticacaoApi {

    public void saveCredenciasUsuario(int usuarioId, String nome, String senha);

    public int autenticarUsuario(String nome, String senha) throws CredenciasInvalidasException;
}
