package br.com.fretee.freteebackend.frete.dto;

import lombok.Data;

@Data
public class FreteNotificacao {
    private int id;
    private String status;
    private String contratante;
    private String prestadorServico;
}
