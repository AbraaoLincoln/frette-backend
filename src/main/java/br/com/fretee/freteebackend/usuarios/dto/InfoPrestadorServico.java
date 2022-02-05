package br.com.fretee.freteebackend.usuarios.dto;

import br.com.fretee.freteebackend.usuarios.entity.PrestadorServico;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InfoPrestadorServico {
    private float reputacao;
    private int veiculo;
    private int numeroDeFretesRealizados;

    public InfoPrestadorServico(PrestadorServico prestadorServico) {
        this.veiculo = prestadorServico.getVeiculo().getId();
        this.numeroDeFretesRealizados = prestadorServico.getNumeroDeFretesRealizados();

        if(prestadorServico.getNumeroDeFretesRealizados() != 0) {
            this.reputacao = prestadorServico.getSomaNotasAvaliacao() / prestadorServico.getNumeroDeFretesRealizados();
        }else {
            this.reputacao = 0;
        }
    }
}
