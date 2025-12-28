package project.adapter.out.persistence;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(String msg) { super(msg); }
}
