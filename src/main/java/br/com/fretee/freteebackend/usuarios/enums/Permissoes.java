package br.com.fretee.freteebackend.usuarios.enums;

import javax.persistence.Entity;

public enum Permissoes {
    ADMIN, USUARIO, PRESTADOR_SERVICO;

    public int getValue() {
        return ordinal() + 1;
    }
}
