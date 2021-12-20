package br.com.fretee.freteebackend.frete.entity;

import br.com.fretee.freteebackend.frete.enums.StatusFrete;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(schema = "frete", name = "fretes")
public class Frete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private boolean ativo = true;
    private LocalDate dataCriacao = LocalDate.now();
    private LocalDate data;
    private LocalTime hora;
    private float preco;
    private String descricaoCarga;
    private boolean precisaAjudade;
    private StatusFrete status;

    private int contratanteId;
    private int pretadorServicoId;
}
