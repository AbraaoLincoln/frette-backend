package br.com.fretee.freteebackend.usuarios.dto;

import br.com.fretee.freteebackend.usuarios.entity.Veiculo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VeiculoDTO {
    private Float largura;
    private Float altura;
    private Float comprimento;
    private String foto;
    private Integer id;
    private String placa;

    public Veiculo toVeiculo() {
        var veiculo = new Veiculo();
        veiculo.setLargura(largura);
        veiculo.setAltura(altura);
        veiculo.setComprimento(comprimento);
        veiculo.setPlaca(placa);
        return veiculo;
    }
}
