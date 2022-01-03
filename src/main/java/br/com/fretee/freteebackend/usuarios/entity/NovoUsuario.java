package br.com.fretee.freteebackend.usuarios.entity;

import lombok.Data;

@Data
public class NovoUsuario {
    private String nomeCompleto;
    private String telefone;
    private String nomeAutenticacao;
    private String senha;

    public Usuario toUsuario() {
        var usuario = new Usuario();
        usuario.setNomeCompleto(nomeCompleto);
        usuario.setTelefone(telefone);
        usuario.setNomeUsuario(nomeAutenticacao);
        usuario.setSenha(senha);
        return usuario;
    }
}
