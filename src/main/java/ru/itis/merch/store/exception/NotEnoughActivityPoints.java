package ru.itis.merch.store.exception;

public class NotEnoughActivityPoints extends RuntimeException {

    public NotEnoughActivityPoints(String message) {
        super(message);
    }
}
