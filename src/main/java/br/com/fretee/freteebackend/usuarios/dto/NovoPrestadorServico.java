package br.com.fretee.freteebackend.usuarios.dto;

import br.com.fretee.freteebackend.usuarios.entity.PrestadorServico;
import br.com.fretee.freteebackend.usuarios.entity.Usuario;
import lombok.Data;

@Data
public class NovoPrestadorServico {
    private String nomeUsuario;

    public PrestadorServico toPrestadorService(Usuario usuario) {
        var prestadorServico = new PrestadorServico();

        prestadorServico.setUsuario(usuario);

        return prestadorServico;
    }
}
