package br.com.fretee.freteebackend.usuarios.dto;

import br.com.fretee.freteebackend.usuarios.entity.PrestadorServico;
import br.com.fretee.freteebackend.usuarios.entity.Usuario;
import lombok.Data;

@Data
public class PrestadorServicoDTO {
    private int usuarioId;
    private double longitude;
    private double latitude;

    public PrestadorServico toPrestadorService() {
        var prestadorServico = new PrestadorServico();

        prestadorServico.setLongitude(longitude);
        prestadorServico.setLatitude(latitude);
        prestadorServico.setUsuario(new Usuario());
        prestadorServico.getUsuario().setId(usuarioId);

        return prestadorServico;
    }
}
