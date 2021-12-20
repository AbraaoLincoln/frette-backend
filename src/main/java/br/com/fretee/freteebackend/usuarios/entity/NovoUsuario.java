package br.com.fretee.freteebackend.usuarios.entity;

import lombok.Data;

@Data
public class NovoUsuario {
    private String nome;
    private String telefone;
    private String nomeAutenticacao;
    private String senha;

    public Usuario toUsuario() {
        var usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setTelefone(telefone);

        return usuario;
    }
}
