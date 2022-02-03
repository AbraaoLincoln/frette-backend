package br.com.fretee.freteebackend.frete.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class FreteDTO {
    private int id;
    private String prestadorServicoNomeUsuario;
    private String contratanteNomeUsuario;
    private String origem;
    private String destino;
    private LocalDate data;
    private LocalTime hora;
    private float preco;
    private String descricaoCarga;
    private Boolean precisaAjudade;
    private String status;
    private float notaContratanteRecebeu;
    private float notaPrestadorServicoRecebeu;
}
