package ru.itis.merch.store.exception;

public class UserWithSpecifiedEmailAlreadyExistException extends RuntimeException {

    public UserWithSpecifiedEmailAlreadyExistException(String message) {
        super(message);
    }
}
