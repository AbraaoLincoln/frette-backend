package br.com.fretee.freteebackend.frete.exceptions;

public class CannotUpdateFreteStatusException extends Exception{

    public CannotUpdateFreteStatusException() {
        super("Nao foi possivel alterar o status do frete");
    }
    public CannotUpdateFreteStatusException(String msg) {
        super(msg);
    }
}
