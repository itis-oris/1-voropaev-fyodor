package ru.itis.merch.store.exception;

public class CsrfException extends RuntimeException {

    public CsrfException(String message) {
        super(message);
    }
}
