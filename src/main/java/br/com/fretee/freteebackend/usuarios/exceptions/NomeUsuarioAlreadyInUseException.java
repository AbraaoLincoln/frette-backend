package br.com.fretee.freteebackend.usuarios.exceptions;

public class NomeUsuarioAlreadyInUseException extends Exception{
    public NomeUsuarioAlreadyInUseException(String msg) {
        super(msg);
    }
}
