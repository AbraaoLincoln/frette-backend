package br.com.fretee.freteebackend.usuarios.dto;

import br.com.fretee.freteebackend.usuarios.entity.PrestadorServico;
import br.com.fretee.freteebackend.usuarios.entity.Usuario;
import lombok.Data;

@Data
public class PrestadorServicoDTO {
    private String nomeCompleto;
    private float reputacao;
    private String telefone;
    private String foto;
    private double longitude;
    private double latitude;
    private int veiculo;

    public PrestadorServicoDTO(Usuario usuario, PrestadorServico prestadorServico) {
        this.nomeCompleto = usuario.getNomeCompleto();
        this.reputacao = prestadorServico.getReputacao();
        this.telefone = usuario.getTelefone();
        this.foto = usuario.getFoto();
        this.longitude = prestadorServico.getLongitude();
        this.latitude = prestadorServico.getLatitude();
        this.veiculo = prestadorServico.getVeiculo().getId();
    }
}
