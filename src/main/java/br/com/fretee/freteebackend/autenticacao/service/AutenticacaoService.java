package br.com.fretee.freteebackend.autenticacao.service;

import br.com.fretee.freteebackend.autenticacao.api.AutenticacaoApi;
import br.com.fretee.freteebackend.autenticacao.entity.Credenciais;
import br.com.fretee.freteebackend.autenticacao.repository.CredenciasRepository;
import br.com.fretee.freteebackend.exceptions.CredenciasInvalidasException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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
    public int autenticarUsuario(String nome, String senha) throws CredenciasInvalidasException {
        return 0;
    }
}
