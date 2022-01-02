package br.com.fretee.freteebackend.usuarios.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "permissoes", schema = "usuario")
@Data
public class Permissao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
}
