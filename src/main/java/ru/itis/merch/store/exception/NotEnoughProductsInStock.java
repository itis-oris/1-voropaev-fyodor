package ru.itis.merch.store.exception;

public class NotEnoughProductsInStock extends RuntimeException {
    public NotEnoughProductsInStock(String message) {
        super(message);
    }
}
