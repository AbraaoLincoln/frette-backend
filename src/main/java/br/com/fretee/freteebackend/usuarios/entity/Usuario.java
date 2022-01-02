package br.com.fretee.freteebackend.usuarios.entity;

import br.com.fretee.freteebackend.frete.api.FreteApi;
import br.com.fretee.freteebackend.frete.dto.FreteDTO;
import br.com.fretee.freteebackend.usuarios.enums.Permissoes;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(schema = "usuario", name = "usuario")
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "boolean DEFAULT true")
    private boolean ativo = true;
    private LocalDate dataCriacao = LocalDate.now();
    private String nome;
    private float reputacao;
    private String telefone;
    private String foto;
    private String nomeUsuario;
    private String senha;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(schema = "usuario")
    private List<Permissao> permissoes;

    @Transient
    public List<FreteDTO> getFretesComoContratante(@Autowired FreteApi freteApi) {
        return freteApi.getFretesByContratanteId(this.id);
    }

    @Transient
    public List<FreteDTO> getFretesComoPrestadorDeServico(@Autowired FreteApi freteApi) {
        return freteApi.getFretesByPrestadorDeServicoId(this.id);
    }
}
