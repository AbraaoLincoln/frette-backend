package br.com.fretee.freteebackend.frete.entity;

import br.com.fretee.freteebackend.frete.enums.StatusFrete;
import br.com.fretee.freteebackend.usuarios.entity.Usuario;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(schema = "frete", name = "fretes")
@Data
public class Frete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private boolean ativo = true;
    private LocalDate dataCriacao = LocalDate.now();
    private String origem;
    private String destino;
    private LocalDate data;
    private LocalTime hora;
    private float preco;
    private String descricaoCarga;
    private Boolean precisaAjudade;
    @Enumerated(EnumType.STRING)
    private StatusFrete status;
    private LocalDateTime notificacaoEnviadaEm;

    @OneToOne
    private Usuario contratante;

    @OneToOne
    private Usuario prestadorServico;
}
