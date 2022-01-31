package br.com.fretee.freteebackend.frete.exceptions;

public class OnlyPrestadorServicoCanDoThisActionException extends Exception{
    public OnlyPrestadorServicoCanDoThisActionException(String msg) {
        super(msg);
    }
}
