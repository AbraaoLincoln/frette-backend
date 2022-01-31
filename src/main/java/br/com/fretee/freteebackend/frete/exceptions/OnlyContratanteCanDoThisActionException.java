package br.com.fretee.freteebackend.frete.exceptions;

public class OnlyContratanteCanDoThisActionException extends Exception{
    public OnlyContratanteCanDoThisActionException(String msg) {
        super(msg);
    }
}
