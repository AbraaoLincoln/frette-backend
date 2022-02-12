package br.com.fretee.freteebackend.usuarios.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(schema = "usuario", name = "veiculos")
@Data
public class Veiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private float largura;
    private float altura;
    private float comprimento;
    private String foto;
    private String placa;

    @OneToOne(mappedBy = "veiculo")
    private PrestadorServico prestadorServico;
}
