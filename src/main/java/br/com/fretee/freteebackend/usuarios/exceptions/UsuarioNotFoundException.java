package br.com.fretee.freteebackend.usuarios.exceptions;

public class UsuarioNotFoundException extends Exception{
    public UsuarioNotFoundException(String msg) {
        super(msg);
    }
}
