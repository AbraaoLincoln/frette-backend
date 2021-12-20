package br.com.fretee.freteebackend.autenticacao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "autenticacao", name = "credencias")
@Data
@AllArgsConstructor
public class Credenciais {
    @Id
    private int usuarioId;
    private String nome;
    private String senha;
}
