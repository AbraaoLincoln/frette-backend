package br.com.fretee.freteebackend.usuarios.entity;

import br.com.fretee.freteebackend.frete.api.FreteApi;
import br.com.fretee.freteebackend.frete.dto.FreteDTO;
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

    @Transient
    public List<FreteDTO> getFretesComoContratante(@Autowired FreteApi freteApi) {
        return freteApi.getFretesByContratanteId(this.id);
    }

    @Transient
    public List<FreteDTO> getFretesComoPrestadorDeServico(@Autowired FreteApi freteApi) {
        return freteApi.getFretesByPrestadorDeServicoId(this.id);
    }
}
