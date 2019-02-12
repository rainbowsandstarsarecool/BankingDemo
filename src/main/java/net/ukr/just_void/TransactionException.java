package net.ukr.just_void;

public class TransactionException extends Exception {

    public TransactionException() {
    }

    TransactionException(String message) {
        super(message);
    }
}
