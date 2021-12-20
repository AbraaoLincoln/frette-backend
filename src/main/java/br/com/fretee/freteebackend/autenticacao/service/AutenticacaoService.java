package br.com.fretee.freteebackend.autenticacao.service;

import br.com.fretee.freteebackend.autenticacao.api.AutenticacaoApi;
import br.com.fretee.freteebackend.autenticacao.entity.Credenciais;
import br.com.fretee.freteebackend.autenticacao.repository.CredenciasRepository;
import br.com.fretee.freteebackend.exceptions.CredenciasInvalidas;
import org.springframework.beans.factory.annotation.Autowired;

public class AutenticacaoService implements AutenticacaoApi {
    @Autowired
    private CredenciasRepository credenciasRepository;

    @Override
    public void saveCredenciasUsuario(int usuarioId, String nome, String senha) {
        //TODO: validar nome(unico)
        //TODO: validar id do usuario
        //TODO: criptografar senha
        credenciasRepository.save(new Credenciais(usuarioId, nome, senha));
    }

    @Override
    public int autenticarUsuario(String nome, String senha) throws CredenciasInvalidas {
        return 0;
    }
}
