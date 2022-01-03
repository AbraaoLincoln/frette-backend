package br.com.fretee.freteebackend.usuarios.dto;

import br.com.fretee.freteebackend.usuarios.entity.Veiculo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VeiculoDTO {
    private float largura;
    private float altura;
    private float comprimento;
    private String foto;
    private Integer id;

    public Veiculo toVeiculo() {
        var veiculo = new Veiculo();
        veiculo.setLargura(largura);
        veiculo.setAltura(altura);
        veiculo.setComprimento(comprimento);
        return veiculo;
    }
}
