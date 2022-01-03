package br.com.fretee.freteebackend.usuarios.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(schema = "usuario", name = "prestador_servico")
@Data
public class PrestadorServico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private float reputacao;
    private double latitude;
    private double longitude;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    @OneToOne
    @JoinColumn(name = "veiculo_id", referencedColumnName = "id")
    private Veiculo veiculo;
}
