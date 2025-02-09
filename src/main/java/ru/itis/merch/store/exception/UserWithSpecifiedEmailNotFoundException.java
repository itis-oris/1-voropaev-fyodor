package ru.itis.merch.store.exception;

public class UserWithSpecifiedEmailNotFoundException extends RuntimeException {

    public UserWithSpecifiedEmailNotFoundException(String message) {
        super(message);
    }
}
