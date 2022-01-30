package br.com.fretee.freteebackend.usuarios.dto;

import br.com.fretee.freteebackend.usuarios.entity.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class UsuarioDTO {
    private String dataCriacao;
    private String nomeCompleto;
    private float reputacao;
    private String telefone;
    private String foto;
    private String nomeUsuario;
    private int fretesRealizados;
    private double distancia;
    private InfoPrestadorServico prestadorServico;

    public UsuarioDTO(Usuario usuario) {
        setUsuarioInfo(usuario);
    }

    public UsuarioDTO(Usuario usuario, InfoPrestadorServico infoPrestadorServico) {
        setUsuarioInfo(usuario);
        this.prestadorServico = infoPrestadorServico;
    }

    private void setUsuarioInfo(Usuario usuario) {
        this.dataCriacao = fomatarData(usuario.getDataCriacao());
        this.nomeCompleto = usuario.getNomeCompleto();
        this.nomeUsuario = usuario.getNomeUsuario();
        this.foto = usuario.getFoto();
        this.telefone = usuario.getTelefone();
        this.reputacao = usuario.getReputacao();
    }

    private String fomatarData(LocalDate data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return data.format(formatter);
    }
}
