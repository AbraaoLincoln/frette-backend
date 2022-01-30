package br.com.fretee.freteebackend.usuarios.dto;

import br.com.fretee.freteebackend.usuarios.entity.PrestadorServico;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InfoPrestadorServico {
    private float reputacao;
    private int veiculo;

    public InfoPrestadorServico(PrestadorServico prestadorServico) {
        this.reputacao = prestadorServico.getReputacao();
        this.veiculo = prestadorServico.getVeiculo().getId();
    }
}
