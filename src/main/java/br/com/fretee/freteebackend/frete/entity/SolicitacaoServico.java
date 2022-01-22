package br.com.fretee.freteebackend.frete.entity;

import br.com.fretee.freteebackend.frete.enums.StatusSolicitacao;
import br.com.fretee.freteebackend.usuarios.entity.Usuario;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

//@Entity
//@Table(schema = "frete", name = "solicitacao_servico")
//@Data
public class SolicitacaoServico {
    @Id
    private long id;
    private boolean ativo = true;
    private StatusSolicitacao status;
    private LocalDateTime criado = LocalDateTime.now();

    @ManyToOne
    private Frete frete;

    @OneToOne
    private Usuario prestadorServico;
}
