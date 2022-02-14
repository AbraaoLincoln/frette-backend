package br.com.fretee.freteebackend.frete.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SolicitacaoServicoDTO {
    private int id;
    private String prestadorServicoNomeUsuario;
    private String contratanteNomeUsuario;
    private String origem;
    private String destino;
    private String data;
    private String hora;
    private float preco;
    private String descricaoCarga;
    private Boolean precisaAjudade;
    private String status;
}
